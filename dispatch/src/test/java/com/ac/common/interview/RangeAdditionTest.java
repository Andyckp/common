package com.ac.common.interview;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class RangeAdditionTest {
    public static int[] getModifiedArray(int length, int[][] updates) {
        int[] result = new int[length];

        // Apply range updates using difference array
        for (int[] update : updates) {
            int start = update[0], end = update[1], inc = update[2];
            result[start] += inc;
            if (end + 1 < length) result[end + 1] -= inc;
        }

        // Compute prefix sum
        for (int i = 1; i < length; i++) {
            result[i] += result[i - 1];
        }

        return result;
    }

    @Test
    public void test() {
        int[][] updates = {{1,3,2}, {2,4,3}, {0,2,-2}};
        int length = 5;

        System.out.println(Arrays.toString(getModifiedArray(length, updates)));
    }
}

