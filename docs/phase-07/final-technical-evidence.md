# IWFC Final Technical Evidence Pack

## Purpose and audit conclusion

This evidence pack maps the completed Intelligent Wellness and Fitness Center prototype to the CMP 7001 PRAC1 and PRES1 briefs. The audit reviewed both supplied assessment documents, all 15 production Java files, all 14 JUnit test classes, Maven configuration, console behavior, phase documentation, and local Git state.

The software prototype is technically strong and demonstrably covers the mandatory equipment, scheduling, booking, maintenance, exception, design-pattern, OOP, console, and testing requirements. The latest verified result is 126 tests passing with zero failures, errors, or skipped tests under Java 21. No production defect requiring an audit-stage change was found.

Submission readiness is conditional. The 3000-word PRAC1 report, PRES1 slides/video, and final repository publication are still required. The local Git repository currently has no commit, no tracked files, and no configured GitHub or Bitbucket remote. Member wellness or schedule notifications and recurring weekly sessions are not implemented; the latter is explicitly optional, while the former appears in the actor description rather than the detailed mandatory feature list.

## Requirement traceability matrix

| Brief requirement | Implementation evidence | Test evidence | Audit status |
|---|---|---|---|
| Unified equipment, scheduling, and maintenance prototype | `IWFCFacade` coordinates repositories, `BookingService`, `MaintenanceService`, and console workflow | `IWFCWorkflowIntegrationTest.completesFullIwfcWorkflowUsingOnlyFacadeOperations` | Complete |
| Administrator manages user accounts | `initializeAdministrator`, `registerUser`, `viewUsers`, `deactivateUser` | `IWFCFacadeAdministrationTest` user-management and authorization tests | Complete |
| Administrator adds, edits, deactivates, and lists equipment | `addEquipment`, `updateEquipment`, `deactivateEquipment`, `viewEquipment` | `administratorAddsUpdatesListsAndDeactivatesEquipment`; duplicate, blank, unknown, and role tests | Complete |
| Equipment has unique ID, status, and location | `Equipment` fields and `Status`; `GenericRepository.add` uniqueness | `EquipmentTest`; `GenericRepositoryTest.rejectsDuplicateId`; Facade duplicate-equipment tests | Complete |
| Track cumulative usage hours | `Equipment.addUsageHours`; `MaintenanceService.recordSessionEquipmentUsage` | `accumulatesUsageHours`; authorized and invalid-usage tests | Complete |
| Preventative maintenance alerts | Inclusive 100-hour threshold, per-cycle latch, next threshold after completion | 99.9/100 boundary, duplicate suppression, and second-cycle tests in `MaintenanceServiceTest` and `IWFCWorkflowIntegrationTest` | Complete |
| Instructor schedules sessions | `IWFCFacade.scheduleSession` authenticates Instructor and delegates to `BookingService` | `registeredInstructorSchedulesAllRequiredValuesThroughFacade`; integration workflow | Complete |
| Validate operating hours | `BookingService.validateOperatingHours`, 06:00-22:00 inclusive | `acceptsInclusiveOpeningAndClosingBoundaries`; outside-hours and cross-day rejection | Complete |
| Prevent equipment or studio double-booking | Interval-overlap rule in `validateScheduleConflicts`; Instructor conflict is an additional safeguard | Conflict, concurrency, adjacency, and integration rejection tests | Complete |
| Reject unavailable equipment | Equipment must exist, be active, and be `OPERATIONAL` | Unknown, faulty, under-maintenance, and deactivated-equipment tests | Complete |
| Members view available sessions and book | `viewAvailableSessions`, `bookSession`, `viewMyBookings` | Successful booking, own-booking, inactive/full/unavailable filtering, and Facade-role tests | Complete |
| Prevent duplicate booking and enforce capacity | `FitnessSession` member set and `BookingService.bookSession` checks | `rejectsDuplicateBookingAndCapacityOverflow`; Facade capacity test | Complete |
| Recurring weekly sessions | No recurrence model or generator | Not applicable | Optional enhancement not implemented |
| Instructor reports faults | `IWFCFacade.reportFault`; `MaintenanceService.reportFault` | Service, Facade, and integration fault-report tests | Complete |
| Maintenance request attributes | `MaintenanceRequest`: equipment ID, description, `Urgency`, `Status`, reporter, assignee, timestamps | Constructor, validation, status, urgency, and audit-information tests | Complete |
| Administrator assigns tasks and updates progress | Administrator-only `assignMaintenance` and `completeMaintenance` | Authorization, successful flow, invalid-transition, and integration tests | Complete |
| Valid maintenance status sequence | Domain guards enforce `PENDING -> ASSIGNED -> COMPLETED` | Direct domain, service, Facade, and integration `assertThrows` tests | Complete |
| Notify when maintenance status changes | Observer delivery to active reporting Instructor on assignment/completion | Recipient, inactive-user, one-message-per-event, and integration-notification tests | Complete |
| Member receives wellness or schedule notifications | `User` can receive notifications, but no Member schedule/wellness event is published | No Member-positive notification test; tests prove unrelated Members receive no maintenance event | Limitation in actor description |
| 10-15 well-structured production classes | Exactly 15 top-level production `.java` files across domain, services, repository, patterns, exceptions, and application packages | Strict compile and Maven both report 15 production sources | Complete |
| Java Collections | `ArrayList`, `HashMap`, `HashSet`, `List`, `Map`, `Set`, streams, read-only views | Domain, repository, service, and read-only-result tests | Complete |
| Generics | `GenericRepository<T>` and typed repositories for four entity categories | `GenericRepositoryTest.genericImplementationSupportsDifferentEntityTypes` | Complete |
| Abstraction, inheritance, encapsulation, polymorphism | Abstract `User`, three subclasses, private state, validated behaviors, overridden `getRoleDescription` | `UserTest.exposesPolymorphicRoleInformation` and entity validation tests | Complete |
| Creational pattern | `EntityFactory` creates every role and equipment; Facade uses it for creation | `EntityFactoryTest`; Facade role-registration tests | Complete |
| Structural pattern | `IWFCFacade` exposes use cases while hiding repository/service coordination | Administration, scheduling, maintenance, integration, and console tests | Complete |
| Behavioural pattern | `MaintenanceService` registers `User` observers and publishes filtered events | Observer filtering, multiple Administrators, inactive users, reporter updates, and duplicate-delivery tests | Complete |
| Invalid-booking custom exception | `InvalidBookingException` covers hours, conflicts, capacity, duplicates, inactive sessions, and invalid resources | Multiple `assertThrows(InvalidBookingException.class, ...)` cases | Complete |
| Unauthorized-access custom exception | `IWFCFacade.requireRole` validates identity, registration, active state, and role; usage ownership also guarded | Administration, scheduling, maintenance, and integration unauthorized tests | Complete |
| Duplicate-data custom exception | `GenericRepository` rejects duplicate IDs; Facade/services propagate it | Duplicate user, equipment, session, request, and integration tests | Complete |
| JUnit scheduling and maintenance validation | JUnit Jupiter 5.11.4 with 14 test classes | 126 tests, zero failures/errors/skips | Complete |
| Intentional exception error condition | Negative tests deliberately trigger error conditions and pass only when `assertThrows` receives the correct exception | Examples in booking, authorization, repository, maintenance, and integration tests | Complete; do not submit a deliberately red test suite |
| Console menu minimum | `IWFCFacade.main` provides setup, guided workflow, validation, recovery, and exit | `IWFCFacadeConsoleTest`; successful scripted console launch | Complete |
| Database not required | Typed in-memory repositories store prototype data | All unit/integration tests run without external infrastructure | Conforms |
| GitHub or Bitbucket source control | Local `.git` exists on `main` | Audit: zero commits, zero tracked files, no remote | Not submission-ready |
| PRAC1 report | Phase documents and this pack provide evidence for the report | Not a software test | 3000-word formatted PDF/Word submission still required |
| PRES1 presentation | Console demo, class diagram, tests, and this evidence support the presentation | Console launch and test evidence available | Slides, 10-minute video, reflection, and first-slide link still required |

