package com.ac.subscribable;

public interface Subscribeable<K, V> {
    void subscribe(MessageListener<K, V> listener);
    void unsubscribe(MessageListener<K, V> listener);
}
