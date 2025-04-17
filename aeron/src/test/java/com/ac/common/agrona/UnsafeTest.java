package com.ac.common.agrona;

import org.agrona.concurrent.UnsafeBuffer;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeTest {
    @Test
    public void test() throws Exception {
        // Access Unsafe instance via reflection
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);

        // Allocate off-heap memory manually
        long address = unsafe.allocateMemory(1024 * 1024); // 1 MB

        // Wrap the allocated memory with UnsafeBuffer
        UnsafeBuffer buffer = new UnsafeBuffer(address, 1024 * 1024);

        // Write data to the buffer
        buffer.putInt(0, 12345);

        // Read the data from buffer
        int value = buffer.getInt(0);
        System.out.println("Value: " + value); // Output: 12345

        // Free memory manually (VERY IMPORTANT to avoid leaks)
        unsafe.freeMemory(address);
    }
}
