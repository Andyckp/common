package subscribable;

import java.util.Objects;

public class MessageImpl<K, V> implements Message<K, V> {
    private final K key;
    private final V value;
    public MessageImpl(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MessageImpl)) return false;
        MessageImpl<?, ?> message = (MessageImpl<?, ?>) o;
        return Objects.equals(key, message.key) && Objects.equals(value, message.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return "MessageImpl{" +
            "key=" + key +
            ", value=" + value +
            '}';
    }
}
