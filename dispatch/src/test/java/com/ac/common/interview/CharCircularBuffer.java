package com.ac.common.interview;

public class CharCircularBuffer {
    private static final int BUFFER_SIZE = 4;
    private final char[] buffer = new char[BUFFER_SIZE];
    private int minSeq = Integer.MAX_VALUE;
    private int maxSeq = Integer.MIN_VALUE;

    public CharCircularBuffer() {
        for (int i = 0; i < BUFFER_SIZE; i++) {
            buffer[i] = '\0';
        }
    }

    public char add(int seq, char c) {
        if (minSeq == Integer.MAX_VALUE || maxSeq == Integer.MIN_VALUE) {
            buffer[seq % BUFFER_SIZE] = c;
            minSeq = seq;
            maxSeq = seq;
            return '\0';
        }

        if (seq < minSeq) {
            if (maxSeq - seq >= BUFFER_SIZE) {
                throw new IllegalArgumentException("Seq too small: " + seq);
            }
            buffer[seq % BUFFER_SIZE] = c;
            minSeq = seq;
            return '\0';
        }

        if (seq > maxSeq) {
            if (seq - minSeq >= BUFFER_SIZE) {
                throw new IllegalArgumentException("Seq too big: " + seq);
            }
            buffer[seq % BUFFER_SIZE] = c;
            maxSeq = seq;
            return '\0';
        }

        char oc = buffer[seq % BUFFER_SIZE];
        buffer[seq % BUFFER_SIZE] = c;
        return oc;
    }

    public char remove(int seq) {
        if (minSeq == Integer.MAX_VALUE || maxSeq == Integer.MIN_VALUE) {
            throw new IllegalStateException("Seq not found: " + seq);
        }

        if (seq < minSeq || seq > maxSeq) {
            throw new IllegalArgumentException("Seq out of range: " + seq);
        }

        if (seq == minSeq && seq == maxSeq) {
            char c = buffer[seq % BUFFER_SIZE];
            buffer[seq % BUFFER_SIZE] = '\0';
            minSeq = Integer.MAX_VALUE;
            maxSeq = Integer.MIN_VALUE;
            return c;
        }

        if (seq == minSeq) {
            char c = buffer[seq % BUFFER_SIZE];
            buffer[seq % BUFFER_SIZE] = '\0';
            for (int i = minSeq + 1; i <= maxSeq; i++) {
                if (buffer[i % BUFFER_SIZE] != '\0') {
                    minSeq = i;
                    break;
                }
            }
            return c;
        }

        if (seq == maxSeq) {
            char c = buffer[seq % BUFFER_SIZE];
            buffer[seq % BUFFER_SIZE] = '\0';
            for (int i = maxSeq - 1; i >= minSeq; i--) {
                if (buffer[i % BUFFER_SIZE] != '\0') {
                    maxSeq = i;
                    break;
                }
            }
            return c;
        }

        char c = buffer[seq % BUFFER_SIZE];
        if (c == '\0') {
            throw new IllegalStateException("Value not found: " + seq);
        }
        
        buffer[seq % BUFFER_SIZE] = '\0';
        return c;
    }
}
