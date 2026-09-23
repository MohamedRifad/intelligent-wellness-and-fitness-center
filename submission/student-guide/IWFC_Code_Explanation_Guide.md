# IWFC Code Explanation Guide

## A student friendly guide to the finished Java prototype

This guide explains how the Intelligent Wellness and Fitness Center works, how its 15 production classes cooperate, and what the JUnit tests prove. Use it to prepare for the PRES1 code walkthrough and lecturer questions. The simplest overall explanation is: **the console calls the Facade, the Facade checks authority and delegates work, services enforce workflows, domain objects protect their own state, and generic repositories store the objects in memory.**

## 1 Package and class connection diagram

```text
Console / presenter
       |
       v
iwfc.app
  IWFCFacade  -------------------->  iwfc.pattern.EntityFactory
       |                                      |
       |                                      v
       |                             User subclasses + Equipment
       |
       +-------------------------->  iwfc.service.BookingService
       |                                      |
       |                                      v
       |                             FitnessSession + Equipment
       |
       +-------------------------->  iwfc.service.MaintenanceService
                                              |
                                              v
                           Equipment + MaintenanceRequest + User observers

All application layers use iwfc.repository.GenericRepository<T>
to store User, Equipment, FitnessSession and MaintenanceRequest objects.

Failures travel back through iwfc.exception:
InvalidBookingException | UnauthorizedAccessException | DuplicateDataException
```

**How to explain the diagram orally:** “IWFCFacade is the centre of the application. The console and tests use its public methods. It calls the Factory when it needs a new object and delegates booking and maintenance rules to two services. Those services work with domain objects stored in typed generic repositories. Exceptions return clear failure reasons to the caller.”

## 2 The 15 production classes

### 2.1 iwfc.app.IWFCFacade

**Responsibility.** This is the application boundary, Structural Facade and console entry point. It offers business-level operations instead of exposing repositories or services to the console.

**Important fields.** Four typed repositories store `User`, `Equipment`, `FitnessSession` and `MaintenanceRequest`. The class also owns one `EntityFactory`, one `BookingService` configured for 06:00-22:00, and one `MaintenanceService`.

**Important methods.** `initializeAdministrator` performs one-time setup. `registerUser`, `viewUsers` and `deactivateUser` manage accounts. `addEquipment`, `updateEquipment`, `deactivateEquipment` and `viewEquipment` manage inventory. `scheduleSession`, `bookSession`, `viewAvailableSessions` and `viewMyBookings` expose booking use cases. Maintenance methods report, assign, complete and list requests, record usage, and check preventative maintenance. `requireRole` is the central authorization guard. `main`, `runConsole` and `runGuidedDemonstration` provide the presentation console.

**Calls and callers.** The console, integration tests and Facade tests call it. It calls `EntityFactory`, both services and all four repositories. It also invokes entity behaviour such as `User.deactivate` and `Equipment.updateDetails`.

**Business logic.** The Facade accepts only the exact registered object, checks that the account is active, and verifies the required role. An object with the same ID but a different identity is rejected. The first Administrator can be created only while the user repository is empty. All later creation requires an active Administrator.

**Presentation wording.** “The Facade gives the console one safe API. It authenticates the exact registered actor, checks role and active state, and then coordinates the correct service and repository. This keeps presentation code away from business internals.”

### 2.2 iwfc.domain.User

**Responsibility.** Abstract base class for every IWFC user and the receiving side of the Observer workflow.

**Important fields and methods.** It stores immutable `id`, `name` and `role`, plus mutable `active` state and an internal notification list. `deactivate` disables the account. `receiveNotification` validates and stores a message. `getNotifications` returns an unmodifiable view. `getRoleDescription` is abstract and implemented differently by each role.

**Calls and callers.** `EntityFactory` creates concrete subclasses. The Facade checks User identity, activity and role. `MaintenanceService` registers User objects as observers and calls `receiveNotification`.

**Business logic.** Blank identity data is rejected. Notifications cannot be blank, and external code cannot edit the returned notification list. The shared abstraction lets repositories and services work with all roles as `User`.

**Presentation wording.** “User holds the shared identity, account and notification behaviour. Polymorphism appears because each concrete role overrides the same role-description method while the application stores all roles through the User type.”

### 2.3 iwfc.domain.Administrator

**Responsibility.** Concrete User role for account, equipment and maintenance administration.

**Important method.** The constructor fixes the role to `ADMINISTRATOR`; `getRoleDescription` returns the Administrator-specific description.

