package com.ac.common.exchange2;

import java.util.Arrays;

public class Util {
    public static char[] padOrTruncate(String input, int length) {
        char[] result = new char[length];
        Arrays.fill(result, ' '); 
        
        for (int i = 0; i < Math.min(input.length(), length); i++) {
            result[i] = input.charAt(i);
        }
        return result;
    }
}
