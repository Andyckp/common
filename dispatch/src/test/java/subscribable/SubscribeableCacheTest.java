package subscribable;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class SubscribeableCacheTest {
    private static final Logger logger = LoggerFactory.getLogger(SubscribeableCacheTest.class);

    @Test
    public void test() {
        SubscribeableCache cache = new SubscribeableCache();

        cache.onMessage(new MessageImpl("k1", "v1"));
        cache.subscribe(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                logger.info("onMessage: {}", message);
            }
        }, new ControlListener() {
            @Override
            public void onSnapshotEnd() {
                logger.info("onSnapshotEnd");
            }
        });

        cache.onMessage(new MessageImpl("k2", "v1"));

        cache.subscribe(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                logger.info("onMessage: {}", message);
            }
        }, new ControlListener() {
            @Override
            public void onSnapshotEnd() {
                logger.info("onSnapshotEnd");
            }
        });
    }
}