**Calls and callers.** `EntityFactory.createUser` creates it. The Facade accepts it for Administrator-only methods. Maintenance notifications target active Administrator observers.

**Business logic.** It gains no unsafe public setter or repository access. Authority still comes from the Facade’s identity, active-account and role checks.

**Presentation wording.** “Administrator is deliberately small because shared behaviour belongs in User and permissions belong at the Facade boundary.”

### 2.4 iwfc.domain.Instructor

**Responsibility.** Concrete User role for scheduling, usage recording and fault reporting.

**Important method.** The constructor fixes the role to `INSTRUCTOR`; `getRoleDescription` describes its duties.

**Calls and callers.** The Factory creates it. The Facade authorizes it for scheduling, usage and reporting. `MaintenanceService` notifies the reporting Instructor when a request is assigned or completed.

**Business logic.** Only the Instructor who owns a session may record usage for that session.

**Presentation wording.** “Instructor inherits common User behaviour but participates in different workflows. Ownership checks stop another Instructor recording usage against someone else’s session.”

### 2.5 iwfc.domain.Member

**Responsibility.** Concrete User role for viewing availability and booking sessions.

**Important method.** The constructor fixes the role to `MEMBER`; `getRoleDescription` describes booking access.

**Calls and callers.** The Factory creates it. The Facade authorizes Member booking and viewing methods. `BookingService` validates that the stored account is active and has the Member role.

**Business logic.** Members cannot use Administrator or Instructor operations. Duplicate booking and capacity checks occur in the booking workflow.

**Presentation wording.** “Member shows role-based polymorphism and least privilege: it can view and book sessions but cannot manage inventory or maintenance.”

### 2.6 iwfc.domain.Equipment

**Responsibility.** Represents one physical equipment item and protects its inventory, status and usage state.

**Important fields and methods.** It stores immutable `id`, mutable `name`, `location`, `status`, cumulative usage hours and active state. Status values are `OPERATIONAL`, `FAULTY` and `UNDER_MAINTENANCE`. `updateDetails`, `deactivate`, `addUsageHours`, `requiresPreventativeMaintenance`, `isAvailableForScheduling`, and the three named status methods implement behaviour.

**Calls and callers.** The Factory creates it. The Facade manages its inventory details. `BookingService` checks scheduling availability. `MaintenanceService` records usage and changes status during fault and maintenance workflows.

**Business logic.** Usage must be positive and finite. The maintenance comparison is inclusive, so 100.0 hours satisfies a 100-hour threshold. Scheduling requires both active state and `OPERATIONAL` status. Deactivation and mechanical status remain separate concepts.

**Presentation wording.** “Equipment combines inventory and operational state. A bike can be active in inventory but faulty, or administratively deactivated. Scheduling accepts it only when both conditions are safe.”

### 2.7 iwfc.domain.FitnessSession

**Responsibility.** Holds a scheduled class, its resources, capacity and Member bookings.

**Important fields and methods.** Immutable session data includes ID, title, Instructor ID, studio, start/end times, capacity and equipment IDs. A `HashSet` stores unique booked Member IDs. `overlaps` uses `start < otherEnd && end > otherStart`. `usesStudio`, `usesAnyEquipment`, `hasCapacity`, `containsMember`, `addMember` and `deactivate` support service rules.

**Calls and callers.** `BookingService` constructs and queries it. `MaintenanceService` uses it to validate usage ownership and equipment assignment. The Facade returns it to callers.

**Business logic.** End time must be after start time, capacity must be positive, and equipment IDs inside one session must be unique. Adjacent sessions do not overlap. Collections are defensively copied and exposed read-only.

**Presentation wording.** “FitnessSession owns facts about one class and supplies small rule methods. The service combines those methods to detect instructor, studio and equipment conflicts.”

### 2.8 iwfc.domain.MaintenanceRequest

**Responsibility.** Represents a reported fault and enforces the maintenance state machine.

**Important fields and methods.** It stores request/equipment IDs, description, urgency, status, reporting Instructor, assignee, and created/updated timestamps. Urgency is `LOW`, `MEDIUM` or `HIGH`; status is `PENDING`, `ASSIGNED` or `COMPLETED`. `assignTo` and `complete` are the only status-changing methods.

**Calls and callers.** `MaintenanceService.reportFault` creates it, then assignment and completion methods call its guarded transitions. The repository stores it and the Facade exposes the log to Administrators.

