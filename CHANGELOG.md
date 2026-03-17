# Changelog

All notable changes to this project will be documented in this file.

The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).
This project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [Unreleased]

## [1.0.0] — 2026-03-17

### Added
- Initial release
- Maven archetype generating a Spring Boot 4 + Java 25 hexagonal architecture project
- Complete vertical slice: `POST /orders` → `PlaceOrderUseCase` → `Order` domain → `JpaOrderRepository` → PostgreSQL
- ArchUnit 1.4.1 rule set enforcing all hexagonal layer constraints (7 rules)
- Testcontainers 2.0.3 integration test against PostgreSQL
- H2-backed repository unit test (no Docker required)
- `@WebMvcTest` slice test for the REST adapter
- Micrometer + Spring Boot Actuator with Prometheus endpoint
- Bean Validation on the REST request model
- `api.version=1.44` system property in Failsafe to support Docker Engine 29.x+
- `.gitignore`, `README.md`, and Maven wrapper included in generated project

[Unreleased]: https://github.com/marvinrichter/spring-hexagonal-archetype/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/marvinrichter/spring-hexagonal-archetype/releases/tag/v1.0.0
