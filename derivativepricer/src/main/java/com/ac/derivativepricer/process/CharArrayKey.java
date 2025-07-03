package com.ac.derivativepricer.process;

import java.util.Arrays;

public class CharArrayKey {
    private final char[] charArray;

    public CharArrayKey(char[] key) {
        this.charArray = key.clone();
    }
    // private CharArrayKey(char[] key) {
    //     this.charArray = key;
    // }

    // public char[] getCharArray() {
    //     return charArray;
    // }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CharArrayKey)) return false;
        return Arrays.equals(this.charArray, ((CharArrayKey) obj).charArray);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(charArray);
    }

    // public static CharArrayKey copy(char[] key) {
    //     return new CharArrayKey(key.clone()); 
    // }

    // public static CharArrayKey wrap(char[] key) {
    //     return new CharArrayKey(key); 
    // }
}