**Business logic.** New requests start Pending. Only Pending can become Assigned, and only Assigned can become Completed. Invalid transitions throw `IllegalStateException`; blank identity, description, reporter or assignee values are rejected.

**Presentation wording.** “The request is a small state machine. Callers cannot set status directly, so the domain object prevents completion before assignment and repeated transitions.”

### 2.9 iwfc.repository.GenericRepository<T>

**Responsibility.** Reusable, type-safe in-memory storage for any entity type with a String ID.

**Important fields and methods.** A `Function<T,String>` extracts the ID and a `LinkedHashMap` stores objects. `add` rejects duplicate IDs. `findById` returns `Optional<T>`, `containsId` checks existence, and `findAll` returns an unmodifiable snapshot.

**Calls and callers.** The Facade creates four typed instances. Services use the repositories to look up and list entities.

**Business logic.** Null items, missing IDs and blank lookup IDs are rejected. The snapshot cannot be modified and does not change when later items are added.

**Presentation wording.** “Generics let one repository implementation safely store four entity types. The compiler keeps a User repository separate from an Equipment repository, while the injected ID function avoids a common entity superclass.”

### 2.10 iwfc.pattern.EntityFactory

**Responsibility.** Creational Factory for all User roles and Equipment.

**Important methods.** `createUser` switches on the role and returns `Administrator`, `Instructor` or `Member` behind the `User` type. `createEquipment` returns validated Equipment.

**Calls and callers.** `IWFCFacade` calls it during first Administrator setup, later user registration and equipment addition. Constructors perform final field validation.

**Business logic.** A null role fails clearly. Creation decisions exist in one place rather than being scattered through console code.

**Presentation wording.** “The Factory centralises the choice of concrete class. The Facade asks for a User role and receives the correct subtype without knowing constructor-selection details.”

### 2.11 iwfc.service.BookingService

**Responsibility.** Implements session scheduling, availability and Member booking rules.

**Important methods.** `scheduleSession` validates hours, Instructor, session data, duplicate ID, equipment and conflicts before saving. `bookSession` validates Member, session activity, equipment, duplicate booking and capacity. `findAvailableSessions` filters unusable sessions; `findSessionsForMember` returns personal bookings.

**Calls and callers.** The Facade calls its public methods. It calls typed repositories and `FitnessSession`/`Equipment` behaviour.

**Business logic.** Operating hours are 06:00-22:00 with inclusive boundaries and same-day sessions. Overlapping sessions cannot share an Instructor, studio or equipment, while adjacent intervals and truly different resources are allowed. Equipment must exist, remain active and be Operational. Full, inactive or equipment-unavailable sessions disappear from availability.

**Presentation wording.** “BookingService is where scheduling rules live. It rejects overlaps by resource, not merely duplicate times, and it rechecks equipment at booking time because equipment state may change after scheduling.”

### 2.12 iwfc.service.MaintenanceService

**Responsibility.** Coordinates fault reports, maintenance transitions, session-equipment usage, preventative alerts and Observer delivery.

**Important fields and methods.** It holds all four repositories, an observer list, a set recording alerts sent in the current cycle, and a map of next thresholds. `reportFault`, `assignRequest`, `completeRequest`, `recordSessionEquipmentUsage` and `checkPreventativeMaintenance` expose workflows. Private notification and validation methods filter recipients.

**Calls and callers.** The Facade calls the service. It calls `Equipment`, `MaintenanceRequest` and `User` behaviour and uses repositories for relationships.

**Business logic.** Reporting requires an active Instructor and active equipment, creates a unique request, marks equipment Faulty and notifies active Administrators. Assignment changes the request to Assigned, marks equipment Under Maintenance and notifies the reporting Instructor. Completion restores only active equipment to Operational, resets the alert cycle and notifies the reporter. Usage requires the active session’s owning Instructor and assigned equipment. At the inclusive threshold, active Administrators receive one alert for that cycle. Completion sets the next threshold to current usage plus 100 and allows a later alert.

**Presentation wording.** “MaintenanceService is the Observer publisher. It decides which event happened and which active users are relevant. It also uses a per-cycle latch so repeated checks do not duplicate an alert, while completed maintenance re-arms the next 100-hour cycle.”

### 2.13 iwfc.exception.InvalidBookingException

**Responsibility.** Checked exception for expected session-scheduling and Member-booking failures.

