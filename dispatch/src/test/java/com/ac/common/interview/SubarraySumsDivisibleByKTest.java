package com.ac.common.interview;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

public class SubarraySumsDivisibleByKTest {
    public static int subarraysDivByK(int[] nums, int k) {
        HashMap<Integer, Integer> remainderCount = new HashMap<>();
        remainderCount.put(0, 1); // To count subarrays starting from index 0

        int prefixSum = 0, count = 0;

        for (int num : nums) {
            prefixSum += num;
            int remainder = ((prefixSum % k) + k) % k; // Ensure non-negative remainder

            // Add previous occurrences of the remainder (valid subarrays)
            count += remainderCount.getOrDefault(remainder, 0);

            // Store the remainder count
            remainderCount.put(remainder, remainderCount.getOrDefault(remainder, 0) + 1);
        }

        return count;
    }

    @Test
    public void test() {
        int[] nums = {4, 5, 0, -2, -3, 1};
        int k = 5;

        System.out.println("Number of subarrays divisible by " + k + ": " + subarraysDivByK(nums, k));
    }
}
