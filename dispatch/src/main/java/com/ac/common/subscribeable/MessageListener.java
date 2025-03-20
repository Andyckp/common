package com.ac.common.subscribeable;

import com.ac.common.Message;

public interface MessageListener<K, V> {
    void onMessage(Message<K, V> message);
}