**Triggered by.** Invalid times or required session values, unknown or unavailable equipment, instructor/studio/equipment overlap, unknown/inactive/wrong-role service-level users, inactive/full sessions, duplicate booking, or unknown/blank session ID.

**Calls and callers.** `BookingService` throws it; the Facade propagates it; console code catches it through the general safe workflow handler; tests verify it with `assertThrows`.

**Presentation wording.** “This exception groups failures the caller can correct, such as choosing another time, resource or session.”

### 2.14 iwfc.exception.UnauthorizedAccessException

**Responsibility.** Checked exception for denied access or identity/ownership failures.

**Triggered by.** Null actor, unregistered actor, fabricated object reusing a registered ID, inactive account, wrong role, or an Instructor attempting usage against another Instructor’s session.

**Calls and callers.** Primarily thrown by `IWFCFacade.requireRole`; usage ownership is also checked in `MaintenanceService`. Public Facade methods propagate it.

**Presentation wording.** “Authorization checks identity, registration, active state and role. Matching an ID alone is not enough.”

### 2.15 iwfc.exception.DuplicateDataException

**Responsibility.** Checked exception for IDs that must be unique.

**Triggered by.** Duplicate User, Equipment, FitnessSession or MaintenanceRequest IDs, and a second application initialization attempt.

**Calls and callers.** `GenericRepository.add` throws it for stored entities. The Facade and services propagate it. `BookingService` deliberately uses this type only for duplicate session IDs.

**Presentation wording.** “The generic repository provides one consistent uniqueness rule. A duplicate is different from an invalid booking, so it has its own exception type.”

## 3 Workflow explanations

### 3.1 One-time Administrator setup

1. `IWFCFacade.initializeAdministrator(id, name)` checks that no User exists.
2. The Facade calls `EntityFactory.createUser(ADMINISTRATOR, ...)`.
3. The Factory constructs `Administrator`, while `User` validates and trims identity data.
4. `GenericRepository<User>.add` stores the account and rejects a duplicate ID.
5. `MaintenanceService.registerObserver` adds the Administrator for future fault and preventative alerts.
6. A second initialization throws `DuplicateDataException`.

### 3.2 Session scheduling and Member booking

1. The Facade requires the exact active registered Instructor.
2. `BookingService.scheduleSession` verifies same-day 06:00-22:00 timing.
3. It verifies the Instructor stored in the repository.
4. `FitnessSession` validates title, studio, time order, capacity and unique equipment IDs.
5. The service rejects a duplicate session ID.
6. Every required Equipment must exist, be active and be Operational.
7. Existing active overlapping sessions are checked for Instructor, studio and equipment conflicts.
8. The valid session is stored.
9. For booking, the Facade requires an active registered Member.
10. The service rechecks session activity, equipment availability, duplicate membership and capacity, then adds the Member ID.

### 3.3 Fault and maintenance workflow

1. The Facade authorizes the active Instructor.
2. `MaintenanceService.reportFault` validates reporter and equipment, creates a Pending request, stores it, marks equipment Faulty and notifies active Administrators.
3. An Administrator views the read-only maintenance list.
4. `assignMaintenance` authorizes the Administrator and calls `assignRequest`.
5. The request becomes Assigned, equipment becomes Under Maintenance, and the reporting Instructor receives one assignment message.
6. `completeMaintenance` authorizes the Administrator and calls `completeRequest`.
7. The request becomes Completed. Active equipment returns to Operational; deactivated equipment remains deactivated and is not restored.
8. The preventative cycle resets and the reporting Instructor receives one completion message.

### 3.4 Usage and preventative-alert workflow

1. The Facade requires an active registered Instructor.
2. `MaintenanceService` finds the session and equipment.
3. It checks that the session is active, belongs to that Instructor and includes that equipment.
4. `Equipment.addUsageHours` accepts only positive finite hours.
5. The service checks the current threshold immediately after recording usage.
6. At 100 hours or more, every active Administrator receives one alert.
7. Repeated checks in the same cycle return the threshold result but do not publish another message.
8. Completed maintenance removes the latch and sets the next threshold to current hours plus 100.

### 3.5 Observer notification filtering

1. Users are registered as observers when the Facade initializes or registers them.
2. A new fault or preventative threshold is relevant to all active Administrators.
3. Assignment and completion are relevant only to the active Instructor who originally reported that request.
4. Members, unrelated Instructors and inactive users receive nothing.
5. Duplicate observer registration is prevented, so an eligible user receives one message per event.

