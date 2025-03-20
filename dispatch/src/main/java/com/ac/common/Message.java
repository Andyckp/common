package com.ac.common;

public interface Message<K, V> {
    K getKey();
    V getValue();
}
