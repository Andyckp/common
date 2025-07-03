/* Generated SBE (Simple Binary Encoding) message codec. */
package com.ac.derivativepricer.codec;

import org.agrona.DirectBuffer;

@SuppressWarnings("all")
public final class ExpiryVolatilityDecoder
{
    public static final int BLOCK_LENGTH = 171;
    public static final int TEMPLATE_ID = 4;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1.0.0";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final ExpiryVolatilityDecoder parentMessage = this;
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

    public ExpiryVolatilityDecoder wrap(
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

    public ExpiryVolatilityDecoder wrapAndApplyHeader(
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

    public ExpiryVolatilityDecoder sbeRewind()
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


    public byte volatilityId(final int index)
    {
        if (index < 0 || index >= 8)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 8 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String volatilityIdCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public int getVolatilityId(final byte[] dst, final int dstOffset)
    {
        final int length = 8;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + dstOffset);
        }

        buffer.getBytes(offset + 8, dst, dstOffset, length);

        return length;
    }

    public String volatilityId()
    {
        final byte[] dst = new byte[8];
        buffer.getBytes(offset + 8, dst, 0, 8);

        int end = 0;
        for (; end < 8 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.US_ASCII);
    }


    public int getVolatilityId(final Appendable value)
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

    public int expiry()
    {
        return (buffer.getShort(offset + 16, BYTE_ORDER) & 0xFFFF);
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

    private final Int64_8Decoder fittingReference = new Int64_8Decoder();

    /**
     * Reference point for curve fitting
     *
     * @return Int64_8Decoder : Reference point for curve fitting
     */
    public Int64_8Decoder fittingReference()
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

    private final Int64_8Decoder parameter1 = new Int64_8Decoder();

    public Int64_8Decoder parameter1()
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

    private final Int64_8Decoder parameter2 = new Int64_8Decoder();

    public Int64_8Decoder parameter2()
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

    private final Int64_8Decoder parameter3 = new Int64_8Decoder();

    public Int64_8Decoder parameter3()
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

    private final Int64_8Decoder parameter4 = new Int64_8Decoder();

    public Int64_8Decoder parameter4()
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

    private final Int64_8Decoder parameter5 = new Int64_8Decoder();

    public Int64_8Decoder parameter5()
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

    private final Int64_8Decoder parameter6 = new Int64_8Decoder();

    public Int64_8Decoder parameter6()
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

    private final Int64_8Decoder parameter7 = new Int64_8Decoder();

    public Int64_8Decoder parameter7()
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

    private final Int64_8Decoder parameter8 = new Int64_8Decoder();

    public Int64_8Decoder parameter8()
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

    private final Int64_8Decoder parameter9 = new Int64_8Decoder();

    public Int64_8Decoder parameter9()
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

    private final Int64_8Decoder parameter10 = new Int64_8Decoder();

    public Int64_8Decoder parameter10()
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

    private final Int64_8Decoder parameter11 = new Int64_8Decoder();

    public Int64_8Decoder parameter11()
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

    private final Int64_8Decoder parameter12 = new Int64_8Decoder();

    public Int64_8Decoder parameter12()
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

    private final Int64_8Decoder parameter13 = new Int64_8Decoder();

    public Int64_8Decoder parameter13()
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

    private final Int64_8Decoder parameter14 = new Int64_8Decoder();

    public Int64_8Decoder parameter14()
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

    private final Int64_8Decoder parameter15 = new Int64_8Decoder();

    public Int64_8Decoder parameter15()
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

    private final Int64_8Decoder parameter16 = new Int64_8Decoder();

    public Int64_8Decoder parameter16()
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

        final ExpiryVolatilityDecoder decoder = new ExpiryVolatilityDecoder();
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
        builder.append("[ExpiryVolatility](sbeTemplateId=");
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
        builder.append("volatilityId=");
        for (int i = 0; i < volatilityIdLength() && this.volatilityId(i) > 0; i++)
        {
            builder.append((char)this.volatilityId(i));
        }
        builder.append('|');
        builder.append("expiry=");
        builder.append(this.expiry());
        builder.append('|');
        builder.append("fittingReference=");
        final Int64_8Decoder fittingReference = this.fittingReference();
        if (null != fittingReference)
        {
            fittingReference.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("parameter1=");
        final Int64_8Decoder parameter1 = this.parameter1();
        if (null != parameter1)
        {
            parameter1.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("parameter2=");
        final Int64_8Decoder parameter2 = this.parameter2();
        if (null != parameter2)
        {
            parameter2.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("parameter3=");
        final Int64_8Decoder parameter3 = this.parameter3();
        if (null != parameter3)
        {
            parameter3.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("parameter4=");
        final Int64_8Decoder parameter4 = this.parameter4();
        if (null != parameter4)
        {
            parameter4.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("parameter5=");
        final Int64_8Decoder parameter5 = this.parameter5();
        if (null != parameter5)
        {
            parameter5.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("parameter6=");
        final Int64_8Decoder parameter6 = this.parameter6();
        if (null != parameter6)
        {
            parameter6.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("parameter7=");
        final Int64_8Decoder parameter7 = this.parameter7();
        if (null != parameter7)
        {
            parameter7.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("parameter8=");
        final Int64_8Decoder parameter8 = this.parameter8();
        if (null != parameter8)
        {
            parameter8.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("parameter9=");
        final Int64_8Decoder parameter9 = this.parameter9();
        if (null != parameter9)
        {
            parameter9.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("parameter10=");
        final Int64_8Decoder parameter10 = this.parameter10();
        if (null != parameter10)
        {
            parameter10.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("parameter11=");
        final Int64_8Decoder parameter11 = this.parameter11();
        if (null != parameter11)
        {
            parameter11.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("parameter12=");
        final Int64_8Decoder parameter12 = this.parameter12();
        if (null != parameter12)
        {
            parameter12.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("parameter13=");
        final Int64_8Decoder parameter13 = this.parameter13();
        if (null != parameter13)
        {
            parameter13.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("parameter14=");
        final Int64_8Decoder parameter14 = this.parameter14();
        if (null != parameter14)
        {
            parameter14.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("parameter15=");
        final Int64_8Decoder parameter15 = this.parameter15();
        if (null != parameter15)
        {
            parameter15.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("parameter16=");
        final Int64_8Decoder parameter16 = this.parameter16();
        if (null != parameter16)
        {
            parameter16.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }

        limit(originalLimit);

        return builder;
    }
    
    public ExpiryVolatilityDecoder sbeSkip()
    {
        sbeRewind();

        return this;
    }
}
