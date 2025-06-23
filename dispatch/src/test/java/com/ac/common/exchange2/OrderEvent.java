package com.ac.common.exchange2;

import com.lmax.disruptor.EventFactory;

public class OrderEvent {
    private int price;
    private int volume;
    private final char[] userId = new char[16];
    private final char[] instrumentId = new char[16]; 
    private Side side;

    public void set(int price, int volume, char[] userId, char[] instrumentId, Side side) {
        this.price = price;
        this.volume = volume;
        System.arraycopy(userId, 0, this.userId, 0, Math.min(userId.length, 16));
        System.arraycopy(instrumentId, 0, this.instrumentId, 0, Math.min(instrumentId.length, 16));
        this.side = side;
    }
    
    public int getPrice() {
        return price;
    }
    
    public int getVolume() {
        return volume;
    }
    
    public char[] getUserId() {
        return userId;
    }
    
    public char[] getInstrumentId() {
        return instrumentId;
    }
    
    public Side getSide() {
        return side;
    }
    
    public void setVolume(int volume) {
        this.volume = volume;
    }
    
    static class OrderEventFactory implements EventFactory<OrderEvent> {
        @Override
        public OrderEvent newInstance() {
            return new OrderEvent();
        }
    }
}
