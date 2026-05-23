package com.ac.common.practice2;

public class Order {
    private final long price;
    private long quantity;

    public Order(long price, long quantity) {
        if (price < 0L) {
            throw new IllegalArgumentException("price must be >= 0");
        }
        if (quantity < 0L) {
            throw new IllegalArgumentException("quantity must be >= 0");
        }
        this.price = price;
        this.quantity = quantity;
    }

    public void adjustQuantity(long delta) {
        long newQuantity = this.quantity + delta;
        if (newQuantity < 0L) {
            throw new IllegalArgumentException("resulting quantity cannot be negative");
        }
        this.quantity = newQuantity;
    }

    public long getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Order{" +
            "price=" + price +
            ", quantity=" + quantity +
            '}';
    }
}
