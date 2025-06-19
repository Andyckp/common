package com.ac.common.interview;

import org.junit.jupiter.api.Test;

public class TrappingRainWaterTest {
    public static int trap(int[] height) {
        if (height.length == 0) return 0;

        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0, water = 0;

        while (left < right) {
            System.out.println("Current state -> Left: " + left + " (" + height[left] + "), Right: " + right + " (" + height[right] + ")");
            System.out.println("LeftMax: " + leftMax + ", RightMax: " + rightMax);

            if (height[left] < height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                    System.out.println("Updating leftMax to: " + leftMax);
                } else {
                    water += leftMax - height[left];
                    System.out.println("Water trapped at index " + left + ": " + (leftMax - height[left]));
                }
                left++;
            } else {
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                    System.out.println("Updating rightMax to: " + rightMax);
                } else {
                    water += rightMax - height[right];
                    System.out.println("Water trapped at index " + right + ": " + (rightMax - height[right]));
                }
                right--;
            }
            System.out.println("Total water so far: " + water);
            System.out.println("--------------------------------");
        }

        return water;
    }

    @Test
    public void test() {
        int[] height = {0,1,0,2,1,0,1,3,2,1,2,1};
        System.out.println("Trapped Water: " + trap(height));
    }
}