## Final 15-class architecture

The count is based on top-level production Java source files. Nested enums are supporting value types inside their owning classes and do not add production source files.

| Package | Class | Responsibility |
|---|---|---|
| `iwfc.app` | `IWFCFacade` | Structural Facade, role/identity guard, use-case API, and console entry point |
| `iwfc.domain` | `User` | Abstract encapsulated user and notification receiver |
| `iwfc.domain` | `Administrator` | Administrator specialization and polymorphic role description |
| `iwfc.domain` | `Instructor` | Instructor specialization and polymorphic role description |
| `iwfc.domain` | `Member` | Member specialization and polymorphic role description |
| `iwfc.domain` | `Equipment` | Equipment identity, mutable details, lifecycle status, usage, and threshold behavior |
| `iwfc.domain` | `FitnessSession` | Session schedule, resources, overlap behavior, capacity, and bookings |
| `iwfc.domain` | `MaintenanceRequest` | Fault data, timestamps, assignee, and guarded maintenance state machine |
| `iwfc.repository` | `GenericRepository<T>` | Reusable typed in-memory storage, ID lookup, read-only snapshots, and duplicate rejection |
| `iwfc.pattern` | `EntityFactory` | Centralized creation of all user roles and equipment |
| `iwfc.service` | `BookingService` | Scheduling, operating hours, resource conflicts, availability, and member booking logic |
| `iwfc.service` | `MaintenanceService` | Fault workflow, usage authorization, preventative cycles, and Observer publishing |
| `iwfc.exception` | `InvalidBookingException` | Checked exception for scheduling and booking rule failures |
| `iwfc.exception` | `UnauthorizedAccessException` | Checked exception for role, identity, registration, active-account, and ownership violations |
| `iwfc.exception` | `DuplicateDataException` | Checked exception for uniqueness violations |