## 4 The three design patterns in this code

| Pattern | Participant | Actual example | Why it is meaningful |
|---|---|---|---|
| Factory | `EntityFactory` | `createUser` returns one of three concrete roles; `createEquipment` creates inventory objects | Constructor choice and creation policy stay out of console and Facade workflow code |
| Facade | `IWFCFacade` | `scheduleSession`, `bookSession`, `reportFault`, `completeMaintenance` | Each public method combines authorization, repositories and service calls behind one use-case API |
| Observer | `MaintenanceService` as publisher; `User` as receiver | Active Administrators receive fault/threshold events; the reporting Instructor receives assignment/completion | Events reach only relevant active users and workflow code does not manually update every screen or client |

## 5 Exceptions and failure meaning

| Exception | Simple meaning | Representative triggers |
|---|---|---|
| `InvalidBookingException` | The requested schedule or booking breaks a business rule | Outside hours, overlap, bad resource, full session, duplicate booking |
| `UnauthorizedAccessException` | The actor is not allowed to perform the operation | Wrong role, inactive/unregistered/fabricated actor, wrong session Instructor |
| `DuplicateDataException` | An entity ID already exists | Duplicate account, equipment, session or request; second initialization |
| `IllegalStateException` | A valid object is in the wrong lifecycle state | Complete Pending request, assign twice, complete twice |
| `IllegalArgumentException` | Input or relationship is invalid outside booking semantics | Blank fields, unknown maintenance/equipment ID, invalid usage, unassigned equipment |
| `NullPointerException` | A mandatory reference is absent | Null role, urgency, repository dependency or entity collection |

## 6 JUnit test guide

The project has 14 test classes and 126 passing tests. Parameterized tests create more executed test cases than the number of method names.

| Test class | Main proof | Good tests to mention orally |
|---|---|---|
| `UserTest` | Inheritance, polymorphism, identity, active state and notifications | `exposesPolymorphicRoleInformation`; read-only notification view |
| `EquipmentTest` | Defaults, updates, usage validation and inclusive threshold | 99.9/100 boundary; non-finite and non-positive usage rejection |
| `FitnessSessionTest` | Constructor rules, defensive collections, overlap and capacity | adjacent intervals are not overlap; duplicate Member IDs do not increase count |
| `MaintenanceRequestTest` | Audit data and strict state machine | completion before assignment and repeated transitions throw |
| `GenericRepositoryTest` | Generic reuse, uniqueness, lookup and snapshots | different entity types use the same repository; snapshots stay read-only and stable |
| `EntityFactoryTest` | All three concrete roles and Equipment are created correctly | parameterized role test and null-role rejection |
| `BookingServiceTest` | Complete scheduling and booking business rules | inclusive 06:00/22:00; three conflict types; adjacent and concurrent sessions; unavailable equipment; capacity |
| `MaintenanceServiceTest` | Fault workflow, usage, alert cycles and Observer filtering | only active relevant recipients; one alert per cycle; later cycle after completion; invalid usage/ownership |
| `IWFCFacadeAdministrationTest` | Administrator-only account/equipment operations | fabricated and inactive Administrator rejection; all roles created through Factory; duplicate IDs |
| `IWFCFacadeSchedulingTest` | Facade role guards plus successful scheduling/booking | exact registered Instructor/Member checks; capacity and equipment changes |
| `IWFCFacadeMaintenanceTest` | Facade guards plus full maintenance/usage operations | Administrator-only log/transitions; deactivated equipment never reactivated; 99.9/100 alert |
| `IWFCWorkflowIntegrationTest` | End-to-end behavior using public Facade methods only | successful full workflow; duplicate equipment; conflict; unauthorized log; invalid transition; second alert cycle |
| `IWFCFacadeConsoleTest` | Invalid input and repeated-demo errors do not crash console | guided workflow reaches Operational/Completed; end-of-input closes safely |
| `IWFCFacadeSmokeTest` | Application foundation constructs correctly | Factory and both services are non-null |

### How to explain `assertThrows`

An exception test is not a broken test. For example, `assertThrows(InvalidBookingException.class, ...)` passes only when invalid input produces the expected exception. The suite stays green because rejection is the required behavior. This satisfies the brief’s request to test error conditions without committing a permanently failing test.

### Strong evidence examples

