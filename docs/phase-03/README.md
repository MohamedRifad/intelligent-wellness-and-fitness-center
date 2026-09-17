# Phase 3 Project Foundation

## Purpose

This phase creates a clean, buildable foundation for IWFC. It establishes source control, Maven conventions, package boundaries, the approved 15 production classes, and the first automated test. It does not implement complete business workflows.

## Steps completed

1. Checked the local toolchain.
2. Initialized a Git repository on the `main` branch.
3. Added a Java 21 Maven project descriptor.
4. Added JUnit 5 and Surefire configuration.
5. Added `.gitignore` rules for generated and IDE files.
6. Created the five planned packages.
7. Created the 15 top-level production classes.
8. Added constructors, protected state, basic invariants, and dependency wiring.
9. Added a smoke test that creates the facade and verifies its dependencies.
10. Prepared README build, test, and run instructions.

## Toolchain result

- Installed Java runtime: Java 26.0.1.
- Project compilation target: Java 21.
- Git: available.
- Maven was not installed system-wide. Apache Maven 3.9.16 was downloaded from Apache into the ignored project-local `.tools` directory and its SHA-512 checksum was verified successfully.

The project targets Java 21 rather than Java 26 so it can run on a common long-term-support Java version, subject to lecturer approval.

## Class-count check

The production source tree contains exactly 15 `.java` files:

- 7 domain classes.
- 1 generic repository.
- 1 Factory.
- 2 services.
- 1 Facade/application entry point.
- 3 custom exceptions.

The smoke test is under `src/test/java` and is not a production class.

## Current implementation level

Already present:

- Abstract `User` and three polymorphic role subclasses.
- Encapsulated equipment, session, and maintenance entities.
- `HashMap`-based generic repository.
- Entity Factory.
- Facade dependency wiring.
- Observer registration foundation.
- Three mandatory custom exceptions.
- JUnit smoke test.

Still intentionally deferred:

- Administrator use cases.
- Complete equipment workflow.
- Session scheduling rules.
- Member booking rules.
- Maintenance transitions through the service.
- Observer message publishing.
- Console menus and seeded data.

## Verification commands

When Maven is available:

```shell
mvn clean test
```

The production source can also be syntax-checked directly with the installed JDK, although JUnit requires Maven to resolve the test dependency.

## Verified results

Direct Java 21 compilation and application startup:

```text
===== DIRECT JAVA 21 COMPILATION =====
JAVAC_EXIT_CODE=0
Intelligent Wellness and Fitness Center
Project foundation ready. Console workflows will be added in later phases.
APP_EXIT_CODE=0
```

Maven and JUnit:

```text
[INFO] Compiling 15 source files with javac [debug release 21] to target\classes
[INFO] Compiling 1 source file with javac [debug release 21] to target\test-classes
[INFO] Running iwfc.app.IWFCFacadeSmokeTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
MAVEN_EXIT_CODE=0
```

Three `serialVersionUID` warnings found by the first `-Xlint:all` run were corrected. The final direct compile used `--release 21 -Xlint:all -Werror` and exited successfully.

## Phase 3 gate

Phase 3 is complete because exactly 15 production source files compile using Java 21 compatibility, the application entry point starts, and the JUnit smoke test passes in a clean Maven build.

## Next phase

Phase 4 will implement and test the reusable foundation: generic repository behaviour, Factory creation, entity invariants, and custom exception basics. Feature workflows will follow in separate phases.
