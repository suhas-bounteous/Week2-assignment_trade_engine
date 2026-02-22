package com.trading.engine.model;

import java.util.concurrent.ConcurrentHashMap;

public class Account {

    private final String accountId;
    private final ConcurrentHashMap<String, Position> positions = new ConcurrentHashMap<>();

    public Account(String accountId) {
        this.accountId = accountId;
    }

    public void processTrade(Trade trade) {

        if (trade.getQuantity() <= 0)
            throw new IllegalArgumentException("Quantity cannot be negative");

        positions.compute(trade.getSymbol(), (symbol, pos) -> {
            if (pos == null) pos = new Position(symbol);

            boolean isBuy = trade.getSide().equalsIgnoreCase("BUY");
            pos.updatePosition(trade.getQuantity(), trade.getPrice(), isBuy);

            return pos;
        });
    }

    public ConcurrentHashMap<String, Position> getPositions() {
        return positions;
    }

    public String getAccountId() {
        return accountId;
    }
}
