package com.trading.engine.service;

import com.trading.engine.model.Position;

import java.util.stream.*;

public class ReportService {

    public static void generateSummary(PortfolioService portfolioService) {

        System.out.println("---- Account Exposure ----");

        portfolioService.getAccounts().values().stream()
                .forEach(acc -> {

                    double exposure = acc.getPositions().values().stream()
                            .mapToDouble(Position::getExposure)
                            .sum();


                    System.out.println(acc.getAccountId() + " -> " + exposure);
                });
    }
}
