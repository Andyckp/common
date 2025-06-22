package com.ac.common.exchange;

public class Order {
    int priceTick;
    int volume;
    String userId;
    Side side;

    public Order(int priceTick, int volume, String userId, Side side) {
        this.priceTick = priceTick;
        this.volume = volume;
        this.userId = userId;
        this.side = side;
    }
}
