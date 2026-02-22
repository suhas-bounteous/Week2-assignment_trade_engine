package com.trading.engine;

import com.trading.engine.repository.TradeRepository;
import com.trading.engine.service.*;
import com.trading.engine.util.FileLoader;
import com.trading.engine.model.Trade;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.*;

public class TradingEngineApplication {

    public static void main(String[] args) throws Exception {

        Connection connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");

        connection.createStatement().execute(
                "CREATE TABLE trades (" +
                        "trade_id VARCHAR(50) PRIMARY KEY," +
                        "account_id VARCHAR(50)," +
                        "symbol VARCHAR(20)," +
                        "quantity INT CHECK (quantity > 0)," +
                        "price DOUBLE," +
                        "side VARCHAR(10)," +
                        "trade_time TIMESTAMP)"
        );

        BlockingQueue<Trade> queue = new LinkedBlockingQueue<>();
        PortfolioService portfolioService = new PortfolioService();
        TradeRepository repository = new TradeRepository(connection);

        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            executor.submit(new TradeProcessor(queue, portfolioService));
        }


        FileLoader.load("src/main/resources/trades.csv", queue);


        Thread.sleep(3000);

        ReportService.generateSummary(portfolioService);

        executor.shutdown();
    }
}
