package com.ac.common.interview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class SubstringConcatenationTest {
    public static List<Integer> findSubstring(String s, String[] words) {
        List<Integer> result = new ArrayList<>();
        if (s == null || words.length == 0) return result;

        int wordLength = words[0].length();
        int totalWords = words.length;
        int windowSize = wordLength * totalWords;

        Map<String, Integer> wordCount = new HashMap<>();
        for (String word : words) {
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
        }

        for (int i = 0; i < wordLength; i++) {
            Map<String, Integer> seen = new HashMap<>();
            int left = i, right = i, count = 0;

            while (right + wordLength <= s.length()) {
                String word = s.substring(right, right + wordLength);
                right += wordLength;

                if (wordCount.containsKey(word)) {
                    seen.put(word, seen.getOrDefault(word, 0) + 1);
                    count++;

                    while (seen.get(word) > wordCount.get(word)) {
                        String leftWord = s.substring(left, left + wordLength);
                        seen.put(leftWord, seen.get(leftWord) - 1);
                        left += wordLength;
                        count--;
                    }

                    if (count == totalWords) result.add(left);
                } else {
                    seen.clear();
                    count = 0;
                    left = right;
                }
            }
        }

        return result;
    }

    @Test
    public void test() {
        String s = "barfoofoobarthefoobarman";
        String[] words = {"bar","foo","the"};
        // String s = "barfoothefoobarman";
        // String[] words = {"foo", "bar"};
        System.out.println("Starting indices: " + findSubstring(s, words));
    }
}

