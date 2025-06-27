/* Generated SBE (Simple Binary Encoding) message codec. */
package com.ac.derivativepricer.codec;

import org.agrona.MutableDirectBuffer;

@SuppressWarnings("all")
public final class ExpiryVolatilityEncoder
{
    public static final int BLOCK_LENGTH = 171;
    public static final int TEMPLATE_ID = 4;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1.0.0";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final ExpiryVolatilityEncoder parentMessage = this;
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

    public ExpiryVolatilityEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);

        return this;
    }

    public ExpiryVolatilityEncoder wrapAndApplyHeader(
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

    public static int volatilityIdId()
    {
        return 1;
    }

    public static int volatilityIdSinceVersion()
    {
        return 0;
    }

    public static int volatilityIdEncodingOffset()
    {
        return 8;
    }

    public static int volatilityIdEncodingLength()
    {
        return 8;
    }

    public static String volatilityIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte volatilityIdNullValue()
    {
        return (byte)0;
    }

    public static byte volatilityIdMinValue()
    {
        return (byte)32;
    }

    public static byte volatilityIdMaxValue()
    {
        return (byte)126;
    }

    public static int volatilityIdLength()
    {
        return 8;
    }


    public ExpiryVolatilityEncoder volatilityId(final int index, final byte value)
    {
        if (index < 0 || index >= 8)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 8 + (index * 1);
        buffer.putByte(pos, value);

        return this;
    }

    public static String volatilityIdCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public ExpiryVolatilityEncoder putVolatilityId(final byte[] src, final int srcOffset)
    {
        final int length = 8;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + srcOffset);
        }

        buffer.putBytes(offset + 8, src, srcOffset, length);

        return this;
    }

    public ExpiryVolatilityEncoder volatilityId(final String src)
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

    public ExpiryVolatilityEncoder volatilityId(final CharSequence src)
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

    public static int expiryId()
    {
        return 2;
    }

    public static int expirySinceVersion()
    {
        return 0;
    }

    public static int expiryEncodingOffset()
    {
        return 16;
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

    public ExpiryVolatilityEncoder expiry(final int value)
    {
        buffer.putShort(offset + 16, (short)value, BYTE_ORDER);
        return this;
    }


    public static int fittingReferenceId()
    {
        return 3;
    }

    public static int fittingReferenceSinceVersion()
    {
        return 0;
    }

    public static int fittingReferenceEncodingOffset()
    {
        return 18;
    }

    public static int fittingReferenceEncodingLength()
    {
        return 9;
    }

    public static String fittingReferenceMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder fittingReference = new Int64_8Encoder();

    /**
     * Reference point for curve fitting
     *
     * @return Int64_8Encoder : Reference point for curve fitting
     */
    public Int64_8Encoder fittingReference()
    {
        fittingReference.wrap(buffer, offset + 18);
        return fittingReference;
    }

    public static int parameter1Id()
    {
        return 4;
    }

    public static int parameter1SinceVersion()
    {
        return 0;
    }

    public static int parameter1EncodingOffset()
    {
        return 27;
    }

    public static int parameter1EncodingLength()
    {
        return 9;
    }

    public static String parameter1MetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder parameter1 = new Int64_8Encoder();

    public Int64_8Encoder parameter1()
    {
        parameter1.wrap(buffer, offset + 27);
        return parameter1;
    }

    public static int parameter2Id()
    {
        return 5;
    }

    public static int parameter2SinceVersion()
    {
        return 0;
    }

    public static int parameter2EncodingOffset()
    {
        return 36;
    }

    public static int parameter2EncodingLength()
    {
        return 9;
    }

    public static String parameter2MetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder parameter2 = new Int64_8Encoder();

    public Int64_8Encoder parameter2()
    {
        parameter2.wrap(buffer, offset + 36);
        return parameter2;
    }

    public static int parameter3Id()
    {
        return 6;
    }

    public static int parameter3SinceVersion()
    {
        return 0;
    }

    public static int parameter3EncodingOffset()
    {
        return 45;
    }

    public static int parameter3EncodingLength()
    {
        return 9;
    }

    public static String parameter3MetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder parameter3 = new Int64_8Encoder();

    public Int64_8Encoder parameter3()
    {
        parameter3.wrap(buffer, offset + 45);
        return parameter3;
    }

    public static int parameter4Id()
    {
        return 7;
    }

    public static int parameter4SinceVersion()
    {
        return 0;
    }

    public static int parameter4EncodingOffset()
    {
        return 54;
    }

    public static int parameter4EncodingLength()
    {
        return 9;
    }

    public static String parameter4MetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder parameter4 = new Int64_8Encoder();

    public Int64_8Encoder parameter4()
    {
        parameter4.wrap(buffer, offset + 54);
        return parameter4;
    }

    public static int parameter5Id()
    {
        return 8;
    }

    public static int parameter5SinceVersion()
    {
        return 0;
    }

    public static int parameter5EncodingOffset()
    {
        return 63;
    }

    public static int parameter5EncodingLength()
    {
        return 9;
    }

    public static String parameter5MetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder parameter5 = new Int64_8Encoder();

    public Int64_8Encoder parameter5()
    {
        parameter5.wrap(buffer, offset + 63);
        return parameter5;
    }

    public static int parameter6Id()
    {
        return 9;
    }

    public static int parameter6SinceVersion()
    {
        return 0;
    }

    public static int parameter6EncodingOffset()
    {
        return 72;
    }

    public static int parameter6EncodingLength()
    {
        return 9;
    }

    public static String parameter6MetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder parameter6 = new Int64_8Encoder();

    public Int64_8Encoder parameter6()
    {
        parameter6.wrap(buffer, offset + 72);
        return parameter6;
    }

    public static int parameter7Id()
    {
        return 10;
    }

    public static int parameter7SinceVersion()
    {
        return 0;
    }

    public static int parameter7EncodingOffset()
    {
        return 81;
    }

    public static int parameter7EncodingLength()
    {
        return 9;
    }

    public static String parameter7MetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder parameter7 = new Int64_8Encoder();

    public Int64_8Encoder parameter7()
    {
        parameter7.wrap(buffer, offset + 81);
        return parameter7;
    }

    public static int parameter8Id()
    {
        return 11;
    }

    public static int parameter8SinceVersion()
    {
        return 0;
    }

    public static int parameter8EncodingOffset()
    {
        return 90;
    }

    public static int parameter8EncodingLength()
    {
        return 9;
    }

    public static String parameter8MetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder parameter8 = new Int64_8Encoder();

    public Int64_8Encoder parameter8()
    {
        parameter8.wrap(buffer, offset + 90);
        return parameter8;
    }

    public static int parameter9Id()
    {
        return 12;
    }

    public static int parameter9SinceVersion()
    {
        return 0;
    }

    public static int parameter9EncodingOffset()
    {
        return 99;
    }

    public static int parameter9EncodingLength()
    {
        return 9;
    }

    public static String parameter9MetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder parameter9 = new Int64_8Encoder();

    public Int64_8Encoder parameter9()
    {
        parameter9.wrap(buffer, offset + 99);
        return parameter9;
    }

    public static int parameter10Id()
    {
        return 13;
    }

    public static int parameter10SinceVersion()
    {
        return 0;
    }

    public static int parameter10EncodingOffset()
    {
        return 108;
    }

    public static int parameter10EncodingLength()
    {
        return 9;
    }

    public static String parameter10MetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder parameter10 = new Int64_8Encoder();

    public Int64_8Encoder parameter10()
    {
        parameter10.wrap(buffer, offset + 108);
        return parameter10;
    }

    public static int parameter11Id()
    {
        return 14;
    }

    public static int parameter11SinceVersion()
    {
        return 0;
    }

    public static int parameter11EncodingOffset()
    {
        return 117;
    }

    public static int parameter11EncodingLength()
    {
        return 9;
    }

    public static String parameter11MetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder parameter11 = new Int64_8Encoder();

    public Int64_8Encoder parameter11()
    {
        parameter11.wrap(buffer, offset + 117);
        return parameter11;
    }

    public static int parameter12Id()
    {
        return 15;
    }

    public static int parameter12SinceVersion()
    {
        return 0;
    }

    public static int parameter12EncodingOffset()
    {
        return 126;
    }

    public static int parameter12EncodingLength()
    {
        return 9;
    }

    public static String parameter12MetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder parameter12 = new Int64_8Encoder();

    public Int64_8Encoder parameter12()
    {
        parameter12.wrap(buffer, offset + 126);
        return parameter12;
    }

    public static int parameter13Id()
    {
        return 16;
    }

    public static int parameter13SinceVersion()
    {
        return 0;
    }

    public static int parameter13EncodingOffset()
    {
        return 135;
    }

    public static int parameter13EncodingLength()
    {
        return 9;
    }

    public static String parameter13MetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder parameter13 = new Int64_8Encoder();

    public Int64_8Encoder parameter13()
    {
        parameter13.wrap(buffer, offset + 135);
        return parameter13;
    }

    public static int parameter14Id()
    {
        return 17;
    }

    public static int parameter14SinceVersion()
    {
        return 0;
    }

    public static int parameter14EncodingOffset()
    {
        return 144;
    }

    public static int parameter14EncodingLength()
    {
        return 9;
    }

    public static String parameter14MetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder parameter14 = new Int64_8Encoder();

    public Int64_8Encoder parameter14()
    {
        parameter14.wrap(buffer, offset + 144);
        return parameter14;
    }

    public static int parameter15Id()
    {
        return 18;
    }

    public static int parameter15SinceVersion()
    {
        return 0;
    }

    public static int parameter15EncodingOffset()
    {
        return 153;
    }

    public static int parameter15EncodingLength()
    {
        return 9;
    }

    public static String parameter15MetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder parameter15 = new Int64_8Encoder();

    public Int64_8Encoder parameter15()
    {
        parameter15.wrap(buffer, offset + 153);
        return parameter15;
    }

    public static int parameter16Id()
    {
        return 19;
    }

    public static int parameter16SinceVersion()
    {
        return 0;
    }

    public static int parameter16EncodingOffset()
    {
        return 162;
    }

    public static int parameter16EncodingLength()
    {
        return 9;
    }

    public static String parameter16MetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final Int64_8Encoder parameter16 = new Int64_8Encoder();

    public Int64_8Encoder parameter16()
    {
        parameter16.wrap(buffer, offset + 162);
        return parameter16;
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

        final ExpiryVolatilityDecoder decoder = new ExpiryVolatilityDecoder();
        decoder.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return decoder.appendTo(builder);
    }
}
