# spring-hexagonal-archetype

A Maven archetype that generates a production-ready Spring Boot 4 project wired for
**hexagonal (ports-and-adapters) architecture** — with ArchUnit enforcement, Testcontainers
integration tests, and Micrometer observability baked in from day one.

This is the project structure I use on every greenfield JVM client engagement.
Not because it's fashionable, but because it consistently delivers the one thing
legacy codebases lack: a seam. A place where you can swap the database, add a
message broker, or extract a service without touching the domain.

---

## Why hexagonal architecture?

Most Spring Boot projects start flat — controllers calling repositories, `@Service`
classes importing JPA entities, business logic scattered across layers that were
never really layers at all. That's fine until it isn't: until you need to replace
Hibernate, add a second delivery mechanism (gRPC, events), or test the core logic
without spinning up a database.

Hexagonal architecture gives you a **pure domain core** surrounded by **ports**
(interfaces your application defines) and **adapters** (implementations that talk
to the outside world). The domain doesn't know Spring exists. The adapters don't
know about each other.

The payoff isn't theoretical. It shows up the first time you:
- Unit-test a use case with no mocks beyond a simple stub
- Swap H2 for PostgreSQL in tests with zero code changes
- Add a CLI adapter alongside the REST adapter in an afternoon

This archetype encodes those constraints structurally so you can't accidentally
violate them — and ArchUnit will tell you immediately if you try.

---

## What's generated

```
src/
├── main/java/{package}/
│   ├── domain/                          # Pure Java. Zero framework imports.
│   │   ├── Order.java                   # Aggregate root with lifecycle methods
│   │   ├── OrderId.java                 # Typed identifier (no raw Strings/UUIDs)
│   │   └── Money.java                   # Value object with currency
│   │
│   ├── application/
│   │   ├── port/
│   │   │   ├── in/PlaceOrderUseCase.java    # Inbound port (interface + Command/Result)
│   │   │   └── out/OrderRepository.java     # Outbound port (interface)
│   │   └── service/PlaceOrderService.java   # Use case implementation
│   │
│   └── adapter/
│       ├── in/web/
│       │   ├── OrderController.java     # REST adapter — depends on port, not service
│       │   ├── PlaceOrderRequest.java
│       │   └── OrderResponse.java
│       └── out/persistence/
│           ├── JpaOrderRepository.java  # Outbound adapter — implements port
│           ├── OrderEntity.java         # JPA entity (separate from domain object)
│           ├── OrderMapper.java         # Maps between domain and persistence model
│           └── SpringDataOrderRepository.java
│
└── test/java/{package}/
    ├── architecture/
    │   └── ArchitectureTest.java        # ArchUnit rules — enforced on every build
    ├── adapter/in/web/
    │   └── OrderControllerTest.java     # @WebMvcTest slice
    ├── adapter/out/persistence/
    │   └── OrderRepositoryTest.java     # Repository test against H2
    └── application/
        └── PlaceOrderIntegrationTest.java  # Full round-trip against PostgreSQL (Testcontainers)
```

**One complete vertical slice:** `POST /orders` → `PlaceOrderUseCase` → `Order.placeOrder()` → `JpaOrderRepository` → PostgreSQL.

---

## Architecture rules enforced by ArchUnit

Every build verifies these constraints. Violations fail the build.

| Rule | What it prevents |
|------|-----------------|
| Domain has no Spring imports | `@Service`, `@Entity` leaking into domain logic |
| Domain has no Jakarta EE imports | JPA annotations polluting the aggregate root |
| Domain has no adapter imports | Domain knowing how it's persisted or served |
| Application has no adapter imports | Use cases importing controllers or JPA repos |
| All ports are interfaces | Concrete classes masquerading as ports |
| Inbound adapters don't import outbound adapters | Controllers calling JPA repos directly |
| Outbound adapters don't import inbound adapters | Repos knowing about HTTP layer |

---

## Getting started

**Prerequisites:** Java 25, Maven 3.9+, Docker (for integration tests)

### Generate a project

```bash
mvn archetype:generate \
  -DarchetypeGroupId=io.github.marvinrichter \
  -DarchetypeArtifactId=spring-hexagonal-archetype \
  -DarchetypeVersion=1.0.0 \
  -DgroupId=com.example \
  -DartifactId=my-service \
  -Dpackage=com.example.myservice \
  -DinteractiveMode=false
```

### Run the tests

```bash
cd my-service

# Unit tests + ArchUnit architecture tests
./mvnw test

# Integration tests (requires Docker — starts PostgreSQL via Testcontainers)
./mvnw verify
```

### Start the application

```bash
./mvnw spring-boot:run
# POST http://localhost:8080/orders
# GET  http://localhost:8080/actuator/health
# GET  http://localhost:8080/actuator/prometheus
```

---

## Stack

| Concern | Choice | Why |
|---------|--------|-----|
| Framework | Spring Boot 4 | Current baseline; Jakarta EE 11 throughout |
| Java | 25 | Records, pattern matching, virtual threads |
| Architecture enforcement | ArchUnit 1.4.1 | Compile-time layer violations → build failure |
| Integration tests | Testcontainers 2.0.3 + PostgreSQL | Real database, no flakiness from mocks |
| Unit-level repo tests | H2 | No Docker dependency for fast feedback |
| Observability | Micrometer + Actuator + Prometheus | Wired, not bolted on |
| Validation | Jakarta Validation (Bean Validation 3) | Standard; no custom framework |

---

## What's not included (and why)

- **Security** — auth strategy is project-specific; the archetype doesn't assume OAuth2, API keys, or mTLS
- **Flyway / Liquibase** — schema management belongs to the team's migration discipline, not the archetype
- **Docker Compose** — Testcontainers handles the test environment; production compose is deployment concern
- **Lombok** — reduces boilerplate but obscures what the archetype is demonstrating; Java records cover most cases

---

## Need help setting up or modernising a JVM codebase?

This archetype is the starting point. The harder work is getting an existing system
to this structure — extracting domain logic, introducing ports, breaking persistence
coupling — without stopping feature delivery.

That's the kind of engagement I take on. → **[marvin-richter.de](https://marvin-richter.de)**
