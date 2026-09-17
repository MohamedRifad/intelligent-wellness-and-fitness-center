# Phase 5 End-to-End Integration Verification

## Scope

Phase 5 verifies the complete IWFC workflow through public `IWFCFacade` methods only and reviews the preventative-maintenance alert lifecycle. No console menu or production source file was added.

## Preventative-alert reset policy

The Batch 4 implementation permanently retained an equipment ID after its first preventative alert. That prevented duplicate alerts, but also prevented a valid alert in every later maintenance cycle.

The revised policy preserves cumulative usage hours and tracks a per-equipment next-due threshold:

1. The first preventative alert is due at 100 cumulative hours, inclusively.
2. Repeated automatic or manual checks in that cycle do not publish another alert.
3. Successful maintenance completion clears the active alert latch.
4. The next threshold becomes the equipment's cumulative usage at completion plus 100 hours.
5. Therefore completion at 100 hours makes the next alert due at 200 hours, not immediately after completion.

This policy avoids resetting the assignment-required cumulative usage value while supporting repeated maintenance cycles.

## Integration scenarios

`IWFCWorkflowIntegrationTest` contains six passing tests using only public Facade operations:

1. Complete workflow: initialize Administrator, register Instructor and Member, add equipment, schedule, book, record usage, report a fault, view and assign maintenance, verify assignment notification, complete maintenance, verify operational equipment, and verify completion notification.
2. Duplicate equipment rejection with `DuplicateDataException`.
3. Overlapping session rejection with `InvalidBookingException`.
4. Instructor and Member maintenance-log rejection with `UnauthorizedAccessException`.
5. Invalid maintenance transitions rejected with `IllegalStateException`.
6. Preventative cycle behavior: alert at 100 hours, no same-cycle duplicate, maintenance completion reset, no alert at 199.9 cumulative hours, new alert at 200 hours, and no duplicate in the second cycle.

`MaintenanceServiceTest` also contains a service-level regression test for the two-cycle threshold policy. This independently proves that completion starts a later 100-hour cycle while repeated checks remain idempotent.

## Class-count decision

The project remains at exactly 15 top-level production Java source files. The policy change was contained within the existing `MaintenanceService`; no production class, interface, enum, or record was added.

## Verification

Strict Java 21 compilation:

```text
STRICT_JAVA21_COMPILE_SUCCESS
Production source files: 15
```

Maven and JUnit:

```text
[INFO] Compiling 15 source files with javac [debug release 21] to target\classes
[INFO] Compiling 13 source files with javac [debug release 21] to target\test-classes
[INFO] Running iwfc.app.IWFCWorkflowIntegrationTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running iwfc.service.MaintenanceServiceTest
[INFO] Tests run: 18, Failures: 0, Errors: 0, Skipped: 0
[INFO] Tests run: 124, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## Files changed in Phase 5

Production file modified:

- `src/main/java/iwfc/service/MaintenanceService.java`

Test file added:

- `src/test/java/iwfc/app/IWFCWorkflowIntegrationTest.java`

Test file modified:

- `src/test/java/iwfc/service/MaintenanceServiceTest.java`

Documentation added:

- `docs/phase-05/README.md`

## Deferred

- Console menus and interactive input handling.
- Final report and presentation packaging.
- Repository submission preparation.
