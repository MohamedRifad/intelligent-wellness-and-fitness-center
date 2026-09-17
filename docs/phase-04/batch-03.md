# Phase 4 Batch 3 Session Scheduling and Member Booking

## Scope

This batch implements session scheduling and member booking only. It uses the existing `IWFCFacade`, `BookingService`, domain entities, repositories, and custom exceptions. No production source file or production type was added.

## Implemented workflow

### Instructor scheduling

`IWFCFacade.scheduleSession(...)` is the application boundary. It receives the authenticated Instructor plus the session ID, title, start and end date/time, studio, equipment IDs, and capacity. The Facade derives the instructor ID from the authenticated actor, so callers cannot schedule a session under another instructor's identity. `BookingService` then validates and stores the session.

The actor must be the exact active Instructor object registered in the Facade. Null, wrong-role, fabricated, unregistered, and inactive actors are rejected with `UnauthorizedAccessException`.

### Scheduling rules

- Operating hours are 06:00 to 22:00. A session may start exactly at 06:00 or finish exactly at 22:00.
- End time must be later than start time, and both must be on the same date.
- Required text, date/time, equipment collection, and positive-capacity values are validated.
- Active sessions conflict when `candidate.start < existing.end` and `candidate.end > existing.start`.
- Overlapping sessions are rejected if they share an Instructor, studio (case-insensitive), or equipment ID.
- End-to-start adjacency is allowed because it does not overlap.
- Every referenced equipment ID must exist, remain active, and have `OPERATIONAL` status.
- Unknown, faulty, under-maintenance, and deactivated equipment is rejected.
- Duplicate session IDs alone use `DuplicateDataException`; all other scheduling failures use `InvalidBookingException`.

### Member booking

`IWFCFacade.bookSession(...)`, `viewAvailableSessions(...)`, and `viewMyBookings(...)` require the exact active registered Member account. The Facade delegates workflow logic to `BookingService`; the console does not construct sessions or manipulate repositories.

- A Member can book an active known session with available capacity.
- Duplicate bookings and bookings beyond capacity are rejected.
- Unknown or blank session IDs are rejected.
- A session cannot be booked if its required equipment has since become unavailable.
- Available-session results include only active, non-full sessions whose equipment is still available.
- A Member's booking list contains only sessions booked by that Member.
- Returned query results are read-only snapshots.
- Booking failures use `InvalidBookingException`; Facade authentication/authorization failures use `UnauthorizedAccessException`.

## JUnit coverage

`BookingServiceTest` contains 15 tests covering inclusive operating-hour boundaries, invalid and cross-day times, all required values, duplicate IDs, Instructor/studio/equipment conflicts, adjacent and independent concurrent sessions, inactive-session conflict handling, all rejected equipment states, Instructor validation, successful Member booking, own-booking queries, duplicate booking, capacity, actor/session failures, booking-time equipment state, and available-session filtering.

`IWFCFacadeSchedulingTest` contains 8 tests covering the complete Facade inputs, authenticated Instructor identity, exception propagation, wrong-role/null/fabricated/inactive actors, successful Member discovery and booking, duplicate and capacity failures, Member access guards, deactivated equipment, and booking-time faulty equipment.

The suite contains multiple explicit `assertThrows(InvalidBookingException.class, ...)` assertions, including the required intentional error-condition verification. These are passing negative tests, not deliberately broken tests.

## Design decisions and limitations

- The session's Instructor is the authenticated Facade actor; there is no separate caller-supplied Instructor ID that could be forged.
- Studio comparison is case-insensitive, while entity IDs remain case-sensitive and exact.
- A session may use zero or more equipment units; when IDs are supplied, every ID must be unique and available.
- Session cancellation/reactivation is not exposed in this batch. The domain's existing deactivation behavior is used in service-level tests.
- In-memory repositories remain the required prototype persistence mechanism.

## Class count

The project remains at exactly 15 top-level production Java source files. Nested enums continue to be treated as supporting types inside existing production files, as documented in the Phase 4 blueprint.

## Explicitly deferred after Batch 3

- Maintenance reporting, task assignment, and workflow transitions.
- Observer notification publishing and relevant-recipient selection.
- Equipment usage recording and preventative-maintenance alerts.
- Console menus.
- End-to-end integration/demo workflow.

## Verification

Strict Java 21 compilation:

```text
STRICT_JAVA21_COMPILE_SUCCESS
Production source files: 15
```

Maven and JUnit:

```text
[INFO] Compiling 15 source files with javac [debug release 21] to target\classes
[INFO] Compiling 10 source files with javac [debug release 21] to target\test-classes
[INFO] Running iwfc.app.IWFCFacadeSchedulingTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running iwfc.service.BookingServiceTest
[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
[INFO] Tests run: 90, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## Files changed in this batch

Production files modified:

- `src/main/java/iwfc/app/IWFCFacade.java`
- `src/main/java/iwfc/service/BookingService.java`

Test files added:

- `src/test/java/iwfc/app/IWFCFacadeSchedulingTest.java`
- `src/test/java/iwfc/service/BookingServiceTest.java`

Documentation added:

- `docs/phase-04/batch-03.md`
