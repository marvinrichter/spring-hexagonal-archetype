# CLAUDE.md — spring-hexagonal-archetype

Maven archetype that generates a production-ready Spring Boot project wired for hexagonal (ports-and-adapters) architecture. ArchUnit rules enforced. Testcontainers integration test included. Micrometer + Actuator baked in.

This is also the "target state" referenced throughout the jvm-modernisation-playbook.

## Key commands

```bash
# Generate a project from the archetype (local install)
./mvnw install
mvn archetype:generate \
  -DarchetypeGroupId=io.github.marvinrichter \
  -DarchetypeArtifactId=spring-hexagonal-archetype \
  -DarchetypeVersion=1.1.2-SNAPSHOT

# Build and test the archetype itself
./mvnw verify

# In a generated project
./mvnw verify          # runs unit tests + ArchUnit + Testcontainers integration test
./mvnw spring-boot:run
```

## Structure

```
src/main/resources/
  archetype-resources/        — template files copied into generated projects
    src/main/java/
      domain/                 — pure Java, no framework dependencies
      application/            — use cases / ports (interfaces)
      adapter/
        in/web/               — REST controllers (inbound adapter)
        out/persistence/      — JPA adapter (outbound adapter)
    src/test/java/
      architecture/           — ArchUnit rules enforcing layer boundaries
      integration/            — Testcontainers PostgreSQL integration test
  META-INF/maven/
    archetype-metadata.xml    — archetype descriptor
```

## Architecture rules (ArchUnit)

Enforced in every generated project:

- `domain` has no imports from `adapter.*` or Spring framework
- `application` has no imports from `adapter.*`
- `adapter.in` does not import `adapter.out` and vice versa

These rules fail the build immediately if violated.

## Conventions

- Domain model is pure Java — no `@Entity`, no Spring annotations
- One complete vertical slice (Order domain) demonstrates the full pattern: REST → use case → domain → JPA
- No freelance or "available for hire" framing — OSS project, not a consulting pitch
- Do not add features beyond what's in the Acceptance Criteria — credibility depends on the example being clean and purposeful

## Relationship to jvm-modernisation-playbook

The generated project is the "after" state for all three modernisation patterns in the playbook. Keep the module structure stable and referenceable.
