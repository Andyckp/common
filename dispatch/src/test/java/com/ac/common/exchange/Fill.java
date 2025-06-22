package com.ac.common.exchange;

public class Fill {
    int priceTick;
    int volume;
    String bidUserId;
    String askUserId;

    public Fill(int priceTick, int volume, String bidUserId, String askUserId) {
        this.priceTick = priceTick;
        this.volume = volume;
        this.bidUserId = bidUserId;
        this.askUserId = askUserId;
    }

    @Override
    public String toString() {
        return String.format("FILL @ %d ticks [%d units] Buyer: %s, Seller: %s",
                priceTick, volume, bidUserId, askUserId);
    }
}
