package com.ac.subscribable;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;

public class SubscribeableCache<K, V> implements Subscribeable<K, V>, MessageListener<K, V> {
    private final Map<K, Message<K, V>> cache = new ConcurrentHashMap<>();
    private final Set<MessageListener<K, V>> listeners = new CopyOnWriteArraySet<MessageListener<K, V>>();
    private final Executor executor;

    public SubscribeableCache() {
        this(null);
    }

    public SubscribeableCache(Executor executor) {
        this.executor = executor;
    }

    public void subscribe(MessageListener<K, V> listener) {
        listeners.add(listener);
        if (executor == null) {
            sendSnapshot(listener);
        } else {
            executor.execute(() -> {
                sendSnapshot(listener);
            });
        }
    }

    private void sendSnapshot(MessageListener<K, V> listener) {
        for (Map.Entry<K, Message<K, V>> e : cache.entrySet()) {
            listener.onMessage(e.getValue());
        }
    }

    public void unsubscribe(MessageListener<K, V> listener) {
        listeners.remove(listener);
    }

    public void onMessage(Message<K, V> message) {
        if (executor == null) {
            sendMessage(message);
        } else {
            executor.execute(() -> sendMessage(message));
        }
    }

    private void sendMessage(Message<K, V> message) {
        for (MessageListener<K, V> listener : listeners) {
            listener.onMessage(message);
        }
    }
}
