package com.ac.derivativepricer.process;

import java.time.LocalDate;

public final class LocalDateCodec {

    private static final LocalDate EPOCH = LocalDate.of(1970, 1, 1);

    private LocalDateCodec() {
        // Utility class; prevent instantiation
    }

    public static LocalDate decodeFromUint16(int daysSinceEpoch) {
        // Treat the unsigned short as 0–65535 range
        return EPOCH.plusDays(daysSinceEpoch & 0xFFFF);
    }

    public static int encodeToUint16(LocalDate date) {
        int days = (int) EPOCH.until(date).getDays();
        if (days < 0 || days > 65535) {
            throw new IllegalArgumentException("Date out of range for uint16: " + date);
        }
        return days;
    }
}

