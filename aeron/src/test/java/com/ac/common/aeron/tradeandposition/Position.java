package com.ac.common.aeron.tradeandposition;

public class Position {
    private final String instrumentId;
    private final String portfolioId;
    private double quantity;
    private double notional;

    public Position(String instrumentId, String portfolioId) {
        this.instrumentId = instrumentId;
        this.portfolioId = portfolioId;
        this.quantity = 0;
        this.notional = 0.0;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getNotional() {
        return notional;
    }

    public void updatePosition(double quantity, double price, String side) {
        if ("B".equalsIgnoreCase(side)) {
            this.quantity += quantity;
            notional += quantity * price;
        } else if ("S".equalsIgnoreCase(side)) {
            this.quantity -= quantity;
            notional -= quantity * price;
        }
    }

    @Override
    public String toString() {
        return "Position{" +
            "instrumentId='" + instrumentId + '\'' +
            ", portfolioId='" + portfolioId + '\'' +
            ", totalQuantity=" + quantity +
            ", totalValue=" + notional +
            '}';
    }
}
