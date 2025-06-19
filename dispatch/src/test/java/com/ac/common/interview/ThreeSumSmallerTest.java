package com.ac.common.interview;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class ThreeSumSmallerTest {
    public static int threeSumSmaller(int[] nums, int target) {
        Arrays.sort(nums); // Step 1: Sort the array
        int count = 0;

        for (int i = 0; i < nums.length - 2; i++) {
            int j = i + 1, k = nums.length - 1;

            while (j < k) {
                if (nums[i] + nums[j] + nums[k] < target) {
                    count += (k - j); // All pairs (j, j+1, ..., k) are valid
                    j++; // Move j forward
                } else {
                    k--; // Move k backward
                }
            }
        }

        return count;
    }

    @Test
    public void test() {
        int[] nums = {-2, 0, 1, 3};
        int target = 2;
        System.out.println("Number of valid triplets: " + threeSumSmaller(nums, target));
    }
}

