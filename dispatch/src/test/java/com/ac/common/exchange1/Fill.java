package com.ac.common.exchange1;

public class Fill {
    double price;
    int volume;
    String bidUserId;
    String askUserId;

    public Fill(double price, int volume, String bidUserId, String askUserId) {
        this.price = price;
        this.volume = volume;
        this.bidUserId = bidUserId;
        this.askUserId = askUserId;
    }

    @Override
    public String toString() {
        return String.format("FILL @ %.2f for %d | Buyer: %s, Seller: %s", 
                             price, volume, bidUserId, askUserId);
    }
}
