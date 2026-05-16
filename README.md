# Sporty Demo

Backend service that simulates sports betting **event outcome handling** and **bet settlement** using **Kafka** and **RocketMQ**.

## Architecture

```text
REST API  -->  Kafka (event-outcomes)  -->  Consumer  -->  match bets (H2)  -->  RocketMQ (bet-settlements)
```

1. **POST /api/v1/event-outcomes** publishes an event outcome to the Kafka topic `event-outcomes`.
2. A **Kafka consumer** listens on `event-outcomes`.
3. The consumer loads bets from an **in-memory H2 database** (seeded via **Liquibase**) where `event_id` matches the outcome.
4. For each matching bet, a **RocketMQ producer** sends a settlement message to the topic `bet-settlements`.

## Prerequisites

- **Java 21**
- **Maven 3.9+**
- **Docker** and **Docker Compose** (for Kafka and RocketMQ)

## Quick start

### 1. Start infrastructure

```bash
docker compose up -d
```

Wait until Kafka (`localhost:9092`) and RocketMQ NameServer (`localhost:9876`) are up.

### 2. Run the application

```bash
mvn spring-boot:run
```

The API listens on **http://localhost:8080**.

Optional environment overrides:

| Variable | Default |
|----------|---------|
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092` |
| `ROCKETMQ_NAME_SERVER` | `127.0.0.1:9876` |

### 3. Inspect seed data

Sample bets are inserted at startup by Liquibase:

```bash
curl http://localhost:8080/api/v1/bets
```

| Bet ID | User ID | Event ID | Market ID | Picked winner ID | Amount |
|--------|---------|----------|-----------|------------------|--------|
| 1 | 101 | 1 | 1 | 1 (Dinamo Zagreb) | 50.00 |
| 2 | 102 | 1 | 1 | 3 (HNK Hajduk Split) | 25.00 |
| 3 | 103 | 2 | 1 | 4 (HNK Rijeka) | 100.00 |
| 4 | 104 | 2 | 1 | 7 (NK Osijek) | 75.50 |

Teams (IDs 1–10): Dinamo Zagreb, HNK Gorica, HNK Hajduk Split, HNK Rijeka, NK Istra 1961, NK Lokomotiva, NK Osijek, NK Slaven Belupo, NK Varaždin, HNK Vukovar 1991.

All ID columns are `BIGINT` values greater than zero. Reference tables:

| Table | Columns | Referenced by `bets` |
|-------|---------|----------------------|
| `users` | `id`, `name` | `user_id` |
| `events` | `id`, `name` | `event_id` |
| `markets` | `id`, `name` | `event_market_id` |
| `teams` | `id`, `name` | `event_winner_id` (picked winner) |

H2 console (optional): http://localhost:8080/h2-console  
JDBC URL: `jdbc:h2:mem:sportydb`, user `sa`, empty password.

## Using the API

### Publish an event outcome

```bash
curl -X POST http://localhost:8080/api/v1/event-outcomes \
  -H "Content-Type: application/json" \
  -d '{
    "eventId": 1,
    "eventName": "Dinamo Zagreb vs HNK Hajduk Split",
    "eventWinnerId": 1
  }'
```

**Request body**

| Field | Description |
|-------|-------------|
| `eventId` | Sports event identifier (positive number) |
| `eventName` | Human-readable event name |
| `eventWinnerId` | Actual winner identifier (positive number) |

**Response** (`202 Accepted`): the same payload echoed back after it is sent to Kafka.

### What happens next

1. Message is written to Kafka topic **`event-outcomes`**.
2. The consumer finds all bets with matching **`eventId`**.
3. For each bet, a message is sent to RocketMQ topic **`bet-settlements`**:

| Settlement field | Meaning |
|------------------|---------|
| `betId`, `userId`, `eventId`, `eventMarketId`, `eventWinnerId`, `betAmount` | Copied from the bet |
| `eventName`, `actualWinnerId` | From the event outcome |
| `won` | `true` if the bet’s `eventWinnerId` equals the outcome’s `eventWinnerId` |
| `payout` | `2 × betAmount` when won, otherwise `0` |

### Example: settle event `1` with Dinamo Zagreb (team id `1`) as winner

```bash
curl -X POST http://localhost:8080/api/v1/event-outcomes \
  -H "Content-Type: application/json" \
  -d '{"eventId":1,"eventName":"Dinamo Zagreb vs HNK Hajduk Split","eventWinnerId":1}'
```

Expected settlements:

- **Bet 1** (picked Dinamo) — won, payout `100.00`
- **Bet 2** (picked Hajduk) — lost, payout `0`

Check application logs for Kafka and RocketMQ publish confirmations.

## Build and test

```bash
mvn clean test
mvn clean package
java -jar target/sporty-demo-1.0.0-SNAPSHOT.jar
```

## Project layout

```text
src/main/java/com/sporty/demo/
  controller/     REST API
  kafka/            Kafka producer & consumer
  rocketmq/         RocketMQ producer
  service/          Service interfaces
  service/impl/     Service implementations
  domain/           JPA entities (Bet, User, Event, Market, Team)
  repository/       Bet persistence
  dto/              API and messaging payloads
src/main/resources/
  application.yml
  db/changelog/     Liquibase migrations & seed data
```

## Tech stack

- Java 21, Lombok
- Spring Boot 3.5 (Web, Data JPA, Kafka)
- Apache Kafka (`event-outcomes`)
- Apache RocketMQ (`bet-settlements`)
- H2 in-memory database
- Liquibase

## Stopping services

```bash
docker compose down
```
