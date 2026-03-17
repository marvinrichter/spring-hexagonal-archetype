# Contributing

Thanks for taking the time to contribute. This is a focused archetype — contributions
that keep it lean and opinionated are more valuable than ones that expand scope.

## Before you open a PR

- Check the [open issues](https://github.com/marvinrichter/spring-hexagonal-archetype/issues)
  to avoid duplicate work
- For significant changes, open an issue first to discuss the approach
- Keep PRs small and focused — one concern per PR

## Prerequisites

- Java 25
- Maven 3.9+
- Docker (required for the Testcontainers integration test inside the archetype)

## Setup

```bash
git clone https://github.com/marvinrichter/spring-hexagonal-archetype.git
cd spring-hexagonal-archetype
./mvnw verify
```

`mvn verify` generates a project from the archetype and runs its full test suite
(unit tests, ArchUnit architecture tests, and Testcontainers integration tests).
All tests must pass before a PR can be merged.

## Project structure

```
src/
├── main/resources/
│   ├── META-INF/maven/archetype-metadata.xml   # Declares which files are archetype resources
│   └── archetype-resources/                    # Template files — become the generated project
│       ├── pom.xml
│       ├── README.md
│       └── src/...
└── test/resources/projects/basic/              # Archetype integration test fixture
    ├── archetype.properties                    # Parameters passed to archetype:generate
    └── goal.txt                                # Maven goals run against the generated project
```

**Velocity templates:** Files inside `archetype-resources/` are processed by the
[Velocity](https://velocity.apache.org/) template engine. Use `${groupId}`,
`${artifactId}`, `${version}`, `${package}`, and `${packageInPathFormat}` as
placeholders. To emit a literal `#`, `$`, or `\`, use the escape sequences defined
at the top of each `.java` file (`#set( $symbol_pound = '#' )` etc.).

## What belongs in this archetype (and what doesn't)

**In scope:**
- Changes that make the generated project cleaner, more correct, or more idiomatic
- ArchUnit rule improvements
- Dependency updates (Spring Boot, Testcontainers, ArchUnit)
- Bug fixes where `mvn archetype:generate` or `./mvnw verify` fails

**Out of scope:**
- Security (auth is project-specific — no Spring Security, OAuth2, etc.)
- Schema migration tooling (Flyway, Liquibase)
- Additional adapters beyond the single REST + JPA slice
- Lombok or other annotation processors
- Docker Compose files

When in doubt, open an issue first.

## Commit messages

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: add @NotBlank validation to PlaceOrderRequest
fix: correct Velocity escape sequence in OrderController
chore: bump Spring Boot to 4.0.4
docs: clarify ArchUnit LocationProvider note in README
```

## Code style

- Match the style of the existing generated files (no Lombok, no custom formatters)
- Archetype template Java files must compile cleanly after generation
- Run `./mvnw verify` locally before pushing — CI will reject failing builds

## Reporting security issues

See [SECURITY.md](SECURITY.md).
