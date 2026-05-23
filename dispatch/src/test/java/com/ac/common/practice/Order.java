package com.ac.common.practice;

public class Order {
    private int price;
    private int quantity;

    public Order(int price, int quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Order{" +
            "price=" + price +
            ", quantity=" + quantity +
            '}';
    }
}
