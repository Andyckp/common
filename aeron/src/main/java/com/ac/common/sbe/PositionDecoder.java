/* Generated SBE (Simple Binary Encoding) message codec. */
package com.ac.common.sbe;

import org.agrona.DirectBuffer;


/**
 * Position Message
 */
@SuppressWarnings("all")
public final class PositionDecoder
{
    public static final int BLOCK_LENGTH = 66;
    public static final int TEMPLATE_ID = 1;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final PositionDecoder parentMessage = this;
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

    public PositionDecoder wrap(
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

    public PositionDecoder wrapAndApplyHeader(
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

    public static int instrumentIdId()
    {
        return 1;
    }

    public static int instrumentIdSinceVersion()
    {
        return 0;
    }

    public static int instrumentIdEncodingOffset()
    {
        return 8;
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

        final int pos = offset + 8 + (index * 1);

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

        buffer.getBytes(offset + 8, dst, dstOffset, length);

        return length;
    }

    public String instrumentId()
    {
        final byte[] dst = new byte[16];
        buffer.getBytes(offset + 8, dst, 0, 16);

        int end = 0;
        for (; end < 16 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.US_ASCII);
    }


    public int getInstrumentId(final Appendable value)
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


    public static int portfolioIdId()
    {
        return 2;
    }

    public static int portfolioIdSinceVersion()
    {
        return 0;
    }

    public static int portfolioIdEncodingOffset()
    {
        return 24;
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

        final int pos = offset + 24 + (index * 1);

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

        buffer.getBytes(offset + 24, dst, dstOffset, length);

        return length;
    }

    public String portfolioId()
    {
        final byte[] dst = new byte[16];
        buffer.getBytes(offset + 24, dst, 0, 16);

        int end = 0;
        for (; end < 16 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.US_ASCII);
    }


    public int getPortfolioId(final Appendable value)
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


    public static int notionalId()
    {
        return 3;
    }

    public static int notionalSinceVersion()
    {
        return 0;
    }

    public static int notionalEncodingOffset()
    {
        return 40;
    }

    public static int notionalEncodingLength()
    {
        return 9;
    }

    public static String notionalMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Decoder notional = new Int64_8Decoder();

    /**
     * Total Notional Value
     *
     * @return Int64_8Decoder : Total Notional Value
     */
    public Int64_8Decoder notional()
    {
        notional.wrap(buffer, offset + 40);
        return notional;
    }

    public static int quantityId()
    {
        return 4;
    }

    public static int quantitySinceVersion()
    {
        return 0;
    }

    public static int quantityEncodingOffset()
    {
        return 49;
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
     * Total Quantity
     *
     * @return Int64_8Decoder : Total Quantity
     */
    public Int64_8Decoder quantity()
    {
        quantity.wrap(buffer, offset + 49);
        return quantity;
    }

    public static int createTsId()
    {
        return 5;
    }

    public static int createTsSinceVersion()
    {
        return 0;
    }

    public static int createTsEncodingOffset()
    {
        return 58;
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
        return buffer.getLong(offset + 58, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public String toString()
    {
        if (null == buffer)
        {
            return "";
        }

        final PositionDecoder decoder = new PositionDecoder();
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
        builder.append("[Position](sbeTemplateId=");
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
        builder.append("instrumentId=");
        for (int i = 0; i < instrumentIdLength() && instrumentId(i) > 0; i++)
        {
            builder.append((char)instrumentId(i));
        }
        builder.append('|');
        builder.append("portfolioId=");
        for (int i = 0; i < portfolioIdLength() && portfolioId(i) > 0; i++)
        {
            builder.append((char)portfolioId(i));
        }
        builder.append('|');
        builder.append("notional=");
        final Int64_8Decoder notional = notional();
        if (notional != null)
        {
            notional.appendTo(builder);
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

        limit(originalLimit);

        return builder;
    }
}
