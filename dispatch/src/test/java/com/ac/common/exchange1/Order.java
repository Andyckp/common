package com.ac.common.exchange1;

public class Order {
    double price;
    int volume;
    String userId;
    Side side;

    public Order(double price, int volume, String userId, Side side) {
        this.price = price;
        this.volume = volume;
        this.userId = userId;
        this.side = side;
    }
}