- **Operating boundaries:** a session may start at 06:00 and end at 22:00, but not go outside that range or cross into another date.
- **Overlap:** sharing an Instructor, studio or equipment is rejected only when intervals overlap. Ending exactly when another starts is allowed.
- **Capacity:** the final place is allowed; the next booking is rejected; a Member cannot book twice.
- **Maintenance state:** only Pending to Assigned to Completed succeeds.
- **Usage threshold:** 99.9 hours does not alert; reaching 100.0 does; repeated checks do not duplicate messages.
- **Alert lifecycle:** completing maintenance opens a later threshold cycle, so the same equipment can alert again after another 100 hours.
- **Security:** same ID is insufficient if the actor is a different object; inactive and wrong-role users are rejected.

## 7 Windows Terminal commands

Run these commands from `E:\Projects\Fitness` in PowerShell.

### Strict Java 21 compilation

```powershell
$sourceFiles = Get-ChildItem src/main/java -Recurse -Filter *.java | ForEach-Object FullName
New-Item -ItemType Directory -Force target/strict-java21 | Out-Null
javac --release 21 -Xlint:all -Werror -d target/strict-java21 $sourceFiles
```

### Clean build and all tests

```powershell
.\.tools\maven\apache-maven-3.9.16\bin\mvn.cmd --batch-mode clean test
```

### Package the application without repeating tests

```powershell
.\.tools\maven\apache-maven-3.9.16\bin\mvn.cmd --batch-mode -DskipTests package
```

### Run the console demonstration

```powershell
java -cp target\iwfc-management-system-1.0.0-SNAPSHOT.jar iwfc.app.IWFCFacade
```

Enter an Administrator ID and name, select `1` for the guided demonstration, then select `0` to exit.

### Check repository status

```powershell
git status
```

### Inspect the latest commit

```powershell
git log -1 --format=fuller
```

For a compact form:

```powershell
git log --oneline -1
```

## 8 One page presentation cheat sheet

### Thirty second architecture answer

“The console uses only IWFCFacade. The Facade checks the exact registered actor, active state and role, then delegates scheduling to BookingService or maintenance to MaintenanceService. Domain objects protect their own invariants and four typed GenericRepository instances hold data in memory. EntityFactory centralises creation, and MaintenanceService implements Observer notifications.”

### Likely lecturer questions and short answers

| Question | Short answer |
|---|---|
| Why use a Facade? | It gives the console one task-level API and centralises role/identity checks while hiding repositories and service coordination. |
| Where is polymorphism? | All roles are stored and passed as `User`; each subclass overrides `getRoleDescription`, and Observer delivery treats recipients as User objects. |
| Why use generics? | `GenericRepository<T>` safely reuses one storage implementation for four entity types without casts or duplicated repository classes. |
| Why are Collections important? | Maps give ID lookup, sets enforce uniqueness, and lists preserve ordered snapshots, resources, observers and messages. |
| How is double booking prevented? | BookingService applies the interval overlap rule, then checks shared Instructor, studio and equipment. |
| Why recheck equipment during booking? | Equipment may become Faulty, Under Maintenance or deactivated after a session was scheduled. |
| How are operating hours handled? | Start cannot be before 06:00, end cannot be after 22:00, and both must be on the same date. Boundaries are inclusive. |
| What is the Observer event flow? | Active Administrators receive new-fault and threshold alerts; the active reporting Instructor receives assignment and completion. |
| How do you prevent duplicate notifications? | Observer registration avoids duplicate objects and a per-equipment set latches one preventative alert per cycle. |
| How can equipment alert again later? | Completion clears the latch and sets the next threshold to current usage plus 100 hours. |
| Why checked custom exceptions? | They make expected business failures explicit to callers and allow the console to report understandable errors. |
| Why use `IllegalStateException` for transitions? | The request exists, but its current lifecycle state makes the operation invalid. |
| What proves the code works? | 126 JUnit tests across 14 classes, including domain, service, Facade, console and integration paths, all pass. |
| Was an intentionally failing test committed? | No. Negative tests intentionally trigger an exception and pass only when `assertThrows` sees the correct type. |
| What was a challenging defect? | Preventative alerts originally risked becoming once-ever; the final per-cycle threshold reset supports future cycles without duplicates. |
| Why no database? | The brief permits hard-coded or in-memory data. Persistence is a documented future improvement. |
| What remains limited? | No credential authentication, persistent transactions, weekly recurrence, concurrent booking protection or Member reminder publishing. |
| Why exactly 15 classes? | It meets the required 10-15 range. Nested enums stay with the entities they describe and add no source files. |
