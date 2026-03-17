#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
${symbol_pound} ${artifactId}

Generated from [spring-hexagonal-archetype](https://github.com/marvinrichter/spring-hexagonal-archetype) —
a production-ready Spring Boot 4 project wired for hexagonal (ports-and-adapters) architecture.

${symbol_pound}${symbol_pound} Architecture

```
src/main/java/${package}/
├── domain/           # Pure Java. Zero framework imports. Business invariants live here.
├── application/
│   ├── port/in/      # Inbound ports  — interfaces your use cases expose to the world
│   ├── port/out/     # Outbound ports — interfaces your use cases need from the world
│   └── service/      # Use case implementations
└── adapter/
    ├── in/web/        # REST controllers — inbound adapters
    └── out/persistence/ # JPA repositories — outbound adapters
```

The dependency rule flows inward only:

```
Adapter → Application (ports) → Domain
```

Adapters know about ports. Ports know about the domain. The domain knows nothing outside itself.
This is enforced structurally by ArchUnit — violations fail the build.

${symbol_pound}${symbol_pound} Running the tests

${symbol_pound}${symbol_pound}${symbol_pound} Unit tests + architecture rules

```bash
./mvnw test
```

Runs unit tests and ArchUnit architecture enforcement. No Docker required.

${symbol_pound}${symbol_pound}${symbol_pound} Integration tests

```bash
./mvnw verify
```

Runs the full suite including Testcontainers PostgreSQL integration tests. Requires Docker.

${symbol_pound}${symbol_pound} Starting the application

```bash
./mvnw spring-boot:run
```

Endpoints:

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/orders` | Place a new order |
| `GET`  | `/actuator/health` | Health check |
| `GET`  | `/actuator/prometheus` | Metrics (Prometheus format) |

Example request:

```bash
curl -X POST http://localhost:8080/orders \
  -H 'Content-Type: application/json' \
  -d '{"customerId": "cust-1", "totalAmount": 49.99, "currency": "EUR"}'
```

${symbol_pound}${symbol_pound} Architecture rules (enforced on every build)

The `ArchitectureTest` verifies these constraints via ArchUnit:

- Domain layer has no Spring or Jakarta EE imports
- Domain layer has no adapter imports
- Application layer has no adapter imports
- All types in `application/port/**` are interfaces (nested records are allowed)
- Inbound adapters (`adapter/in`) never import outbound adapters (`adapter/out`)
- Outbound adapters (`adapter/out`) never import inbound adapters (`adapter/in`)

${symbol_pound}${symbol_pound} Extending the archetype output

To add a second use case:

1. Define an inbound port interface in `application/port/in/`
2. Implement it as a service in `application/service/`
3. Wire the controller to the port interface (never to the service directly)
4. If new persistence is needed, define an outbound port in `application/port/out/` and implement it in `adapter/out/persistence/`

The ArchUnit rules will catch any accidental cross-layer dependencies.
