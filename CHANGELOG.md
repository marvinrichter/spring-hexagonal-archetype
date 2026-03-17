# Changelog

All notable changes to this project will be documented in this file.

The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).
This project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.1.1](https://github.com/marvinrichter/spring-hexagonal-archetype/compare/v1.1.0...v1.1.1) (2026-03-17)


### Maintenance

* Bump actions/checkout from 4 to 6 ([c1c8f43](https://github.com/marvinrichter/spring-hexagonal-archetype/commit/c1c8f43119b602799fda336b1e99faceb5cde0b7))
* Bump actions/checkout from 4 to 6 ([c8828f1](https://github.com/marvinrichter/spring-hexagonal-archetype/commit/c8828f1538692102141f5d08a48ed36d4f988ef3))
* Bump actions/setup-java from 4 to 5 ([ed42d7a](https://github.com/marvinrichter/spring-hexagonal-archetype/commit/ed42d7ae76b7945ecaaa02a77aeb430c5fd62bb6))
* Bump org.apache.maven.plugins:maven-resources-plugin from 3.3.1 to 3.5.0 ([eea9b2e](https://github.com/marvinrichter/spring-hexagonal-archetype/commit/eea9b2e7eb5d2434d7fd7ab489cfdae609da4887))
* **main:** release 1.1.1-SNAPSHOT ([#6](https://github.com/marvinrichter/spring-hexagonal-archetype/issues/6)) ([0c2c480](https://github.com/marvinrichter/spring-hexagonal-archetype/commit/0c2c480dea756a6845b038b12ab6e528e363fb3f))

## [1.1.0](https://github.com/marvinrichter/spring-hexagonal-archetype/compare/v1.0.0...v1.1.0) (2026-03-17)


### Features

* initial release of spring-hexagonal-archetype 1.0.0 ([878bd50](https://github.com/marvinrichter/spring-hexagonal-archetype/commit/878bd50feaab51e07a44d4bc23d0707e38f5daf2))


### Maintenance

* add dependabot, release-please, and GitHub Packages publishing ([be45f43](https://github.com/marvinrichter/spring-hexagonal-archetype/commit/be45f43009b692bd97d65f2062d4ffeaea845a61))
* add SPDX identifier to LICENSE for GitHub detection ([c5890b6](https://github.com/marvinrichter/spring-hexagonal-archetype/commit/c5890b63c7c4bb9d37107494d9b0f462cb74b273))
* **main:** release 1.0.1-SNAPSHOT ([7584494](https://github.com/marvinrichter/spring-hexagonal-archetype/commit/7584494888e97b53be0070e6f19583cc5caafffb))
* **main:** release 1.0.1-SNAPSHOT ([56f455b](https://github.com/marvinrichter/spring-hexagonal-archetype/commit/56f455b07711b4e3252a29088ae8037dfb74df64))

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
