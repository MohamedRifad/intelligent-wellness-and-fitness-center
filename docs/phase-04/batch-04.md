# Phase 4 Batch 4 Maintenance, Usage, Alerts, and Observer Notifications

## Scope

This batch implements only maintenance reporting and progression, authorised session-equipment usage, preventative-maintenance alerts, and Observer delivery. It reuses the existing 15 production files. Console menus and the end-to-end integration test remain deferred.

## Maintenance workflow

### Fault reporting

`IWFCFacade.reportFault(...)` requires the exact active registered Instructor account. `MaintenanceService.reportFault(...)` validates the unique request ID, known active equipment, non-blank description, non-null urgency, and active Instructor reporter. A successful request starts as `PENDING`, changes the equipment to `FAULTY`, and publishes one new-fault notification to every active Administrator observer.

Duplicate maintenance IDs propagate `DuplicateDataException`. Invalid entity values and unknown IDs use `IllegalArgumentException`; a null urgency retains the domain's `NullPointerException` validation contract. Facade role, registration, identity, and active-account failures use `UnauthorizedAccessException`.

### Assignment and completion

Only an Administrator can call assignment, completion, or global maintenance-log listing through the Facade.

- `PENDING → ASSIGNED`: stores the validated assignee, changes equipment to `UNDER_MAINTENANCE`, and notifies the active reporting Instructor once.
- `ASSIGNED → COMPLETED`: completes the request, restores active equipment to `OPERATIONAL`, and notifies the active reporting Instructor once.
- Completion never changes the `active` flag. If equipment was administratively deactivated, it remains inactive and is not restored to operational service.
- Completing a pending request, reassigning an assigned request, or recompleting a completed request throws `IllegalStateException`.

## Equipment usage and preventative alerts

`IWFCFacade.recordSessionEquipmentUsage(...)` requires an exact active registered Instructor. The service additionally verifies that:

- the session exists and is active;
- the authenticated Instructor owns that session;
- the equipment exists and belongs to that session;
- usage is a positive finite number.

Valid usage accumulates in `Equipment`. At the inclusive 100-hour threshold, every active Administrator observer receives one preventative-maintenance message. A set of alerted equipment IDs latches the event, so automatic and repeated manual checks cannot duplicate the alert. At 99.9 hours no alert is published; adding 0.1 hours publishes it.

## Observer behavior

The existing `User` notification receiver and `MaintenanceService` observer registry now form a functional Observer implementation:

- Active Administrators receive new-fault and preventative-maintenance events.
- The active reporting Instructor receives assignment and completion events.
- Unrelated Instructors, Members, and inactive users receive nothing.
- Re-registering the same observer does not duplicate delivery.
- Multiple active Administrators each receive exactly one message per eligible event.

## JUnit coverage

`MaintenanceServiceTest` adds 17 tests covering fault creation and validation, duplicate IDs, equipment and reporter validation, all maintenance transitions, unknown requests, read-only request listing, active/deactivated equipment completion, authorised usage relationships, invalid numeric usage, unknown IDs, inactive sessions, the 99.9/100-hour boundary, duplicate alert prevention, multiple Administrator delivery, inactive-recipient filtering, unrelated-recipient filtering, and single-message delivery.

`IWFCFacadeMaintenanceTest` adds 10 tests covering Instructor fault reporting, exception propagation, exact registered actor validation, Administrator-only assignment/completion/log access, invalid transitions, deactivated equipment completion, authorised usage, Instructor session ownership, preventative checks, and role restrictions.

## Class count

The project remains at exactly 15 top-level production Java source files. No production class, interface, enum, or record was added. Existing nested enums remain supporting types within their original files.

## Explicitly deferred

- Console menus and interactive input handling.
- End-to-end integration/demo test.
- Report, presentation, and repository-submission packaging work scheduled for later phases.

## Verification

Strict Java 21 compilation:

```text
STRICT_JAVA21_COMPILE_SUCCESS
Production source files: 15
```

Maven and JUnit:

```text
[INFO] Compiling 15 source files with javac [debug release 21] to target\classes
[INFO] Compiling 12 source files with javac [debug release 21] to target\test-classes
[INFO] Running iwfc.app.IWFCFacadeMaintenanceTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running iwfc.service.MaintenanceServiceTest
[INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0
[INFO] Tests run: 117, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## Files changed in this batch

Production files modified:

- `src/main/java/iwfc/app/IWFCFacade.java`
- `src/main/java/iwfc/service/MaintenanceService.java`

Test files added:

- `src/test/java/iwfc/app/IWFCFacadeMaintenanceTest.java`
- `src/test/java/iwfc/service/MaintenanceServiceTest.java`

Documentation added:

- `docs/phase-04/batch-04.md`
