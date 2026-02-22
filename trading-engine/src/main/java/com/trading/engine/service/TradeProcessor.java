package com.trading.engine.service;

import com.trading.engine.model.Trade;
import com.trading.engine.repository.TradeRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.BlockingQueue;

public class TradeProcessor implements Runnable {

    private final BlockingQueue<Trade> queue;
    private final PortfolioService portfolioService;

    public TradeProcessor(BlockingQueue<Trade> queue,
                          PortfolioService portfolioService) {
        this.queue = queue;
        this.portfolioService = portfolioService;
    }

    @Override
    public void run() {

        try (Connection connection =
                     DriverManager.getConnection("jdbc:h2:mem:test")) {

            TradeRepository repository = new TradeRepository(connection);

            while (true) {

                Trade trade = queue.take();

                try {
                    connection.setAutoCommit(false);

                    portfolioService.processTrade(trade);
                    repository.saveTrade(trade);

                    connection.commit();

                } catch (Exception e) {
                    connection.rollback();
                    System.out.println("Trade failed: " + trade.getTradeId());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
