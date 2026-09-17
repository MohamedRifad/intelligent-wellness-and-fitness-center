# Phase 4 Batch 2 Facade Administration

## Scope

This batch implements only Facade-level Administrator authorization, user-account administration, and equipment inventory administration. Equipment usage, scheduling, booking, maintenance-service workflows, Observer publishing, integration, and console menus remain deferred.

## Implemented operations

### Secure initialization

`initializeAdministrator(id, name)` creates the first Administrator through `EntityFactory`. It works only while the user repository is empty. This avoids hard-coded credentials while solving the initial authorization bootstrap problem.

### Authorization guard

Every administrative operation verifies that the actor:

1. Is not null.
2. Exists in the Facade's user repository.
3. Is the exact object stored for that account ID, preventing a fabricated Administrator object from impersonating it.
4. Has an active account.
5. Has the Administrator role.

Violations throw `UnauthorizedAccessException`.

### User administration

- Register Administrator, Instructor, or Member through `EntityFactory`.
- List registered users using an unmodifiable snapshot.
- Deactivate an existing account without deleting its history.
- Propagate `DuplicateDataException` from the generic repository.

### Equipment administration

- Add equipment through `EntityFactory`.
- Update name and location while preserving immutable ID.
- Deactivate equipment without deleting it.
- List equipment using an unmodifiable snapshot.
- Propagate `DuplicateDataException` for repeated equipment IDs.

## Design decisions

- All new users and equipment are created through the Factory inside the Facade.
- The Facade owns authorization because it is the application's use-case boundary.
- Unknown IDs and blank fields use `IllegalArgumentException`; the assignment's custom exceptions remain reserved for duplicate data, unauthorized access, and invalid booking.
- Self-deactivation is permitted. Once deactivated, that Administrator cannot perform further operations. A production authentication system would normally require a last-Administrator safeguard, but authentication and persistent account recovery are outside this prototype's scope.
- New users are registered with `MaintenanceService` as observers, but notification publishing is not implemented in this batch.

## JUnit coverage

`IWFCFacadeAdministrationTest` verifies:

- Initial Administrator creation.
- One-time initialization.
- Factory-backed creation of all three roles.
- Authorized user registration, listing, and deactivation.
- Unauthorized Instructor and Member attempts.
- Null, unregistered/fabricated, and inactive actors.
- Duplicate user and equipment IDs.
- Blank user and equipment input.
- Unknown user and equipment IDs.
- Authorized equipment add, update, list, and deactivate operations.
- Read-only user and equipment list views.

## Class count

No production source file or production type was added. The project remains at exactly 15 top-level production Java files. The previously documented nested-enum assumption remains unchanged.

## Deferred after Batch 2

- Equipment usage recording and maintenance-alert calculation.
- Operating-hours validation.
- Instructor, studio, and equipment schedule-conflict validation.
- Member booking and capacity limits.
- Maintenance reporting, assignment, and completion through services.
- Observer notification publishing and relevant-recipient filtering.
- Complete Facade workflow and console menus.

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
[INFO] Compiling 8 source files with javac [debug release 21] to target\test-classes
[INFO] Running iwfc.app.IWFCFacadeAdministrationTest
[INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0
[INFO] Tests run: 67, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
MAVEN_EXIT_CODE=0
```

## Files changed in this batch

Production file modified:

- `src/main/java/iwfc/app/IWFCFacade.java`

Test file added:

- `src/test/java/iwfc/app/IWFCFacadeAdministrationTest.java`

Documentation added:

- `docs/phase-04/batch-02.md`
