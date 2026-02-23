# 📈 High-Throughput Transaction Processing Engine

## 🧩 Project Overview

This project implements a **lightweight, high-throughput transaction processing engine** for a trading desk system.

The engine:

* Loads trade requests from a CSV file
* Validates trades (no negative positions allowed)
* Processes trades concurrently
* Maintains in-memory portfolio state
* Persists trades into an embedded relational database (H2)
* Generates summary reports using Java Streams
* Ensures thread safety and data integrity

The system simulates the core behavior of a simplified trading backend system.

---

# 🎯 Business Requirements

### ✔ One account → Multiple trades

Each account can execute multiple BUY and SELL transactions.

### ✔ One account → Multiple positions

An account may hold multiple instruments (e.g., AAPL, MSFT, TSLA).

### ✔ Trades must not allow negative quantity

A SELL trade cannot exceed the currently held quantity.

If violated:

```
Trade failed: <tradeId>
```

---

# 🏗 System Architecture

```
                +------------------+
                |   trades.csv     |
                +------------------+
                          |
                          v
                +------------------+
                | Trade Loader     |
                +------------------+
                          |
                          v
                +------------------+
                | Validation Layer |
                +------------------+
                          |
                          v
         +-----------------------------------+
         | In-Memory Portfolio (Thread Safe) |
         | ConcurrentHashMap                 |
         +-----------------------------------+
                          |
                          v
                +------------------+
                | H2 Database      |
                | (Relational)     |
                +------------------+
                          |
                          v
                +------------------+
                | Summary Reports  |
                | (Java Streams)   |
                +------------------+
```

---

# ⚙️ Technology Stack

| Component       | Technology                  |
| --------------- | --------------------------- |
| Language        | Java                        |
| Build Tool      | Maven                       |
| Database        | H2 (Embedded Relational DB) |
| Concurrency     | ExecutorService             |
| Data Processing | Java Streams                |
| Storage         | JDBC                        |

---

# 🗄 Database Details (H2)

This project uses **H2 Database (v2.2.224)** — an embedded, lightweight, relational database written in Java.

## Why H2?

* No external installation required
* Runs inside JVM
* Perfect for testing and assignments
* Supports full SQL

---

## Database Configuration

The database is created using:

```
jdbc:h2:mem:tradingdb
```

This means:

* `mem:` → In-memory database
* Data is lost when application stops
* Fast and lightweight

---

## Database Schema

### Trades Table

```sql
CREATE TABLE trades (
    trade_id INT PRIMARY KEY,
    account_id INT NOT NULL,
    symbol VARCHAR(20) NOT NULL,
    side VARCHAR(10) NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL,
    trade_value DOUBLE
);
```

---

## What Is Stored?

Each valid trade is persisted into the `trades` table.

Rejected trades are NOT stored.

---

## JDBC Integration

Database interaction is handled via:

```java
Connection connection = DriverManager.getConnection(
    "jdbc:h2:mem:tradingdb", 
    "sa", 
    ""
);
```

Trades are inserted using `PreparedStatement`.

---

# 🧠 In-Memory Portfolio Design

Portfolio state is maintained using:

```java
ConcurrentHashMap<Integer, Map<String, Integer>>
```

Structure:

```
AccountId → { Symbol → Quantity }
```

Example:

```
1001 → {
    AAPL → 40,
    MSFT → 20
}
```

---

# 🔄 Trade Processing Flow

### 1️⃣ Load Trades

* CSV file parsed
* Each row mapped to a Trade object

### 2️⃣ Concurrent Processing

* ExecutorService thread pool
* Multiple trades processed simultaneously

### 3️⃣ Validation

* If BUY → add quantity
* If SELL → check current holding

  * If insufficient → reject
  * If valid → deduct quantity

### 4️⃣ Persistence

* Valid trades inserted into H2 DB

### 5️⃣ Reporting

* Account exposure
* Total traded volume
* Top accounts
* Failed trades

---

# 📊 Sample Console Output

```
Trade failed: 32
Trade failed: 38
Trade failed: 40

---- Account Exposure ----
1016 -> 435975.0
1017 -> 285500.0
1008 -> 186472.5
...
```

---

# 🔐 Thread Safety & Data Integrity

To ensure correctness during concurrent processing:

* `ConcurrentHashMap` is used
* Atomic updates via `compute()` / `merge()`
* Validation performed before position update
* Primary key constraint on `trade_id`
* No negative positions allowed

This prevents race conditions like:

Two SELL trades reducing quantity below zero.

---

# 📈 Summary Reporting (Java Streams)

Exposure is calculated as:

```
Exposure = Σ (BUY quantity × price) − Σ (SELL quantity × price)
```

Reports generated using:

```java
stream()
.collect(Collectors.groupingBy(...))
```

---

# 🚀 How To Run

### 1️⃣ Clone repository

```
git clone <repo-url>
```

### 2️⃣ Build

```
mvn clean install
```

### 3️⃣ Run

```
mvn exec:java
```

Or run:

```
TradingEngineApplication.main()
```

---

# 🧪 Future Enhancements

* Switch to file-based H2 database
* Add connection pooling
* Add REST API layer
* Add transaction management
* Introduce audit logging
* Implement position PnL calculation
* Add integration tests

---

# 🎓 What This Project Demonstrates

* Concurrency handling
* Thread safety
* Financial trade validation logic
* Relational database integration
* Stream-based data aggregation
* Clean system architecture
* End-to-end processing pipeline

---

# 🏁 Conclusion

This project simulates the core of a simplified trading engine capable of:

* High-throughput trade processing
* Safe concurrent execution
* Real-time portfolio tracking
* Persistent trade storage
* Analytical reporting

It represents a strong foundation for building scalable financial transaction systems.

---
