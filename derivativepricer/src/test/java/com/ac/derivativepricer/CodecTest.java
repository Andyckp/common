package com.ac.derivativepricer;

import java.nio.ByteBuffer;
import java.time.LocalDate;

import org.agrona.concurrent.UnsafeBuffer;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import org.junit.jupiter.api.Test;

import com.ac.derivativepricer.codec.AdjustedGreekDecoder;
import com.ac.derivativepricer.codec.AdjustedGreekEncoder;
import com.ac.derivativepricer.codec.ExpiryVolatilityDecoder;
import com.ac.derivativepricer.codec.ExpiryVolatilityEncoder;
import com.ac.derivativepricer.codec.InstrumentDecoder;
import com.ac.derivativepricer.codec.InstrumentEncoder;
import com.ac.derivativepricer.codec.InstrumentType;
import com.ac.derivativepricer.codec.MarketDataDecoder;
import com.ac.derivativepricer.codec.MarketDataEncoder;
import com.ac.derivativepricer.codec.MessageHeaderDecoder;
import com.ac.derivativepricer.codec.MessageHeaderEncoder;
import com.ac.derivativepricer.codec.PrimaryGreekDecoder;
import com.ac.derivativepricer.codec.PrimaryGreekEncoder;
import com.ac.derivativepricer.codec.SecondaryGreekDecoder;
import com.ac.derivativepricer.codec.SecondaryGreekEncoder;
import com.ac.derivativepricer.codec.StrategyDecoder;
import com.ac.derivativepricer.codec.StrategyEncoder;

public class CodecTest {

