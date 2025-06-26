package com.ac.common.interview;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class CharCircularBufferTest {

    @Test
    public void testBasicAddAndRemove() {
        CharCircularBuffer buffer = new CharCircularBuffer();

        assertEquals('\0', buffer.add(1, 'A'));
        assertEquals('\0', buffer.add(2, 'B'));
        assertEquals('\0', buffer.add(3, 'C'));
        assertEquals('\0', buffer.add(4, 'D'));

        assertEquals('A', buffer.remove(1));
        assertEquals('B', buffer.remove(2));
        assertEquals('C', buffer.remove(3));
        assertEquals('D', buffer.remove(4));

        assertThrows(IllegalStateException.class, () -> buffer.remove(1));
    }

    @Test
    public void testWrapAround() {
        CharCircularBuffer buffer = new CharCircularBuffer();

        // Fill the buffer completely
        buffer.add(1, 'A');
        buffer.add(2, 'B');
        buffer.add(3, 'C');
        buffer.add(4, 'D');

        // Expect exception when trying to add beyond buffer capacity
        assertThrows(IllegalArgumentException.class, () -> buffer.add(5, 'E'));

        // Remove an element to make space
        assertEquals('A', buffer.remove(1));

        // Now adding should succeed
        assertEquals('\0', buffer.add(5, 'E'));

        // Expect exception again when trying to add beyond buffer capacity
        assertThrows(IllegalArgumentException.class, () -> buffer.add(6, 'F'));

        // Remove another element
        assertEquals('B', buffer.remove(2));

        // Now adding should succeed
        assertEquals('\0', buffer.add(6, 'F'));

        // Continue removing and adding while checking exceptions
        assertEquals('C', buffer.remove(3));
        assertEquals('\0', buffer.add(7, 'G'));

        assertEquals('D', buffer.remove(4));
        assertEquals('\0', buffer.add(8, 'H'));

        // Validate final removals
        assertEquals('E', buffer.remove(5));
        assertEquals('F', buffer.remove(6));
        assertEquals('G', buffer.remove(7));
        assertEquals('H', buffer.remove(8));

        // Expect exception when trying to remove from an empty buffer
        assertThrows(IllegalStateException.class, () -> buffer.remove(1));
    }


    @Test
    public void testMinMaxSequenceUpdates() {
        CharCircularBuffer buffer = new CharCircularBuffer();

        buffer.add(10, 'X');
        buffer.add(11, 'Y');
        buffer.add(12, 'Z');

        assertEquals('X', buffer.remove(10));
        assertEquals('Y', buffer.remove(11));

        buffer.add(13, 'A');
        buffer.add(14, 'B');

        assertEquals('Z', buffer.remove(12));
        assertEquals('A', buffer.remove(13));
        assertEquals('B', buffer.remove(14));

        assertThrows(IllegalStateException.class, () -> buffer.remove(10));
    }

    @Test
    public void testInvalidSequenceHandling() {
        CharCircularBuffer buffer = new CharCircularBuffer();

        buffer.add(1, 'A');
        buffer.add(2, 'B');

        assertThrows(IllegalArgumentException.class, () -> buffer.add(10, 'X')); // Too big
        assertThrows(IllegalArgumentException.class, () -> buffer.add(-5, 'Y')); // Too small

        assertThrows(IllegalArgumentException.class, () -> buffer.remove(10)); // Out of range
        assertThrows(IllegalArgumentException.class, () -> buffer.remove(-1)); // Out of range
    }

    @Test
    public void testOverwriteExistingValue() {
        CharCircularBuffer buffer = new CharCircularBuffer();

        buffer.add(1, 'A');
        buffer.add(2, 'B');
        buffer.add(3, 'C');

        assertEquals('B', buffer.add(2, 'X')); // Overwriting existing value
        assertEquals('X', buffer.remove(2)); // Should return new value

        assertEquals('A', buffer.remove(1));
        assertEquals('C', buffer.remove(3));
    }
}

