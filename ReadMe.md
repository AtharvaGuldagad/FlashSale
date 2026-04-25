# ⚡ Flash Sale E-Commerce Platform

A highly concurrent, event-driven microservices architecture designed to handle massive traffic spikes during e-commerce flash sales. 

This project demonstrates how to decouple services using **Apache Kafka**, manage distributed transactions using the **Saga Pattern (Choreography)**, and prevent database race conditions under heavy load using **Pessimistic Locking**.

## 🏗️ Architecture & Tech Stack

* **Backend:** Java 17, Spring Boot, Spring Data JPA
* **Databases:** 3 isolated H2 In-Memory Databases (Database-per-service pattern)
* **Message Broker:** Apache Kafka (KRaft mode)
* **Frontend:** React, Vite, Axios (made with AntiGravity)
* **Testing:** Artillery (Load & Stress Testing)

### The Microservices
1. **Order Service:** Manages user checkout and final order status.
2. **Inventory Service:** Manages stock levels. Implements row-level database locking to prevent overselling.
3. **Payment Service:** Simulates a third-party payment gateway and determines transaction success/failure.

## 🔄 The Saga Pattern (Choreography)
To ensure data consistency without a monolithic database, this system uses an event-driven Saga pattern:
1. User places an order ➡️ `OrderCreatedEvent` published.
2. Inventory Service deducts stock ➡️ `InventoryReservedEvent` published.
3. Payment Service processes transaction ➡️ `PaymentEvent` (Success/Failed) published.
4. **The Rollback:** If payment fails, the Order Service catches the failure, cancels the order, and publishes an `OrderCancelledEvent`. The Inventory Service catches this and restores the stock.

## 🚀 Concurrency & Load Testing
A primary goal of this project was to handle the "Snowball Effect" of concurrent database reads/writes during a flash sale. 

I load-tested the system using **Artillery** at **100 requests/second**. 
* **The Problem:** The initial test revealed a severe Race Condition where overlapping threads overwrote database states, resulting in lost transactions and inaccurate stock counts.
* **The Solution:** Implemented `@Lock(LockModeType.PESSIMISTIC_WRITE)` and `@Transactional` on the Inventory database queries.
* **The Result:** The system now successfully handles 1,000+ concurrent checkout requests with 100% mathematical accuracy and zero overselling.

## 💻 How to Run Locally

*(Coming Soon: 1-Click Docker Compose Setup)*

**Manual Setup:**
1. Start Apache Kafka: `.\bin\windows\kafka-server-start.bat .\config\kraft\server.properties`
2. Run the Inventory Service (Port `8081`)
3. Run the Order Service (Port `8080`)
4. Run the Payment Service (Port `8082`)
5. Navigate to the client folder, run `npm install`, then `npm run dev` to launch the React frontend.