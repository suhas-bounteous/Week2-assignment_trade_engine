package com.trading.engine.repository;

import com.trading.engine.model.Trade;

import java.sql.*;

public class TradeRepository {

    private final Connection connection;

    public TradeRepository(Connection connection) {
        this.connection = connection;
    }

    public void saveTrade(Trade trade) throws SQLException {

        String sql = "INSERT INTO trades VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, trade.getTradeId());
            ps.setString(2, trade.getAccountId());
            ps.setString(3, trade.getSymbol());
            ps.setInt(4, trade.getQuantity());
            ps.setDouble(5, trade.getPrice());
            ps.setString(6, trade.getSide());
            ps.setTimestamp(7, Timestamp.valueOf(trade.getTimestamp()));

            ps.executeUpdate();
        }
    }
}
