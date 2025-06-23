package com.ac.common.exchange2;

import com.lmax.disruptor.EventFactory;

public class FillEvent {
    int price;
    int volume;
    char[] bidUserId = new char[16];
    char[] askUserId = new char[16];

    public void set(int price, int volume, char[] bidUserId, char[] askUserId) {
        this.price = price;
        this.volume = volume;
        System.arraycopy(bidUserId, 0, this.bidUserId, 0, Math.min(bidUserId.length, 16));
        System.arraycopy(askUserId, 0, this.askUserId, 0, Math.min(askUserId.length, 16));
    }

    static class FillEventFactory implements EventFactory<FillEvent> {
        @Override
        public FillEvent newInstance() {
            return new FillEvent();
        }
    }
}
