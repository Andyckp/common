/* Generated SBE (Simple Binary Encoding) message codec. */
package com.ac.derivativepricer.codec;

import org.agrona.MutableDirectBuffer;

@SuppressWarnings("all")
public final class InstrumentEncoder
{
    public static final int BLOCK_LENGTH = 39;
    public static final int TEMPLATE_ID = 2;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1.0.0";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final InstrumentEncoder parentMessage = this;
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

    public InstrumentEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);

        return this;
    }

    public InstrumentEncoder wrapAndApplyHeader(
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


    public InstrumentEncoder instrumentId(final int index, final byte value)
    {
        if (index < 0 || index >= 8)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 8 + (index * 1);
        buffer.putByte(pos, value);

        return this;
    }

    public static String instrumentIdCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public InstrumentEncoder putInstrumentId(final byte[] src, final int srcOffset)
    {
        final int length = 8;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + srcOffset);
        }

        buffer.putBytes(offset + 8, src, srcOffset, length);

        return this;
    }

    public InstrumentEncoder instrumentId(final String src)
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

    public InstrumentEncoder instrumentId(final CharSequence src)
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


    public InstrumentEncoder underlyingId(final int index, final byte value)
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

    public InstrumentEncoder putUnderlyingId(final byte[] src, final int srcOffset)
    {
        final int length = 8;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + srcOffset);
        }

        buffer.putBytes(offset + 16, src, srcOffset, length);

        return this;
    }

    public InstrumentEncoder underlyingId(final String src)
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

    public InstrumentEncoder underlyingId(final CharSequence src)
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

    private final Int64_8Encoder strike = new Int64_8Encoder();

    /**
     * Strike Price
     *
     * @return Int64_8Encoder : Strike Price
     */
    public Int64_8Encoder strike()
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

    public InstrumentEncoder expiry(final int value)
    {
        buffer.putShort(offset + 33, (short)value, BYTE_ORDER);
        return this;
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

    public InstrumentEncoder instrumentType(final InstrumentType value)
    {
        buffer.putInt(offset + 35, value.value(), BYTE_ORDER);
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

        final InstrumentDecoder decoder = new InstrumentDecoder();
        decoder.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return decoder.appendTo(builder);
    }
}
