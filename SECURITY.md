# Security Policy

## Supported versions

| Version | Supported |
|---------|-----------|
| 1.x     | ✅        |

## Reporting a vulnerability

Please **do not** open a public GitHub issue for security vulnerabilities.

Report security issues by email to **security@marvin-richter.de** with:
- A description of the vulnerability
- Steps to reproduce or a proof-of-concept
- The version affected

You will receive a response within 72 hours. If the issue is confirmed,
a fix will be released as quickly as possible and credited to you in the changelog
(unless you prefer to remain anonymous).

## Scope

This project is a Maven archetype — a code generator. The primary security
surface is the **code it generates**, not the archetype machinery itself.

If you find that the generated project introduces a security anti-pattern
(e.g. insecure defaults, hardcoded credentials, missing input validation),
that is in scope and worth reporting.