    private static final int BUFFER_SIZE = 1024;
    private static final LocalDate EPOCH = LocalDate.of(1970, 1, 1);
    private final UnsafeBuffer buffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));

    private void encodeHeader(MessageHeaderEncoder hdr, int blockLength, int templateId) {
        hdr.wrap(buffer, 0)
                .blockLength(blockLength)
                .templateId(templateId)
                .schemaId(1)
                .version(1);
    }

    private int encodeDate(LocalDate date) {
        int days = (int) date.toEpochDay();
        assertThat("Date out of uint16 range", days, allOf(greaterThanOrEqualTo(0), lessThanOrEqualTo(65535)));
        return days;
    }

    private LocalDate decodeDate(int encoded) {
        return EPOCH.plusDays(encoded & 0xFFFF);
    }

    @Test
    public void testMarketData() {
        MessageHeaderEncoder hdr = new MessageHeaderEncoder();
        MarketDataEncoder enc = new MarketDataEncoder();
        encodeHeader(hdr, enc.sbeBlockLength(), enc.sbeTemplateId());

        enc.wrap(buffer, hdr.encodedLength())
                .marketDataId("MD123456")
                .price().mantissa(456789L).exponent((byte) -4);

        MessageHeaderDecoder decHdr = new MessageHeaderDecoder().wrap(buffer, 0);
        MarketDataDecoder dec = new MarketDataDecoder();
        dec.wrap(buffer, decHdr.encodedLength(), decHdr.blockLength(), decHdr.version());

        assertThat(dec.marketDataId(), is("MD123456"));
        assertThat(dec.price().mantissa(), is(456789L));
        assertThat(dec.price().exponent(), is((byte) -4));
    }

    @Test
    public void testInstrument() {
        InstrumentEncoder enc = new InstrumentEncoder();
        MessageHeaderEncoder header = new MessageHeaderEncoder();
        encodeHeader(header, enc.sbeBlockLength(), enc.sbeTemplateId());

        LocalDate expiry = LocalDate.of(2026, 1, 31);
        int encodedExpiry = encodeDate(expiry); // localDate = uint16 = days since 1970-01-01

        enc.wrap(buffer, MessageHeaderEncoder.ENCODED_LENGTH)
                .instrumentId("OPT00001")
                .underlyingId("UNDLY000")
                .strike().mantissa(15000L).exponent((byte) -2);
        enc.expiry(encodedExpiry);
        enc.instrumentType(InstrumentType.STOCK);

        InstrumentDecoder dec = new InstrumentDecoder();
        MessageHeaderDecoder hdr = new MessageHeaderDecoder().wrap(buffer, 0);
        dec.wrap(buffer, hdr.encodedLength(), hdr.blockLength(), hdr.version());

        assertThat(dec.instrumentId(), is("OPT00001"));
        assertThat(dec.underlyingId(), is("UNDLY000"));
        assertThat(decodeDate(dec.expiry()), is(expiry));
        assertThat(dec.instrumentType(), is(InstrumentType.STOCK));
    }

    @Test
    public void testStrategy() {
        StrategyEncoder enc = new StrategyEncoder();
        encodeHeader(new MessageHeaderEncoder(), enc.sbeBlockLength(), enc.sbeTemplateId());

        enc.wrap(buffer, MessageHeaderEncoder.ENCODED_LENGTH)
                .strategyId("STRAT123")
                .underlyingId("UNDER001")
                .volatilityId("VOLTY001")
                .marketDataId("MKTDATA1");

        StrategyDecoder dec = new StrategyDecoder();
        MessageHeaderDecoder hdr = new MessageHeaderDecoder().wrap(buffer, 0);
        dec.wrap(buffer, hdr.encodedLength(), hdr.blockLength(), hdr.version());

        assertThat(dec.strategyId(), is("STRAT123"));
        assertThat(dec.underlyingId(), is("UNDER001"));
        assertThat(dec.volatilityId(), is("VOLTY001"));
        assertThat(dec.marketDataId(), is("MKTDATA1"));
    }

    @Test
    public void testExpiryVolatility() throws Exception {
        ExpiryVolatilityEncoder enc = new ExpiryVolatilityEncoder();
        encodeHeader(new MessageHeaderEncoder(), enc.sbeBlockLength(), enc.sbeTemplateId());

        LocalDate expiry = LocalDate.of(2025, 10, 20);
        enc.wrap(buffer, MessageHeaderEncoder.ENCODED_LENGTH)
                .volatilityId("VOL10000")
                .expiry(encodeDate(expiry))
                .fittingReference().mantissa(1).exponent((byte) -2);

        for (int i = 1; i <= 16; i++) {
            Object param = enc.getClass().getMethod("parameter" + i).invoke(enc);
            param.getClass().getMethod("mantissa", long.class).invoke(param, (long) i * 10);
            param.getClass().getMethod("exponent", byte.class).invoke(param, (byte) -2);
        }

        ExpiryVolatilityDecoder dec = new ExpiryVolatilityDecoder();
        MessageHeaderDecoder hdr = new MessageHeaderDecoder().wrap(buffer, 0);
        dec.wrap(buffer, hdr.encodedLength(), hdr.blockLength(), hdr.version());

        assertThat(dec.volatilityId(), is("VOL10000"));
        assertThat(decodeDate(dec.expiry()), is(expiry));
        assertThat(dec.parameter10().mantissa(), is(100L));
    }

    @Test
    public void testPrimaryGreek() {
        PrimaryGreekEncoder enc = new PrimaryGreekEncoder();
        encodeHeader(new MessageHeaderEncoder(), enc.sbeBlockLength(), enc.sbeTemplateId());

        enc.wrap(buffer, MessageHeaderEncoder.ENCODED_LENGTH)
                .strategyId("STRG001")
                .underlyingId("UND001")
                .instrumentId("OPT001")
                .referenceMarketDataId("REFMD01");
        enc.theo().mantissa(1000).exponent((byte) -2);
        enc.delta().mantissa(55).exponent((byte) -2);
        enc.gamma().mantissa(12).exponent((byte) -4);
        enc.referenceUnderlyingPrice().mantissa(30000).exponent((byte) -2);

        PrimaryGreekDecoder dec = new PrimaryGreekDecoder();
        MessageHeaderDecoder hdr = new MessageHeaderDecoder().wrap(buffer, 0);
        dec.wrap(buffer, hdr.encodedLength(), hdr.blockLength(), hdr.version());

        assertThat(dec.strategyId(), is("STRG001"));
        assertThat(dec.theo().mantissa(), is(1000L));
        assertThat(dec.gamma().exponent(), is((byte) -4));
    }

    @Test
    public void testSecondaryGreek() {
        SecondaryGreekEncoder enc = new SecondaryGreekEncoder();
        encodeHeader(new MessageHeaderEncoder(), enc.sbeBlockLength(), enc.sbeTemplateId());

        enc.wrap(buffer, MessageHeaderEncoder.ENCODED_LENGTH)
                .strategyId("SGRK002")
                .underlyingId("UND002")
                .instrumentId("OPT002")
                .vega().mantissa(21).exponent((byte) -2);
        enc.referenceVolatility().mantissa(30).exponent((byte) -2);
        enc.theta().mantissa(-7).exponent((byte) -3);
        enc.rho().mantissa(15).exponent((byte) -2);
        enc.vanna().mantissa(2).exponent((byte) -3);
        enc.volga().mantissa(6).exponent((byte) -3);

        SecondaryGreekDecoder dec = new SecondaryGreekDecoder();
        MessageHeaderDecoder hdr = new MessageHeaderDecoder().wrap(buffer, 0);
        dec.wrap(buffer, hdr.encodedLength(), hdr.blockLength(), hdr.version());

        assertThat(dec.instrumentId(), is("OPT002"));
        assertThat(dec.theta().mantissa(), is(-7L));
        assertThat(dec.volga().exponent(), is((byte) -3));
    }

    @Test
    public void testAdjustedGreek() {
        AdjustedGreekEncoder enc = new AdjustedGreekEncoder();
        encodeHeader(new MessageHeaderEncoder(), enc.sbeBlockLength(), enc.sbeTemplateId());

        enc.wrap(buffer, MessageHeaderEncoder.ENCODED_LENGTH)
                .strategyId("STRAT03")
                .underlyingId("UNDL03")
                .instrumentId("OPTX03");
        enc.theo().mantissa(2000L).exponent((byte) -2);
        enc.underlyingPrice().mantissa(10500L).exponent((byte) -2);

        AdjustedGreekDecoder dec = new AdjustedGreekDecoder();
        MessageHeaderDecoder hdr = new MessageHeaderDecoder().wrap(buffer, 0);
        dec.wrap(buffer, hdr.encodedLength(), hdr.blockLength(), hdr.version());

        assertThat(dec.strategyId(), is("STRAT03"));
        assertThat(dec.underlyingId(), is("UNDL03"));
        assertThat(dec.instrumentId(), is("OPTX03"));
        assertThat(dec.theo().mantissa(), is(2000L));
        assertThat(dec.theo().exponent(), is((byte) -2));
        assertThat(dec.underlyingPrice().mantissa(), is(10500L));
        assertThat(dec.underlyingPrice().exponent(), is((byte) -2));
    }
}