## Design pattern justification

### Factory

`EntityFactory` is the creational pattern. Its `createUser` switch returns an `Administrator`, `Instructor`, or `Member` behind the abstract `User` type, and `createEquipment` creates validated equipment. `IWFCFacade` uses the Factory for initial Administrator setup, later user registration, and equipment addition. This prevents console or client code from scattering concrete-constructor decisions and provides a single creation boundary tested for all roles.

### Facade

`IWFCFacade` is the structural pattern and the application boundary. A caller invokes task-oriented methods such as `scheduleSession`, `bookSession`, `reportFault`, or `completeMaintenance`; the Facade authenticates the exact registered actor, enforces the required role, then coordinates repositories and services. The Phase 5 integration test proves the entire workflow through public Facade methods only. The console likewise uses only the public Facade API.

### Observer

`MaintenanceService` is the behavioural subject/publisher. Registered `User` objects are observers with `receiveNotification` behavior. New faults and preventative thresholds are delivered to every active Administrator; assignment and completion events are delivered to the active reporting Instructor. Filtering prevents unrelated or inactive users from receiving an event, duplicate registration does not duplicate delivery, and the per-cycle preventative latch ensures one alert per event cycle.

## Object-oriented and advanced programming evidence

### Encapsulation

Entity fields are private. Constructors and behavior methods validate input and protect invariants. IDs and immutable session data have no setters. Mutable collections are returned as unmodifiable views or snapshots. Equipment state changes through named operations such as `markFaulty`, `markUnderMaintenance`, and `markOperational`; maintenance transitions occur through `assignTo` and `complete` rather than direct status assignment.

### Abstraction and inheritance

`User` is abstract and defines common identity, role, active-state, and notification behavior. `Administrator`, `Instructor`, and `Member` inherit this state and implement the abstract role-description operation. This keeps shared rules in one reusable base abstraction.

### Polymorphism

