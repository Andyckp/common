package subscribable;

public interface Message<K, V> {
    K getKey();
    V getValue();
}
