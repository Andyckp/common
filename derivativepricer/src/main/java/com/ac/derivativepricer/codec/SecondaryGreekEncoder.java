/* Generated SBE (Simple Binary Encoding) message codec. */
package com.ac.derivativepricer.codec;

import org.agrona.MutableDirectBuffer;

@SuppressWarnings("all")
public final class SecondaryGreekEncoder
{
    public static final int BLOCK_LENGTH = 86;
    public static final int TEMPLATE_ID = 6;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1.0.0";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final SecondaryGreekEncoder parentMessage = this;
    private MutableDirectBuffer buffer;
    private int offset;
    private int limit;

    public int sbeBlockLength()
    {
        return BLOCK_LENGTH;
    }

    public int sbeTemplateId()
    {
        return TEMPLATE_ID;
    }

    public int sbeSchemaId()
    {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion()
    {
        return SCHEMA_VERSION;
    }

    public String sbeSemanticType()
    {
        return "";
    }

    public MutableDirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public SecondaryGreekEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);

        return this;
    }

    public SecondaryGreekEncoder wrapAndApplyHeader(
        final MutableDirectBuffer buffer, final int offset, final MessageHeaderEncoder headerEncoder)
    {
        headerEncoder
            .wrap(buffer, offset)
            .blockLength(BLOCK_LENGTH)
            .templateId(TEMPLATE_ID)
            .schemaId(SCHEMA_ID)
            .version(SCHEMA_VERSION);

        return wrap(buffer, offset + MessageHeaderEncoder.ENCODED_LENGTH);
    }

    public int encodedLength()
    {
        return limit - offset;
    }

    public int limit()
    {
        return limit;
    }

    public void limit(final int limit)
    {
        this.limit = limit;
    }

    public static int messageHeaderId()
    {
        return 0;
    }

    public static int messageHeaderSinceVersion()
    {
        return 0;
    }

    public static int messageHeaderEncodingOffset()
    {
        return 0;
    }

    public static int messageHeaderEncodingLength()
    {
        return 8;
    }

    public static String messageHeaderMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final MessageHeaderEncoder messageHeader = new MessageHeaderEncoder();

    /**
     * Standard Message Header
     *
     * @return MessageHeaderEncoder : Standard Message Header
     */
    public MessageHeaderEncoder messageHeader()
    {
        messageHeader.wrap(buffer, offset + 0);
        return messageHeader;
    }

    public static int strategyIdId()
    {
        return 1;
    }

    public static int strategyIdSinceVersion()
    {
        return 0;
    }

    public static int strategyIdEncodingOffset()
    {
        return 8;
    }

    public static int strategyIdEncodingLength()
    {
        return 8;
    }

    public static String strategyIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte strategyIdNullValue()
    {
        return (byte)0;
    }

    public static byte strategyIdMinValue()
    {
        return (byte)32;
    }

    public static byte strategyIdMaxValue()
    {
        return (byte)126;
    }

    public static int strategyIdLength()
    {
        return 8;
    }


    public SecondaryGreekEncoder strategyId(final int index, final byte value)
    {
        if (index < 0 || index >= 8)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 8 + (index * 1);
        buffer.putByte(pos, value);

        return this;
    }

    public static String strategyIdCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public SecondaryGreekEncoder putStrategyId(final byte[] src, final int srcOffset)
    {
        final int length = 8;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + srcOffset);
        }

        buffer.putBytes(offset + 8, src, srcOffset, length);

        return this;
    }

    public SecondaryGreekEncoder strategyId(final String src)
    {
        final int length = 8;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 8, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 8 + start, (byte)0);
        }

        return this;
    }

    public SecondaryGreekEncoder strategyId(final CharSequence src)
    {
        final int length = 8;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("CharSequence too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 8, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 8 + start, (byte)0);
        }

        return this;
    }

    public static int underlyingIdId()
    {
        return 2;
    }

    public static int underlyingIdSinceVersion()
    {
        return 0;
    }

    public static int underlyingIdEncodingOffset()
    {
        return 16;
    }

    public static int underlyingIdEncodingLength()
    {
        return 8;
    }

    public static String underlyingIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte underlyingIdNullValue()
    {
        return (byte)0;
    }

    public static byte underlyingIdMinValue()
    {
        return (byte)32;
    }

    public static byte underlyingIdMaxValue()
    {
        return (byte)126;
    }

    public static int underlyingIdLength()
    {
        return 8;
    }


    public SecondaryGreekEncoder underlyingId(final int index, final byte value)
    {
        if (index < 0 || index >= 8)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 16 + (index * 1);
        buffer.putByte(pos, value);

        return this;
    }

    public static String underlyingIdCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public SecondaryGreekEncoder putUnderlyingId(final byte[] src, final int srcOffset)
    {
        final int length = 8;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + srcOffset);
        }

        buffer.putBytes(offset + 16, src, srcOffset, length);

        return this;
    }

    public SecondaryGreekEncoder underlyingId(final String src)
    {
        final int length = 8;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 16, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 16 + start, (byte)0);
        }

        return this;
    }

    public SecondaryGreekEncoder underlyingId(final CharSequence src)
    {
        final int length = 8;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("CharSequence too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 16, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 16 + start, (byte)0);
        }

        return this;
    }

    public static int instrumentIdId()
    {
        return 3;
    }

    public static int instrumentIdSinceVersion()
    {
        return 0;
    }

    public static int instrumentIdEncodingOffset()
    {
        return 24;
    }

    public static int instrumentIdEncodingLength()
    {
        return 8;
    }

    public static String instrumentIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte instrumentIdNullValue()
    {
        return (byte)0;
    }

    public static byte instrumentIdMinValue()
    {
        return (byte)32;
    }

    public static byte instrumentIdMaxValue()
    {
        return (byte)126;
    }

    public static int instrumentIdLength()
    {
        return 8;
    }


    public SecondaryGreekEncoder instrumentId(final int index, final byte value)
    {
        if (index < 0 || index >= 8)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 24 + (index * 1);
        buffer.putByte(pos, value);

        return this;
    }

    public static String instrumentIdCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public SecondaryGreekEncoder putInstrumentId(final byte[] src, final int srcOffset)
    {
        final int length = 8;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + srcOffset);
        }

        buffer.putBytes(offset + 24, src, srcOffset, length);

        return this;
    }

    public SecondaryGreekEncoder instrumentId(final String src)
    {
        final int length = 8;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 24, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 24 + start, (byte)0);
        }

        return this;
    }

    public SecondaryGreekEncoder instrumentId(final CharSequence src)
    {
        final int length = 8;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("CharSequence too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 24, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 24 + start, (byte)0);
        }

        return this;
    }

    public static int vegaId()
    {
        return 4;
    }

    public static int vegaSinceVersion()
    {
        return 0;
    }

    public static int vegaEncodingOffset()
    {
        return 32;
    }

    public static int vegaEncodingLength()
    {
        return 9;
    }

    public static String vegaMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder vega = new Int64_8Encoder();

    /**
     * Vega Sensitivity
     *
     * @return Int64_8Encoder : Vega Sensitivity
     */
    public Int64_8Encoder vega()
    {
        vega.wrap(buffer, offset + 32);
        return vega;
    }

    public static int referenceVolatilityId()
    {
        return 5;
    }

    public static int referenceVolatilitySinceVersion()
    {
        return 0;
    }

    public static int referenceVolatilityEncodingOffset()
    {
        return 41;
    }

    public static int referenceVolatilityEncodingLength()
    {
        return 9;
    }

    public static String referenceVolatilityMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder referenceVolatility = new Int64_8Encoder();

    /**
     * Reference Volatility
     *
     * @return Int64_8Encoder : Reference Volatility
     */
    public Int64_8Encoder referenceVolatility()
    {
        referenceVolatility.wrap(buffer, offset + 41);
        return referenceVolatility;
    }

    public static int thetaId()
    {
        return 6;
    }

    public static int thetaSinceVersion()
    {
        return 0;
    }

    public static int thetaEncodingOffset()
    {
        return 50;
    }

    public static int thetaEncodingLength()
    {
        return 9;
    }

    public static String thetaMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder theta = new Int64_8Encoder();

    /**
     * Theta Sensitivity
     *
     * @return Int64_8Encoder : Theta Sensitivity
     */
    public Int64_8Encoder theta()
    {
        theta.wrap(buffer, offset + 50);
        return theta;
    }

    public static int rhoId()
    {
        return 7;
    }

    public static int rhoSinceVersion()
    {
        return 0;
    }

    public static int rhoEncodingOffset()
    {
        return 59;
    }

    public static int rhoEncodingLength()
    {
        return 9;
    }

    public static String rhoMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder rho = new Int64_8Encoder();

    /**
     * Rho Sensitivity
     *
     * @return Int64_8Encoder : Rho Sensitivity
     */
    public Int64_8Encoder rho()
    {
        rho.wrap(buffer, offset + 59);
        return rho;
    }

    public static int vannaId()
    {
        return 8;
    }

    public static int vannaSinceVersion()
    {
        return 0;
    }

    public static int vannaEncodingOffset()
    {
        return 68;
    }

    public static int vannaEncodingLength()
    {
        return 9;
    }

    public static String vannaMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder vanna = new Int64_8Encoder();

    /**
     * Vanna
     *
     * @return Int64_8Encoder : Vanna
     */
    public Int64_8Encoder vanna()
    {
        vanna.wrap(buffer, offset + 68);
        return vanna;
    }

    public static int volgaId()
    {
        return 9;
    }

    public static int volgaSinceVersion()
    {
        return 0;
    }

    public static int volgaEncodingOffset()
    {
        return 77;
    }

    public static int volgaEncodingLength()
    {
        return 9;
    }

    public static String volgaMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder volga = new Int64_8Encoder();

    /**
     * Volga
     *
     * @return Int64_8Encoder : Volga
     */
    public Int64_8Encoder volga()
    {
        volga.wrap(buffer, offset + 77);
        return volga;
    }

    public String toString()
    {
        if (null == buffer)
        {
            return "";
        }

        return appendTo(new StringBuilder()).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        if (null == buffer)
        {
            return builder;
        }

        final SecondaryGreekDecoder decoder = new SecondaryGreekDecoder();
        decoder.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return decoder.appendTo(builder);
    }
}
