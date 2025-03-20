package com.ac.common.subscribeable;

public interface Subscribable<K, V> {
    void subscribe(MessageListener<K, V> listener, ControlListener controlListener);
    void unsubscribe(MessageListener<K, V> listener);
}