The system stores and passes all three concrete roles as `User` references. `EntityFactory.createUser` returns `User`, repositories use `GenericRepository<User>`, Observer delivery operates on `List<User>`, and `getRoleDescription` dispatches to each subclass implementation. `UserTest.exposesPolymorphicRoleInformation` is the direct proof.

### Collections

The implementation uses `HashMap` for repository storage and alert thresholds, `ArrayList` for snapshots/notifications/observers/equipment IDs, and `HashSet` for unique bookings, equipment IDs, and alert latches. Streams filter available sessions, personal bookings, and Observer recipients.

### Generics

`GenericRepository<T>` stores any entity type while preserving compile-time type safety. The Facade owns repositories parameterized as `User`, `Equipment`, `FitnessSession`, and `MaintenanceRequest`. An injected `Function<T, String>` extracts entity IDs without duplicating repository classes.

## Exception-handling evidence

| Exception | Trigger examples | Handling boundary |
|---|---|---|
| `InvalidBookingException` | Outside 06:00-22:00, overlapping Instructor/studio/equipment, unknown or unavailable equipment, full session, duplicate Member booking | Thrown by `BookingService`, propagated by `IWFCFacade`, displayed safely by console |
| `UnauthorizedAccessException` | Wrong role, null actor, fabricated actor with reused ID, unregistered or inactive account, non-owner usage attempt | Central `IWFCFacade.requireRole` plus service ownership guard |
| `DuplicateDataException` | Duplicate user, equipment, session, maintenance request, or second application initialization | `GenericRepository.add` or Facade initialization, propagated to caller |
| `IllegalStateException` | Complete a pending request, reassign an assigned request, or complete twice | `MaintenanceRequest` state machine, propagated through service/Facade |
| `IllegalArgumentException` | Blank entity fields, unknown IDs outside booking semantics, invalid numeric usage, unassigned session equipment | Domain/service validation |

The console catches workflow exceptions, prints a clear error, and returns to its menu. Unit tests use `assertThrows` to prove that invalid conditions fail with the expected type while the overall suite remains green.

## Testing evidence

The Maven Surefire reports contain 14 suites with 126 tests, 0 failures, 0 errors, and 0 skipped tests.

| Area | Test class | Tests | Evidence focus |
|---|---|---:|---|
| Administration | `IWFCFacadeAdministrationTest` | 19 | Authorization, users, equipment, duplicates, blanks, unknown IDs, read-only views |
| Console | `IWFCFacadeConsoleTest` | 2 | Successful demo, invalid input, repeated-demo error recovery, end-of-input |
| Maintenance Facade | `IWFCFacadeMaintenanceTest` | 10 | Fault reporting, admin workflow, role guards, usage, alert threshold |
| Scheduling Facade | `IWFCFacadeSchedulingTest` | 8 | Complete inputs, role guards, booking, capacity, equipment availability |
| Smoke | `IWFCFacadeSmokeTest` | 1 | Application foundation construction |
| Integration | `IWFCWorkflowIntegrationTest` | 6 | Complete workflow and four rejected paths plus second alert cycle |
| Equipment domain | `EquipmentTest` | 14 | State, updates, usage, numeric validation, inclusive threshold |
| Session domain | `FitnessSessionTest` | 9 | Validation, defensive copy, overlap, adjacency, capacity |
| Maintenance domain | `MaintenanceRequestTest` | 6 | Audit fields and state transitions |
| User domain | `UserTest` | 5 | Polymorphism, identity, activation, notifications |
| Factory | `EntityFactoryTest` | 6 | Three roles, equipment, validation |
| Repository | `GenericRepositoryTest` | 7 | Generic reuse, duplicates, lookup, immutable snapshots |
| Booking service | `BookingServiceTest` | 15 | Boundaries, conflicts, availability, bookings, capacity |
| Maintenance service | `MaintenanceServiceTest` | 18 | Workflow, observers, usage, 99.9/100 boundary, alert reset cycle |

