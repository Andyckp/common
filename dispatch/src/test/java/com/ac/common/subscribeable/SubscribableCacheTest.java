package com.ac.common.subscribeable;

import com.ac.common.MessageImpl;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@SuppressWarnings({"unchecked"})
public class SubscribableCacheTest {
    public static final int TIMEOUT_IN_MILLIS = 1000;

    @Test
    public void GIVEN_non_empty_cache_without_executor_WHEN_onMessage_THEN_receive_the_snapshot_and_message() {
        GIVEN_non_empty_cache_WHEN_onMessage_THEN_receive_the_snapshot_and_message(null);
    }

    @Test
    public void GIVEN_non_empty_cache_with_executor_WHEN_onMessage_THEN_receive_the_snapshot_and_message() {
        GIVEN_non_empty_cache_WHEN_onMessage_THEN_receive_the_snapshot_and_message(Executors.newSingleThreadExecutor());
    }

    private void GIVEN_non_empty_cache_WHEN_onMessage_THEN_receive_the_snapshot_and_message(Executor executor) {
        // Given
        SubscribableCache<String, String> cache = new SubscribableCache<>(executor);
        cache.onMessage(new MessageImpl<>("k1", "v1"));

        // When
        MessageListener<String, String> listener = mock(MessageListener.class);
        ControlListener controlListener = mock(ControlListener.class);
        cache.subscribe(listener, controlListener);

        // Then
        verify(listener, timeout(TIMEOUT_IN_MILLIS)).onMessage(new MessageImpl<>("k1", "v1"));
        verify(controlListener, timeout(TIMEOUT_IN_MILLIS)).onSnapshotEnd();

        // When
        reset(listener);
        cache.onMessage(new MessageImpl<>("k2", "v1"));
        cache.onMessage(new MessageImpl<>("k1", "v1"));

        // Then
        verify(listener, timeout(TIMEOUT_IN_MILLIS)).onMessage(new MessageImpl<>("k1", "v1"));
        verify(listener, timeout(TIMEOUT_IN_MILLIS)).onMessage(new MessageImpl<>("k2", "v1"));

        // When
        MessageListener<String, String> listener2 = mock(MessageListener.class);
        reset(controlListener);
        cache.subscribe(listener2, controlListener);

        // Then
        verify(listener2, timeout(TIMEOUT_IN_MILLIS)).onMessage(new MessageImpl<>("k1", "v1"));
        verify(listener2, timeout(TIMEOUT_IN_MILLIS)).onMessage(new MessageImpl<>("k2", "v1"));
        verify(controlListener, timeout(TIMEOUT_IN_MILLIS)).onSnapshotEnd();

        // When
        reset(listener);
        reset(listener2);
        cache.onMessage(new MessageImpl<>("k3", "v1"));

        // Then
        verify(listener, timeout(TIMEOUT_IN_MILLIS)).onMessage(new MessageImpl<>("k3", "v1"));
        verify(listener2, timeout(TIMEOUT_IN_MILLIS)).onMessage(new MessageImpl<>("k3", "v1"));

        // Given
        cache.unsubscribe(listener);

        // When
        reset(listener);
        reset(listener2);
        cache.onMessage(new MessageImpl<>("k4", "v1"));

        // Then
        verifyNoMoreInteractions(listener);
        verify(listener2, timeout(TIMEOUT_IN_MILLIS)).onMessage(new MessageImpl<>("k4", "v1"));
    }
}