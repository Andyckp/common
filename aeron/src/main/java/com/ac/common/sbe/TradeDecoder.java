/* Generated SBE (Simple Binary Encoding) message codec. */
package com.ac.common.sbe;

import org.agrona.DirectBuffer;

@SuppressWarnings("all")
public final class TradeDecoder
{
    public static final int BLOCK_LENGTH = 88;
    public static final int TEMPLATE_ID = 1;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final TradeDecoder parentMessage = this;
    private DirectBuffer buffer;
    private int initialOffset;
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

    public int initialOffset()
    {
        return initialOffset;
    }

    public int offset()
    {
        return offset;
    }

    public TradeDecoder wrap(
        final DirectBuffer buffer,
        final int offset,
        final int actingBlockLength,
        final int actingVersion)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.initialOffset = offset;
        this.offset = offset;
        this.actingBlockLength = actingBlockLength;
        this.actingVersion = actingVersion;
        limit(offset + actingBlockLength);

        return this;
    }

    public TradeDecoder wrapAndApplyHeader(
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

    public static int tradeIdId()
    {
        return 1;
    }

    public static int tradeIdSinceVersion()
    {
        return 0;
    }

    public static int tradeIdEncodingOffset()
    {
        return 8;
    }

    public static int tradeIdEncodingLength()
    {
        return 16;
    }

    public static String tradeIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte tradeIdNullValue()
    {
        return (byte)0;
    }

    public static byte tradeIdMinValue()
    {
        return (byte)32;
    }

    public static byte tradeIdMaxValue()
    {
        return (byte)126;
    }

    public static int tradeIdLength()
    {
        return 16;
    }


    public byte tradeId(final int index)
    {
        if (index < 0 || index >= 16)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 8 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String tradeIdCharacterEncoding()
    {
        return "US-ASCII";
    }

    public int getTradeId(final byte[] dst, final int dstOffset)
    {
        final int length = 16;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + dstOffset);
        }

        buffer.getBytes(offset + 8, dst, dstOffset, length);

        return length;
    }

    public String tradeId()
    {
        final byte[] dst = new byte[16];
        buffer.getBytes(offset + 8, dst, 0, 16);

        int end = 0;
        for (; end < 16 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.US_ASCII);
    }


    public int getTradeId(final Appendable value)
    {
        for (int i = 0; i < 16; ++i)
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

        return 16;
    }


    public static int instrumentIdId()
    {
        return 2;
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
        return 16;
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
        return 16;
    }


    public byte instrumentId(final int index)
    {
        if (index < 0 || index >= 16)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 24 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String instrumentIdCharacterEncoding()
    {
        return "US-ASCII";
    }

    public int getInstrumentId(final byte[] dst, final int dstOffset)
    {
        final int length = 16;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + dstOffset);
        }

        buffer.getBytes(offset + 24, dst, dstOffset, length);

        return length;
    }

    public String instrumentId()
    {
        final byte[] dst = new byte[16];
        buffer.getBytes(offset + 24, dst, 0, 16);

        int end = 0;
        for (; end < 16 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.US_ASCII);
    }


    public int getInstrumentId(final Appendable value)
    {
        for (int i = 0; i < 16; ++i)
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

        return 16;
    }


    public static int marketIdId()
    {
        return 3;
    }

    public static int marketIdSinceVersion()
    {
        return 0;
    }

    public static int marketIdEncodingOffset()
    {
        return 40;
    }

    public static int marketIdEncodingLength()
    {
        return 4;
    }

    public static String marketIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static int marketIdNullValue()
    {
        return -2147483648;
    }

    public static int marketIdMinValue()
    {
        return -2147483647;
    }

    public static int marketIdMaxValue()
    {
        return 2147483647;
    }

    public int marketId()
    {
        return buffer.getInt(offset + 40, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int portfolioIdId()
    {
        return 4;
    }

    public static int portfolioIdSinceVersion()
    {
        return 0;
    }

    public static int portfolioIdEncodingOffset()
    {
        return 44;
    }

    public static int portfolioIdEncodingLength()
    {
        return 16;
    }

    public static String portfolioIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte portfolioIdNullValue()
    {
        return (byte)0;
    }

    public static byte portfolioIdMinValue()
    {
        return (byte)32;
    }

    public static byte portfolioIdMaxValue()
    {
        return (byte)126;
    }

    public static int portfolioIdLength()
    {
        return 16;
    }


    public byte portfolioId(final int index)
    {
        if (index < 0 || index >= 16)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 44 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String portfolioIdCharacterEncoding()
    {
        return "US-ASCII";
    }

    public int getPortfolioId(final byte[] dst, final int dstOffset)
    {
        final int length = 16;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + dstOffset);
        }

        buffer.getBytes(offset + 44, dst, dstOffset, length);

        return length;
    }

    public String portfolioId()
    {
        final byte[] dst = new byte[16];
        buffer.getBytes(offset + 44, dst, 0, 16);

        int end = 0;
        for (; end < 16 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.US_ASCII);
    }


    public int getPortfolioId(final Appendable value)
    {
        for (int i = 0; i < 16; ++i)
        {
            final int c = buffer.getByte(offset + 44 + i) & 0xFF;
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

        return 16;
    }


    public static int sideId()
    {
        return 5;
    }

    public static int sideSinceVersion()
    {
        return 0;
    }

    public static int sideEncodingOffset()
    {
        return 60;
    }

    public static int sideEncodingLength()
    {
        return 1;
    }

    public static String sideMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte sideNullValue()
    {
        return (byte)0;
    }

    public static byte sideMinValue()
    {
        return (byte)32;
    }

    public static byte sideMaxValue()
    {
        return (byte)126;
    }

    public byte side()
    {
        return buffer.getByte(offset + 60);
    }


    public static int priceId()
    {
        return 6;
    }

    public static int priceSinceVersion()
    {
        return 0;
    }

    public static int priceEncodingOffset()
    {
        return 61;
    }

    public static int priceEncodingLength()
    {
        return 9;
    }

    public static String priceMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Decoder price = new Int64_8Decoder();

    /**
     * Trade Price
     *
     * @return Int64_8Decoder : Trade Price
     */
    public Int64_8Decoder price()
    {
        price.wrap(buffer, offset + 61);
        return price;
    }

    public static int quantityId()
    {
        return 7;
    }

    public static int quantitySinceVersion()
    {
        return 0;
    }

    public static int quantityEncodingOffset()
    {
        return 70;
    }

    public static int quantityEncodingLength()
    {
        return 9;
    }

    public static String quantityMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Decoder quantity = new Int64_8Decoder();

    /**
     * Trade Quantity
     *
     * @return Int64_8Decoder : Trade Quantity
     */
    public Int64_8Decoder quantity()
    {
        quantity.wrap(buffer, offset + 70);
        return quantity;
    }

    public static int createTsId()
    {
        return 8;
    }

    public static int createTsSinceVersion()
    {
        return 0;
    }

    public static int createTsEncodingOffset()
    {
        return 79;
    }

    public static int createTsEncodingLength()
    {
        return 8;
    }

    public static String createTsMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static long createTsNullValue()
    {
        return -9223372036854775808L;
    }

    public static long createTsMinValue()
    {
        return -9223372036854775807L;
    }

    public static long createTsMaxValue()
    {
        return 9223372036854775807L;
    }

    public long createTs()
    {
        return buffer.getLong(offset + 79, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int isDeleteId()
    {
        return 9;
    }

    public static int isDeleteSinceVersion()
    {
        return 0;
    }

    public static int isDeleteEncodingOffset()
    {
        return 87;
    }

    public static int isDeleteEncodingLength()
    {
        return 1;
    }

    public static String isDeleteMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static short isDeleteNullValue()
    {
        return (short)255;
    }

    public static short isDeleteMinValue()
    {
        return (short)0;
    }

    public static short isDeleteMaxValue()
    {
        return (short)254;
    }

    public short isDelete()
    {
        return ((short)(buffer.getByte(offset + 87) & 0xFF));
    }


    public String toString()
    {
        if (null == buffer)
        {
            return "";
        }

        final TradeDecoder decoder = new TradeDecoder();
        decoder.wrap(buffer, initialOffset, actingBlockLength, actingVersion);

        return decoder.appendTo(new StringBuilder()).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        if (null == buffer)
        {
            return builder;
        }

        final int originalLimit = limit();
        limit(initialOffset + actingBlockLength);
        builder.append("[Trade](sbeTemplateId=");
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
        final MessageHeaderDecoder messageHeader = messageHeader();
        if (messageHeader != null)
        {
            messageHeader.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("tradeId=");
        for (int i = 0; i < tradeIdLength() && tradeId(i) > 0; i++)
        {
            builder.append((char)tradeId(i));
        }
        builder.append('|');
        builder.append("instrumentId=");
        for (int i = 0; i < instrumentIdLength() && instrumentId(i) > 0; i++)
        {
            builder.append((char)instrumentId(i));
        }
        builder.append('|');
        builder.append("marketId=");
        builder.append(marketId());
        builder.append('|');
        builder.append("portfolioId=");
        for (int i = 0; i < portfolioIdLength() && portfolioId(i) > 0; i++)
        {
            builder.append((char)portfolioId(i));
        }
        builder.append('|');
        builder.append("side=");
        builder.append(side());
        builder.append('|');
        builder.append("price=");
        final Int64_8Decoder price = price();
        if (price != null)
        {
            price.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("quantity=");
        final Int64_8Decoder quantity = quantity();
        if (quantity != null)
        {
            quantity.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("createTs=");
        builder.append(createTs());
        builder.append('|');
        builder.append("isDelete=");
        builder.append(isDelete());

        limit(originalLimit);

        return builder;
    }
}
