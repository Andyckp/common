package com.ac.common.exchange2;

import com.lmax.disruptor.EventFactory;

public class OrderEvent {
    int price;
    int volume;
    char[] userId = new char[16];
    char[] instrumentId = new char[16]; 
    Side side;

    public void set(int price, int volume, String userId, String instrumentId, Side side) {
        this.price = price;
        this.volume = volume;
        System.arraycopy(userId.toCharArray(), 0, this.userId, 0, Math.min(userId.length(), 16));
        System.arraycopy(instrumentId.toCharArray(), 0, this.instrumentId, 0, Math.min(instrumentId.length(), 16));
        this.side = side;
    }

    static class OrderEventFactory implements EventFactory<OrderEvent> {
        @Override
        public OrderEvent newInstance() {
            return new OrderEvent();
        }
    }
}
