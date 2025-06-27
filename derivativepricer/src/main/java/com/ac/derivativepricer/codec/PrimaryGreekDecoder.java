/* Generated SBE (Simple Binary Encoding) message codec. */
package com.ac.derivativepricer.codec;

import org.agrona.DirectBuffer;

@SuppressWarnings("all")
public final class PrimaryGreekDecoder
{
    public static final int BLOCK_LENGTH = 76;
    public static final int TEMPLATE_ID = 5;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1.0.0";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final PrimaryGreekDecoder parentMessage = this;
    private DirectBuffer buffer;
    private int offset;
    private int limit;
    int actingBlockLength;
    int actingVersion;

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

    public DirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public PrimaryGreekDecoder wrap(
        final DirectBuffer buffer,
        final int offset,
        final int actingBlockLength,
        final int actingVersion)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;
        this.actingBlockLength = actingBlockLength;
        this.actingVersion = actingVersion;
        limit(offset + actingBlockLength);

        return this;
    }

    public PrimaryGreekDecoder wrapAndApplyHeader(
        final DirectBuffer buffer,
        final int offset,
        final MessageHeaderDecoder headerDecoder)
    {
        headerDecoder.wrap(buffer, offset);

        final int templateId = headerDecoder.templateId();
        if (TEMPLATE_ID != templateId)
        {
            throw new IllegalStateException("Invalid TEMPLATE_ID: " + templateId);
        }

        return wrap(
            buffer,
            offset + MessageHeaderDecoder.ENCODED_LENGTH,
            headerDecoder.blockLength(),
            headerDecoder.version());
    }

    public PrimaryGreekDecoder sbeRewind()
    {
        return wrap(buffer, offset, actingBlockLength, actingVersion);
    }

    public int sbeDecodedLength()
    {
        final int currentLimit = limit();
        sbeSkip();
        final int decodedLength = encodedLength();
        limit(currentLimit);

        return decodedLength;
    }

    public int actingVersion()
    {
        return actingVersion;
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

    private final MessageHeaderDecoder messageHeader = new MessageHeaderDecoder();

    /**
     * Standard Message Header
     *
     * @return MessageHeaderDecoder : Standard Message Header
     */
    public MessageHeaderDecoder messageHeader()
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


    public byte strategyId(final int index)
    {
        if (index < 0 || index >= 8)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 8 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String strategyIdCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public int getStrategyId(final byte[] dst, final int dstOffset)
    {
        final int length = 8;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + dstOffset);
        }

        buffer.getBytes(offset + 8, dst, dstOffset, length);

        return length;
    }

    public String strategyId()
    {
        final byte[] dst = new byte[8];
        buffer.getBytes(offset + 8, dst, 0, 8);

        int end = 0;
        for (; end < 8 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.US_ASCII);
    }


    public int getStrategyId(final Appendable value)
    {
        for (int i = 0; i < 8; ++i)
        {
            final int c = buffer.getByte(offset + 8 + i) & 0xFF;
            if (c == 0)
            {
                return i;
            }

            try
            {
                value.append(c > 127 ? '?' : (char)c);
            }
            catch (final java.io.IOException ex)
            {
                throw new java.io.UncheckedIOException(ex);
            }
        }

        return 8;
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


    public byte underlyingId(final int index)
    {
        if (index < 0 || index >= 8)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 16 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String underlyingIdCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public int getUnderlyingId(final byte[] dst, final int dstOffset)
    {
        final int length = 8;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + dstOffset);
        }

        buffer.getBytes(offset + 16, dst, dstOffset, length);

        return length;
    }

    public String underlyingId()
    {
        final byte[] dst = new byte[8];
        buffer.getBytes(offset + 16, dst, 0, 8);

        int end = 0;
        for (; end < 8 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.US_ASCII);
    }


    public int getUnderlyingId(final Appendable value)
    {
        for (int i = 0; i < 8; ++i)
        {
            final int c = buffer.getByte(offset + 16 + i) & 0xFF;
            if (c == 0)
            {
                return i;
            }

            try
            {
                value.append(c > 127 ? '?' : (char)c);
            }
            catch (final java.io.IOException ex)
            {
                throw new java.io.UncheckedIOException(ex);
            }
        }

        return 8;
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


    public byte instrumentId(final int index)
    {
        if (index < 0 || index >= 8)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 24 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String instrumentIdCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public int getInstrumentId(final byte[] dst, final int dstOffset)
    {
        final int length = 8;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + dstOffset);
        }

        buffer.getBytes(offset + 24, dst, dstOffset, length);

        return length;
    }

    public String instrumentId()
    {
        final byte[] dst = new byte[8];
        buffer.getBytes(offset + 24, dst, 0, 8);

        int end = 0;
        for (; end < 8 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.US_ASCII);
    }


    public int getInstrumentId(final Appendable value)
    {
        for (int i = 0; i < 8; ++i)
        {
            final int c = buffer.getByte(offset + 24 + i) & 0xFF;
            if (c == 0)
            {
                return i;
            }

            try
            {
                value.append(c > 127 ? '?' : (char)c);
            }
            catch (final java.io.IOException ex)
            {
                throw new java.io.UncheckedIOException(ex);
            }
        }

        return 8;
    }


    public static int referenceMarketDataIdId()
    {
        return 4;
    }

    public static int referenceMarketDataIdSinceVersion()
    {
        return 0;
    }

    public static int referenceMarketDataIdEncodingOffset()
    {
        return 32;
    }

    public static int referenceMarketDataIdEncodingLength()
    {
        return 8;
    }

    public static String referenceMarketDataIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte referenceMarketDataIdNullValue()
    {
        return (byte)0;
    }

    public static byte referenceMarketDataIdMinValue()
    {
        return (byte)32;
    }

    public static byte referenceMarketDataIdMaxValue()
    {
        return (byte)126;
    }

    public static int referenceMarketDataIdLength()
    {
        return 8;
    }


    public byte referenceMarketDataId(final int index)
    {
        if (index < 0 || index >= 8)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 32 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String referenceMarketDataIdCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public int getReferenceMarketDataId(final byte[] dst, final int dstOffset)
    {
        final int length = 8;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + dstOffset);
        }

        buffer.getBytes(offset + 32, dst, dstOffset, length);

        return length;
    }

    public String referenceMarketDataId()
    {
        final byte[] dst = new byte[8];
        buffer.getBytes(offset + 32, dst, 0, 8);

        int end = 0;
        for (; end < 8 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.US_ASCII);
    }


    public int getReferenceMarketDataId(final Appendable value)
    {
        for (int i = 0; i < 8; ++i)
        {
            final int c = buffer.getByte(offset + 32 + i) & 0xFF;
            if (c == 0)
            {
                return i;
            }

            try
            {
                value.append(c > 127 ? '?' : (char)c);
            }
            catch (final java.io.IOException ex)
            {
                throw new java.io.UncheckedIOException(ex);
            }
        }

        return 8;
    }


    public static int theoId()
    {
        return 5;
    }

    public static int theoSinceVersion()
    {
        return 0;
    }

    public static int theoEncodingOffset()
    {
        return 40;
    }

    public static int theoEncodingLength()
    {
        return 9;
    }

    public static String theoMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Decoder theo = new Int64_8Decoder();

    /**
     * Theoretical Price
     *
     * @return Int64_8Decoder : Theoretical Price
     */
    public Int64_8Decoder theo()
    {
        theo.wrap(buffer, offset + 40);
        return theo;
    }

    public static int deltaId()
    {
        return 6;
    }

    public static int deltaSinceVersion()
    {
        return 0;
    }

    public static int deltaEncodingOffset()
    {
        return 49;
    }

    public static int deltaEncodingLength()
    {
        return 9;
    }

    public static String deltaMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Decoder delta = new Int64_8Decoder();

    /**
     * Delta Greek
     *
     * @return Int64_8Decoder : Delta Greek
     */
    public Int64_8Decoder delta()
    {
        delta.wrap(buffer, offset + 49);
        return delta;
    }

    public static int gammaId()
    {
        return 7;
    }

    public static int gammaSinceVersion()
    {
        return 0;
    }

    public static int gammaEncodingOffset()
    {
        return 58;
    }

    public static int gammaEncodingLength()
    {
        return 9;
    }

    public static String gammaMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Decoder gamma = new Int64_8Decoder();

    /**
     * Gamma Greek
     *
     * @return Int64_8Decoder : Gamma Greek
     */
    public Int64_8Decoder gamma()
    {
        gamma.wrap(buffer, offset + 58);
        return gamma;
    }

    public static int referenceUnderlyingPriceId()
    {
        return 8;
    }

    public static int referenceUnderlyingPriceSinceVersion()
    {
        return 0;
    }

    public static int referenceUnderlyingPriceEncodingOffset()
    {
        return 67;
    }

    public static int referenceUnderlyingPriceEncodingLength()
    {
        return 9;
    }

    public static String referenceUnderlyingPriceMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Decoder referenceUnderlyingPrice = new Int64_8Decoder();

    /**
     * Reference Underlying Price
     *
     * @return Int64_8Decoder : Reference Underlying Price
     */
    public Int64_8Decoder referenceUnderlyingPrice()
    {
        referenceUnderlyingPrice.wrap(buffer, offset + 67);
        return referenceUnderlyingPrice;
    }

    public String toString()
    {
        if (null == buffer)
        {
            return "";
        }

        final PrimaryGreekDecoder decoder = new PrimaryGreekDecoder();
        decoder.wrap(buffer, offset, actingBlockLength, actingVersion);

        return decoder.appendTo(new StringBuilder()).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        if (null == buffer)
        {
            return builder;
        }

        final int originalLimit = limit();
        limit(offset + actingBlockLength);
        builder.append("[PrimaryGreek](sbeTemplateId=");
        builder.append(TEMPLATE_ID);
        builder.append("|sbeSchemaId=");
        builder.append(SCHEMA_ID);
        builder.append("|sbeSchemaVersion=");
        if (parentMessage.actingVersion != SCHEMA_VERSION)
        {
            builder.append(parentMessage.actingVersion);
            builder.append('/');
        }
        builder.append(SCHEMA_VERSION);
        builder.append("|sbeBlockLength=");
        if (actingBlockLength != BLOCK_LENGTH)
        {
            builder.append(actingBlockLength);
            builder.append('/');
        }
        builder.append(BLOCK_LENGTH);
        builder.append("):");
        builder.append("messageHeader=");
        final MessageHeaderDecoder messageHeader = this.messageHeader();
        if (null != messageHeader)
        {
            messageHeader.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("strategyId=");
        for (int i = 0; i < strategyIdLength() && this.strategyId(i) > 0; i++)
        {
            builder.append((char)this.strategyId(i));
        }
        builder.append('|');
        builder.append("underlyingId=");
        for (int i = 0; i < underlyingIdLength() && this.underlyingId(i) > 0; i++)
        {
            builder.append((char)this.underlyingId(i));
        }
        builder.append('|');
        builder.append("instrumentId=");
        for (int i = 0; i < instrumentIdLength() && this.instrumentId(i) > 0; i++)
        {
            builder.append((char)this.instrumentId(i));
        }
        builder.append('|');
        builder.append("referenceMarketDataId=");
        for (int i = 0; i < referenceMarketDataIdLength() && this.referenceMarketDataId(i) > 0; i++)
        {
            builder.append((char)this.referenceMarketDataId(i));
        }
        builder.append('|');
        builder.append("theo=");
        final Int64_8Decoder theo = this.theo();
        if (null != theo)
        {
            theo.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("delta=");
        final Int64_8Decoder delta = this.delta();
        if (null != delta)
        {
            delta.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("gamma=");
        final Int64_8Decoder gamma = this.gamma();
        if (null != gamma)
        {
            gamma.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("referenceUnderlyingPrice=");
        final Int64_8Decoder referenceUnderlyingPrice = this.referenceUnderlyingPrice();
        if (null != referenceUnderlyingPrice)
        {
            referenceUnderlyingPrice.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }

        limit(originalLimit);

        return builder;
    }
    
    public PrimaryGreekDecoder sbeSkip()
    {
        sbeRewind();

        return this;
    }
}
