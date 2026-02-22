package com.trading.engine.model;

public class Position {

    private final String symbol;
    private int quantity;
    private double totalCost;  // track total cost instead of avgPrice

    public Position(String symbol) {
        this.symbol = symbol;
    }

    public synchronized void updatePosition(int qty, double price, boolean isBuy) {

        if (isBuy) {

            totalCost += qty * price;
            quantity += qty;

        } else {

            if (quantity < qty)
                throw new IllegalStateException("Insufficient quantity to sell");

            double avgPrice = totalCost / quantity;

            totalCost -= avgPrice * qty;   // reduce proportional cost
            quantity -= qty;
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getAvgPrice() {
        if (quantity == 0) return 0;
        return totalCost / quantity;
    }

    public double getExposure() {
        return totalCost;
    }
}
