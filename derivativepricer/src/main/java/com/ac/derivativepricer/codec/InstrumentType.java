/* Generated SBE (Simple Binary Encoding) message codec. */
package com.ac.derivativepricer.codec;

@SuppressWarnings("all")
public enum InstrumentType
{
    STOCK(0),

    EUROPEAN_CALL(1),

    EUROPEAN_PUT(2),

    AMERICAN_CALL(3),

    AMERICAN_PUT(4),

    ASIAN_CALL(5),

    ASIAN_PUT(6),

    /**
     * To be used to represent not present or null.
     */
    NULL_VAL(-2147483648);

    private final int value;

    InstrumentType(final int value)
    {
        this.value = value;
    }

    /**
     * The raw encoded value in the Java type representation.
     *
     * @return the raw value encoded.
     */
    public int value()
    {
        return value;
    }

    /**
     * Lookup the enum value representing the value.
     *
     * @param value encoded to be looked up.
     * @return the enum value representing the value.
     */
    public static InstrumentType get(final int value)
    {
        switch (value)
        {
            case 0: return STOCK;
            case 1: return EUROPEAN_CALL;
            case 2: return EUROPEAN_PUT;
            case 3: return AMERICAN_CALL;
            case 4: return AMERICAN_PUT;
            case 5: return ASIAN_CALL;
            case 6: return ASIAN_PUT;
            case -2147483648: return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
