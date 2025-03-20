package subscribable;

public interface Subscribeable<K, V> {
    void subscribe(MessageListener<K, V> listener, ControlListener controlListener);
    void unsubscribe(MessageListener<K, V> listener);
}
