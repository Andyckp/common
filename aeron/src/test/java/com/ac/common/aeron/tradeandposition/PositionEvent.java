package com.ac.common.aeron.tradeandposition;

public class PositionEvent {
    private String instrumentId;
    private String portfolioId;
    private double quantity;
    private double notional;

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getNotional() {
        return notional;
    }

    public void setNotional(double notional) {
        this.notional = notional;
    }

    @Override
    public String toString() {
        return "PositionEvent{" +
            "instrumentId='" + instrumentId + '\'' +
            ", portfolioId='" + portfolioId + '\'' +
            ", quantity=" + quantity +
            ", notional=" + notional +
            '}';
    }
}