Key boundaries include 06:00 and 22:00 inclusive, adjacent versus overlapping session intervals, capacity at the final place, usage values of 99.9 and 100 hours, inactive versus active accounts/equipment, and `PENDING -> ASSIGNED -> COMPLETED`. The integration suite proves a successful full workflow and rejected duplicate equipment, conflicting session, unauthorized log access, and invalid transition scenarios.

## Console demonstration

Run the tests from PowerShell:

```powershell
.\.tools\maven\apache-maven-3.9.16\bin\mvn.cmd --batch-mode clean test
```

Build and launch the console:

```powershell
.\.tools\maven\apache-maven-3.9.16\bin\mvn.cmd --batch-mode -DskipTests package
java -cp target\iwfc-management-system-1.0.0-SNAPSHOT.jar iwfc.app.IWFCFacade
```

At launch, enter an Administrator ID and name, choose option `1`, and narrate the numbered steps. The demo registers an Instructor and Member, adds a Spin Bike, schedules Morning Spin, books the Member, records usage, reports a fault, assigns it, displays the Instructor notification, completes maintenance, and displays the final `OPERATIONAL` equipment and `COMPLETED` request. Choose option `0` to exit.

## Limitations and assumptions

- Persistence is intentionally in memory, as allowed by the brief. Data is lost when the process ends.
- Authentication is represented by exact registered object identity, active state, and role; passwords, sessions, encryption, and external identity providers are outside the prototype.
- Recurring weekly sessions are not implemented because the brief labels them optional, although they would strengthen a distinction-level extension discussion.
- Member wellness or schedule notification publishing is not implemented. The notification mechanism exists, but current event rules intentionally target maintenance-relevant users.
- Maintenance assignees are validated non-blank text rather than modeled technician accounts.
- The application is single-process and not thread-safe; simultaneous real-world booking would require transactional persistence and concurrency control.
- Dates use local Java date/time values without timezone or daylight-saving policy.
- Preventative alert thresholds and latches are held in memory. Completion starts the next threshold at current cumulative hours plus 100.
- The console is a guided presentation workflow, not a full CRUD shell. Business operations remain available through the Facade and fully tested.

Realistic future enhancements include persistent storage, credential-based authentication, transaction-safe concurrent booking, recurring-session generation, Member schedule reminders, technician accounts, configurable operating hours and maintenance thresholds, audit logging, and a GUI or web API.

## Git and repository readiness

### Confirmed ready

- A local Git repository exists on branch `main`.
- `.gitignore` excludes `target/`, `.tools/`, IDE folders, `*.class`, and `*.log`.
- `git check-ignore` confirms that Maven output, compiled classes, and the bundled Maven runtime remain ignored.
- No generated build artifacts need to be committed.
- Maven configuration targets Java 21 and declares JUnit Jupiter 5.11.4.

### Required before submission

- The repository currently has zero commits and zero tracked files.
- No GitHub or Bitbucket remote is configured.
- Add the intended source, tests, documentation, `pom.xml`, `.gitignore`, and README; review whether the supplied assignment briefs belong in the submitted repository; create meaningful commits; create the remote repository; push; and verify the remote contents from a clean clone.
- Do not add `.tools/` or `target/`.

## PRAC1 and PRES1 readiness

### PRAC1

The Java prototype itself is ready: functional requirements, 15-file architecture, three pattern categories, OOP, collections, generics, custom exceptions, JUnit evidence, and console minimum are demonstrated. PRAC1 is not yet submission-ready because the mandatory GitHub/Bitbucket history is absent and the 3000-word report still needs to be authored, formatted to the brief, referenced in Harvard style, exported to PDF for Moodle/Turnitin, and supplied in Word format where required by the ICBT instructions.

### PRES1

The technical demonstration material is ready: class diagram, pattern workflows, polymorphism evidence, exception examples, test results, bug-fix narrative, and running console are all available. PRES1 is not yet submission-ready because the PowerPoint/equivalent, approximately 10-minute narrated video, reflective discussion, tested audio/screen capture, hosted video, and first-slide video link still need to be produced. The brief specifies PDF submission naming based on student ID, module code, and assessment ID, with Word-format submission also identified in the ICBT instructions.
