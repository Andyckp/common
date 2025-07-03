/* Generated SBE (Simple Binary Encoding) message codec. */
package com.ac.derivativepricer.codec;

import org.agrona.DirectBuffer;

@SuppressWarnings("all")
public final class InstrumentDecoder
{
    public static final int BLOCK_LENGTH = 39;
    public static final int TEMPLATE_ID = 2;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1.0.0";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final InstrumentDecoder parentMessage = this;
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

    public InstrumentDecoder wrap(
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

    public InstrumentDecoder wrapAndApplyHeader(
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

    public InstrumentDecoder sbeRewind()
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

        final int pos = offset + 8 + (index * 1);

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

        buffer.getBytes(offset + 8, dst, dstOffset, length);

        return length;
    }

    public String instrumentId()
    {
        final byte[] dst = new byte[8];
        buffer.getBytes(offset + 8, dst, 0, 8);

        int end = 0;
        for (; end < 8 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.US_ASCII);
    }


    public int getInstrumentId(final Appendable value)
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


    public static int strikeId()
    {
        return 3;
    }

    public static int strikeSinceVersion()
    {
        return 0;
    }

    public static int strikeEncodingOffset()
    {
        return 24;
    }

    public static int strikeEncodingLength()
    {
        return 9;
    }

    public static String strikeMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Decoder strike = new Int64_8Decoder();

    /**
     * Strike Price
     *
     * @return Int64_8Decoder : Strike Price
     */
    public Int64_8Decoder strike()
    {
        strike.wrap(buffer, offset + 24);
        return strike;
    }

    public static int expiryId()
    {
        return 4;
    }

    public static int expirySinceVersion()
    {
        return 0;
    }

    public static int expiryEncodingOffset()
    {
        return 33;
    }

    public static int expiryEncodingLength()
    {
        return 2;
    }

    public static String expiryMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static int expiryNullValue()
    {
        return 65535;
    }

    public static int expiryMinValue()
    {
        return 0;
    }

    public static int expiryMaxValue()
    {
        return 65534;
    }

    public int expiry()
    {
        return (buffer.getShort(offset + 33, BYTE_ORDER) & 0xFFFF);
    }


    public static int instrumentTypeId()
    {
        return 5;
    }

    public static int instrumentTypeSinceVersion()
    {
        return 0;
    }

    public static int instrumentTypeEncodingOffset()
    {
        return 35;
    }

    public static int instrumentTypeEncodingLength()
    {
        return 4;
    }

    public static String instrumentTypeMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public int instrumentTypeRaw()
    {
        return buffer.getInt(offset + 35, BYTE_ORDER);
    }

    public InstrumentType instrumentType()
    {
        return InstrumentType.get(buffer.getInt(offset + 35, BYTE_ORDER));
    }


    public String toString()
    {
        if (null == buffer)
        {
            return "";
        }

        final InstrumentDecoder decoder = new InstrumentDecoder();
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
        builder.append("[Instrument](sbeTemplateId=");
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
        builder.append("instrumentId=");
        for (int i = 0; i < instrumentIdLength() && this.instrumentId(i) > 0; i++)
        {
            builder.append((char)this.instrumentId(i));
        }
        builder.append('|');
        builder.append("underlyingId=");
        for (int i = 0; i < underlyingIdLength() && this.underlyingId(i) > 0; i++)
        {
            builder.append((char)this.underlyingId(i));
        }
        builder.append('|');
        builder.append("strike=");
        final Int64_8Decoder strike = this.strike();
        if (null != strike)
        {
            strike.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("expiry=");
        builder.append(this.expiry());
        builder.append('|');
        builder.append("instrumentType=");
        builder.append(this.instrumentType());

        limit(originalLimit);

        return builder;
    }
    
    public InstrumentDecoder sbeSkip()
    {
        sbeRewind();

        return this;
    }
}
