/* Generated SBE (Simple Binary Encoding) message codec. */
package com.ac.common.sbe;

import org.agrona.MutableDirectBuffer;

@SuppressWarnings("all")
public final class TradeEncoder
{
    public static final int BLOCK_LENGTH = 88;
    public static final int TEMPLATE_ID = 1;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final TradeEncoder parentMessage = this;
    private MutableDirectBuffer buffer;
    private int initialOffset;
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

    public int initialOffset()
    {
        return initialOffset;
    }

    public int offset()
    {
        return offset;
    }

    public TradeEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.initialOffset = offset;
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);

        return this;
    }

    public TradeEncoder wrapAndApplyHeader(
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


    public TradeEncoder tradeId(final int index, final byte value)
    {
        if (index < 0 || index >= 16)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 8 + (index * 1);
        buffer.putByte(pos, value);

        return this;
    }

    public static String tradeIdCharacterEncoding()
    {
        return "US-ASCII";
    }

    public TradeEncoder putTradeId(final byte[] src, final int srcOffset)
    {
        final int length = 16;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + srcOffset);
        }

        buffer.putBytes(offset + 8, src, srcOffset, length);

        return this;
    }

    public TradeEncoder tradeId(final String src)
    {
        final int length = 16;
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

    public TradeEncoder tradeId(final CharSequence src)
    {
        final int length = 16;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("CharSequence too large for copy: byte length=" + srcLength);
        }

        for (int i = 0; i < srcLength; ++i)
        {
            final char charValue = src.charAt(i);
            final byte byteValue = charValue > 127 ? (byte)'?' : (byte)charValue;
            buffer.putByte(offset + 8 + i, byteValue);
        }

        for (int i = srcLength; i < length; ++i)
        {
            buffer.putByte(offset + 8 + i, (byte)0);
        }

        return this;
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


    public TradeEncoder instrumentId(final int index, final byte value)
    {
        if (index < 0 || index >= 16)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 24 + (index * 1);
        buffer.putByte(pos, value);

        return this;
    }

    public static String instrumentIdCharacterEncoding()
    {
        return "US-ASCII";
    }

    public TradeEncoder putInstrumentId(final byte[] src, final int srcOffset)
    {
        final int length = 16;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + srcOffset);
        }

        buffer.putBytes(offset + 24, src, srcOffset, length);

        return this;
    }

    public TradeEncoder instrumentId(final String src)
    {
        final int length = 16;
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

    public TradeEncoder instrumentId(final CharSequence src)
    {
        final int length = 16;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("CharSequence too large for copy: byte length=" + srcLength);
        }

        for (int i = 0; i < srcLength; ++i)
        {
            final char charValue = src.charAt(i);
            final byte byteValue = charValue > 127 ? (byte)'?' : (byte)charValue;
            buffer.putByte(offset + 24 + i, byteValue);
        }

        for (int i = srcLength; i < length; ++i)
        {
            buffer.putByte(offset + 24 + i, (byte)0);
        }

        return this;
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

    public TradeEncoder marketId(final int value)
    {
        buffer.putInt(offset + 40, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
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


    public TradeEncoder portfolioId(final int index, final byte value)
    {
        if (index < 0 || index >= 16)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 44 + (index * 1);
        buffer.putByte(pos, value);

        return this;
    }

    public static String portfolioIdCharacterEncoding()
    {
        return "US-ASCII";
    }

    public TradeEncoder putPortfolioId(final byte[] src, final int srcOffset)
    {
        final int length = 16;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + srcOffset);
        }

        buffer.putBytes(offset + 44, src, srcOffset, length);

        return this;
    }

    public TradeEncoder portfolioId(final String src)
    {
        final int length = 16;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 44, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 44 + start, (byte)0);
        }

        return this;
    }

    public TradeEncoder portfolioId(final CharSequence src)
    {
        final int length = 16;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("CharSequence too large for copy: byte length=" + srcLength);
        }

        for (int i = 0; i < srcLength; ++i)
        {
            final char charValue = src.charAt(i);
            final byte byteValue = charValue > 127 ? (byte)'?' : (byte)charValue;
            buffer.putByte(offset + 44 + i, byteValue);
        }

        for (int i = srcLength; i < length; ++i)
        {
            buffer.putByte(offset + 44 + i, (byte)0);
        }

        return this;
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

    public TradeEncoder side(final byte value)
    {
        buffer.putByte(offset + 60, value);
        return this;
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

    private final Int64_8Encoder price = new Int64_8Encoder();

    /**
     * Trade Price
     *
     * @return Int64_8Encoder : Trade Price
     */
    public Int64_8Encoder price()
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

    private final Int64_8Encoder quantity = new Int64_8Encoder();

    /**
     * Trade Quantity
     *
     * @return Int64_8Encoder : Trade Quantity
     */
    public Int64_8Encoder quantity()
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

    public TradeEncoder createTs(final long value)
    {
        buffer.putLong(offset + 79, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
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

    public TradeEncoder isDelete(final short value)
    {
        buffer.putByte(offset + 87, (byte)value);
        return this;
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

        final TradeDecoder decoder = new TradeDecoder();
        decoder.wrap(buffer, initialOffset, BLOCK_LENGTH, SCHEMA_VERSION);

        return decoder.appendTo(builder);
    }
}
