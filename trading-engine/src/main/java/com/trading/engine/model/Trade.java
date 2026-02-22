package com.trading.engine.model;

import java.time.LocalDateTime;

public class Trade {

    private final String tradeId;
    private final String accountId;
    private final String symbol;
    private final int quantity;
    private final double price;
    private final String side;
    private final LocalDateTime timestamp;

    public Trade(String tradeId, String accountId, String symbol,
                 int quantity, double price, String side) {
        this.tradeId = tradeId;
        this.accountId = accountId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.side = side;
        this.timestamp = LocalDateTime.now();
    }

    public String getTradeId() { return tradeId; }
    public String getAccountId() { return accountId; }
    public String getSymbol() { return symbol; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public String getSide() { return side; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
