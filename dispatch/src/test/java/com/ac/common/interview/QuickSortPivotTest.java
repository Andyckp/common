package com.ac.common.interview;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class QuickSortPivotTest {
    // Function to partition the array using the pivot
    public static int partition(int[] arr, int low, int high) {
        int pivot = arr[high]; // Choosing last element as pivot
        int i = low - 1; // Pointer for greater element

        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) { // Move elements less than pivot to the left
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high); // Move pivot to correct position
        return i + 1; // Return pivot index
    }

    // Swap function
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // QuickSort function
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            System.out.println("Pivot selected: " + arr[pivotIndex]);
            System.out.println("Array after partitioning: " + Arrays.toString(arr));

            // Recursively sort subarrays
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    // Main method to test quicksort pivot
    @Test
    public void test() {
        int[] testArray = {10, 7, 8, 9, 1, 5};
        System.out.println("Original Array: " + Arrays.toString(testArray));
        
        quickSort(testArray, 0, testArray.length - 1);

        System.out.println("Sorted Array: " + Arrays.toString(testArray));
    }
}
