package com.trading.engine.service;

import com.trading.engine.model.Account;
import com.trading.engine.model.Trade;

import java.util.concurrent.ConcurrentHashMap;

public class PortfolioService {

    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Object> accountLocks = new ConcurrentHashMap<>();

    public void processTrade(Trade trade) {

        // Create lock per account
        Object lock = accountLocks.computeIfAbsent(trade.getAccountId(), k -> new Object());

        synchronized (lock) {

            Account account = accounts.computeIfAbsent(
                    trade.getAccountId(),
                    Account::new
            );

            account.processTrade(trade);
        }
    }

    public ConcurrentHashMap<String, Account> getAccounts() {
        return accounts;
    }
}
