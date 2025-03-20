package com.ac.subscribable;

public interface MessageListener<K, V> {
    void onMessage(Message<K, V> message);
}
