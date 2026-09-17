# Phase 4 Batch 1 Foundation Behaviour

## Scope

This batch implements and tests only the reusable repository, Factory, and domain-entity behaviour. It does not implement authorization, equipment administration through the Facade, scheduling services, booking services, maintenance-service workflows, Observer publishing, or console menus.

## Class-count assumption

The assignment requires 10–15 well-structured classes but does not state whether nested enums are included. This project uses exactly 15 top-level production `.java` files. Four enums are nested inside existing owning classes:

- `User.Role`
- `Equipment.Status`
- `MaintenanceRequest.Urgency`
- `MaintenanceRequest.Status`

The nested enums are supporting value types, not additional top-level production files or independent service/entity responsibilities. This interpretation must be confirmed with the lecturer. No production file or type was added in this batch.

## Implemented behaviour

### Generic repository

- Stores generic entity types in a `HashMap` using an injected ID extractor.
- Rejects null items and null or blank extracted IDs.
- Rejects duplicate IDs with `DuplicateDataException`.
- Returns optional lookup results and an unmodifiable snapshot from `findAll()`.

### Entity Factory

- Creates Administrator, Instructor, and Member objects from `User.Role`.
- Creates new Operational equipment.
- Delegates field validation to domain constructors.
- Produces a clear error for a missing role.

### User hierarchy

- Validates user ID, name, and role.
- Demonstrates inheritance and polymorphic role descriptions.
- Supports account deactivation.
- Stores trimmed notifications behind an unmodifiable view.

### Equipment

- Validates identity, name, and location.
- Preserves immutable ID while allowing controlled detail updates.
- Accumulates only positive finite usage hours.
- Validates and applies an inclusive maintenance threshold.
- Calculates scheduling availability from active state and operational status.

### Fitness session

- Validates required fields, chronological times, and positive capacity.
- Defensively copies equipment IDs and protects booking collections.
- Rejects blank or duplicate equipment IDs within a session.
- Detects overlapping intervals while allowing adjacent intervals.
- Tracks unique Member IDs and capacity and supports deactivation.

### Maintenance request

- Starts in Pending with timestamps.
- Permits Pending to Assigned to Completed.
- Rejects skipped or repeated transitions.
- Validates required identifiers, description, urgency, reporter, and assignee.

## Tests added

- `UserTest`
- `EquipmentTest`
- `FitnessSessionTest`
- `MaintenanceRequestTest`
- `GenericRepositoryTest`
- `EntityFactoryTest`

The existing `IWFCFacadeSmokeTest` remains in the suite.

## Deferred to Batch 2

- Administrator authorization guards.
- User-account administration through the Facade.
- Equipment add, update, deactivate, list, and usage operations through the Facade.
- Facade-level `UnauthorizedAccessException` and `DuplicateDataException` tests.

All scheduling, booking, maintenance-service, Observer-publishing, integration, and console work remains deferred beyond Batch 2.

## Verification

Strict Java 21 compilation:

```text
PRODUCTION_SOURCE_COUNT=15
===== STRICT JAVA 21 COMPILATION =====
JAVAC_EXIT_CODE=0
```

Maven and JUnit:

```text
[INFO] Compiling 15 source files with javac [debug release 21] to target\classes
[INFO] Compiling 7 source files with javac [debug release 21] to target\test-classes
[INFO] Tests run: 48, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
MAVEN_EXIT_CODE=0
```

## Files changed in this batch

Production files modified:

- `src/main/java/iwfc/domain/Equipment.java`
- `src/main/java/iwfc/domain/FitnessSession.java`
- `src/main/java/iwfc/pattern/EntityFactory.java`
- `src/main/java/iwfc/repository/GenericRepository.java`

Test files added:

- `src/test/java/iwfc/domain/UserTest.java`
- `src/test/java/iwfc/domain/EquipmentTest.java`
- `src/test/java/iwfc/domain/FitnessSessionTest.java`
- `src/test/java/iwfc/domain/MaintenanceRequestTest.java`
- `src/test/java/iwfc/pattern/EntityFactoryTest.java`
- `src/test/java/iwfc/repository/GenericRepositoryTest.java`

Documentation added:

- `docs/phase-04/batch-01.md`
