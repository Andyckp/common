package subscribable;

import java.util.Map;
import java.util.Objects;
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

    public void subscribe(MessageListener<K, V> listener, ControlListener controlListener) {
        Objects.nonNull(listener);
        listeners.add(listener);
        if (executor == null) {
            sendSnapshot(listener, controlListener);
        } else {
            executor.execute(() -> {
                sendSnapshot(listener, controlListener);
            });
        }
    }

    private void sendSnapshot(MessageListener<K, V> listener, ControlListener controlListener) {
        for (Map.Entry<K, Message<K, V>> e : cache.entrySet()) {
            listener.onMessage(e.getValue());
        }

        if (controlListener != null) {
            controlListener.onSnapshotEnd();
        }
    }

    public void unsubscribe(MessageListener<K, V> listener) {
        Objects.nonNull(listener);
        listeners.remove(listener);
    }

    public void onMessage(Message<K, V> message) {
        cache.put(message.getKey(), message);
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
