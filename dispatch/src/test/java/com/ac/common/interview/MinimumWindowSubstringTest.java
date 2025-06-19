package com.ac.common.interview;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

public class MinimumWindowSubstringTest {
    public static String minWindow(String s, String t) {
        if (s.length() < t.length()) return "";

        HashMap<Character, Integer> charToIndex = new HashMap<>();
        int uniqueChars = 0;

        // Assign unique index for each required character
        for (char c : t.toCharArray()) {
            if (!charToIndex.containsKey(c)) {
                charToIndex.put(c, uniqueChars++);
            }
        }

        int[] need = new int[uniqueChars];  // Track required frequencies
        int[] window = new int[uniqueChars]; // Track current window frequencies

        // Populate need array
        for (char c : t.toCharArray()) {
            need[charToIndex.get(c)]++;
        }

        int left = 0, right = 0, valid = 0;
        int minLen = Integer.MAX_VALUE, start = 0;

        while (right < s.length()) {
            char c = s.charAt(right);
            right++;

            // Expand window
            if (charToIndex.containsKey(c)) {
                int idx = charToIndex.get(c);
                window[idx]++;
                if (window[idx] == need[idx]) valid++;
            }

            // Shrink window while maintaining all required characters
            while (valid == uniqueChars) {
                if (right - left < minLen) {
                    minLen = right - left;
                    start = left;
                }

                char d = s.charAt(left);
                left++;

                if (charToIndex.containsKey(d)) {
                    int idx = charToIndex.get(d);
                    if (window[idx] == need[idx]) valid--;
                    window[idx]--;
                }
            }
        }

        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }

    @Test
    public void test() {
        String s = "ADOBECODEBANC";
        String t = "ABC";
        System.out.println("Minimum Window Substring: " + minWindow(s, t));
    }
}

