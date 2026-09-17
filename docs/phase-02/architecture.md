# IWFC Detailed Architecture

## 1 Architecture summary

IWFC will use a small layered structure:

```text
Console user
    ↓
IWFCFacade
    ↓
BookingService / MaintenanceService
    ↓
GenericRepository<T>
    ↓
Users / Equipment / FitnessSession / MaintenanceRequest
```

The console calls the facade. The facade checks roles and delegates business rules to services. Services use repositories instead of owning unrelated menu logic. Entities protect their own state.

## 2 Package structure

To keep the project understandable, the 15 production classes will use five packages:

```text
iwfc.app
    IWFCFacade

iwfc.domain
    User
    Administrator
    Instructor
    Member
    Equipment
    FitnessSession
    MaintenanceRequest

iwfc.repository
    GenericRepository

iwfc.service
    BookingService
    MaintenanceService

iwfc.pattern
    EntityFactory

iwfc.exception
    InvalidBookingException
    UnauthorizedAccessException
    DuplicateDataException
```

## 3 Exact class count

| # | Class | Kind | Main purpose |
|---:|---|---|---|
| 1 | `User` | Abstract entity and Observer | Common user identity, active state, and notification contract |
| 2 | `Administrator` | Entity | Administrative role behaviour |
| 3 | `Instructor` | Entity | Instructor role behaviour |
| 4 | `Member` | Entity | Member role and received notifications |
| 5 | `Equipment` | Entity | Equipment inventory and usage state |
| 6 | `FitnessSession` | Entity | Scheduled class, resources, capacity, and Member IDs |
| 7 | `MaintenanceRequest` | Entity | Fault report and controlled maintenance workflow |
| 8 | `GenericRepository<T>` | Generic repository | Reusable ID-indexed in-memory storage |
| 9 | `EntityFactory` | Factory pattern | Valid creation of users and equipment |
| 10 | `BookingService` | Service | Scheduling and booking validation |
| 11 | `MaintenanceService` | Service and Observer subject | Maintenance operations, alerts, and notifications |
| 12 | `IWFCFacade` | Facade and entry point | Authorization, use-case coordination, and console menus |
| 13 | `InvalidBookingException` | Custom exception | Invalid schedule or booking operations |
| 14 | `UnauthorizedAccessException` | Custom exception | Role or inactive-user violations |
| 15 | `DuplicateDataException` | Custom exception | Duplicate ID or duplicate booking data |

Enums will be nested in the class that owns them, so they do not create additional source files:

- `User.Role`
- `Equipment.Status`
- `MaintenanceRequest.Urgency`
- `MaintenanceRequest.Status`

## 4 Class contracts

### 4.1 `User`

**Purpose:** Provides abstraction and the Observer behaviour shared by all roles.

**Fields:**

- `String id`
- `String name`
- `Role role`
- `boolean active`
- `List<String> notifications`

**Methods:**

- Constructor validates ID and name.
- `getId()`, `getName()`, `getRole()`, `isActive()`.
- `deactivate()`.
- `receiveNotification(String message)` adds a message to the notification list.
- `getNotifications()` returns an unmodifiable view.
- `getRoleDescription()` is abstract and demonstrates polymorphism.

### 4.2 `Administrator`

Extends `User` and reports the Administrator role description. Administrative operations remain in the facade and services so the entity does not become a God class.

### 4.3 `Instructor`

Extends `User` and reports the Instructor role description. The Instructor ID is stored on sessions and fault reports.

### 4.4 `Member`

Extends `User` and reports the Member role description. Member IDs are stored in the session booking collection.

### 4.5 `Equipment`

**Fields:**

- `String id`
- `String name`
- `String location`
- `Status status`
- `double cumulativeUsageHours`
- `boolean active`

**Methods:**

- Constructor validates required fields.
- `updateDetails(String name, String location)`.
- `deactivate()`.
- `addUsageHours(double hours)` rejects non-positive values.
- `requiresPreventativeMaintenance(double threshold)`.
- `isAvailableForScheduling()`.
- Controlled `markFaulty()`, `markUnderMaintenance()`, and `markOperational()` methods.

The ID is immutable. No public general-purpose status setter will be provided.

### 4.6 `FitnessSession`

**Fields:**

- `String id`
- `String title`
- `String instructorId`
- `String studio`
- `LocalDateTime startTime`
- `LocalDateTime endTime`
- `int capacity`
- `List<String> equipmentIds`
- `Set<String> bookedMemberIds`
- `boolean active`

**Methods:**

- Constructor validates basic required data.
- `overlaps(FitnessSession other)`.
- `usesStudio(String studio)`.
- `usesAnyEquipment(Collection<String> equipmentIds)`.
- `hasCapacity()`.
- `addMember(String memberId)`.
- `containsMember(String memberId)`.
- `deactivate()`.
- Read-only collection accessors.

`HashSet` prevents the same Member ID being inserted twice. `ArrayList` retains equipment allocation order.

### 4.7 `MaintenanceRequest`

**Fields:**

- `String id`
- `String equipmentId`
- `String description`
- `Urgency urgency`
- `Status status`
- `String reportedByInstructorId`
- `String assignedTo`
- `LocalDateTime createdAt`
- `LocalDateTime updatedAt`

**Methods:**

- Constructor creates a Pending request.
- `assignTo(String assignee)` permits Pending to Assigned.
- `complete()` permits Assigned to Completed.
- Getters expose the workflow safely.

Invalid maintenance transitions will throw `IllegalStateException`. The assignment's named custom exceptions remain reserved for its three mandatory categories.

### 4.8 `GenericRepository<T>`

**Fields:**

- `Map<String, T> items`
- `Function<T, String> idExtractor`

**Methods:**

- `add(T item)` throws `DuplicateDataException` for an existing ID.
- `findById(String id)` returns `Optional<T>`.
- `findAll()` returns an immutable snapshot.
- `containsId(String id)`.

This supplies genuine generics and `HashMap` usage for several entity types.

### 4.9 `EntityFactory`

**Methods:**

- `createUser(User.Role role, String id, String name)` returns the correct polymorphic subtype.
- `createEquipment(String id, String name, String location)`.

This is the required creational Factory pattern.

### 4.10 `BookingService`

**Dependencies:**

- Equipment repository.
- Session repository.
- User repository.
- Opening and closing times.

**Methods:**

- `scheduleSession(...)` validates the Instructor, time, studio, and equipment before storing the session.
- `bookSession(String memberId, String sessionId)` validates Member status, capacity, and duplicates.
- `findAvailableSessions()`.
- `findSessionsForMember(String memberId)`.
- `recordSessionUsage(String sessionId, double hours)` adds usage to allocated equipment.
- Optional `scheduleWeeklySessions(...)` after the mandatory system is complete.

Time, capacity, studio, equipment, and availability violations throw `InvalidBookingException`. A repeated Member-to-session relationship throws `DuplicateDataException`.

### 4.11 `MaintenanceService`

**Dependencies:**

- Equipment repository.
- Maintenance-request repository.
- User repository.
- `List<User> observers`.

**Methods:**

- `registerObserver(User user)`.
- `reportFault(...)` creates a Pending request and marks equipment Faulty.
- `assignRequest(...)` assigns the request and marks equipment Under Maintenance.
- `completeRequest(...)` completes the request and marks active equipment Operational.
- `findAllRequests()`.
- `checkUsageAlert(String equipmentId)`.
- `notifyObservers(String message)` calls polymorphic `receiveNotification` behaviour.

This class is the subject/publisher in the required behavioural Observer pattern.

### 4.12 `IWFCFacade`

**Purpose:** Provides the structural Facade pattern and application entry point.

**Responsibilities:**

- Own repositories and services.
- Seed demonstration data.
- Check role authorization.
- Expose simple application use cases.
- Map exceptions to friendly console messages.
- Run Administrator, Instructor, and Member menus.

Representative methods:

- `registerUser(User actor, ...)`.
- `deactivateUser(User actor, String userId)`.
- `addEquipment(User actor, ...)`.
- `editEquipment(User actor, ...)`.
- `deactivateEquipment(User actor, String equipmentId)`.
- `scheduleSession(User actor, ...)`.
- `bookSession(User actor, String sessionId)`.
- `reportFault(User actor, ...)`.
- `assignMaintenance(User actor, ...)`.
- `completeMaintenance(User actor, ...)`.
- `viewMaintenanceLog(User actor)`.
- `main(String[] args)`.

### 4.13–4.15 Custom exceptions

- `InvalidBookingException extends Exception`.
- `UnauthorizedAccessException extends Exception`.
- `DuplicateDataException extends Exception`.

They are checked exceptions so the application layer must acknowledge and handle the required error conditions explicitly.

## 5 Relationship rules

- Every role class inherits from `User`.
- `GenericRepository<T>` stores users, equipment, sessions, and maintenance requests.
- `BookingService` uses user, equipment, and session repositories.
- `MaintenanceService` uses user, equipment, and maintenance repositories.
- `EntityFactory` creates polymorphic users and equipment.
- `IWFCFacade` owns and coordinates repositories and services.
- `FitnessSession` references users and equipment by immutable ID to avoid circular object ownership.
- `MaintenanceRequest` references its equipment and reporter by immutable ID.

## 6 Authorization design

Every privileged facade method receives the acting `User`. A shared internal guard validates that the account is active and has the required role. This means permissions remain enforced even if a method is called without using the normal menu.

Examples:

- Only Administrator: manage users/equipment, view global maintenance log, assign and complete maintenance.
- Only Instructor: schedule sessions, record session equipment usage, report faults.
- Only Member: book sessions.
- All active roles: view suitable schedules and their own notifications.

## 7 Error ownership

| Problem | Owner | Exception |
|---|---|---|
| Duplicate entity ID | Repository | `DuplicateDataException` |
| Duplicate Member booking | Booking service | `DuplicateDataException` |
| Time, capacity, studio, or equipment violation | Booking service | `InvalidBookingException` |
| Wrong role or inactive actor | Facade authorization guard | `UnauthorizedAccessException` |
| Invalid maintenance transition | Maintenance entity/service | `IllegalStateException` |
| Invalid raw field value | Entity constructor/method | `IllegalArgumentException` |

## 8 Planned test classes

Test classes do not belong to the 15 production-class count.

- `EquipmentTest`
- `FitnessSessionTest`
- `GenericRepositoryTest`
- `EntityFactoryTest`
- `BookingServiceTest`
- `MaintenanceServiceTest`
- `IWFCFacadeAuthorizationTest`
- `IWFCWorkflowIntegrationTest`

## 9 Design quality checks

- No UI class directly edits repository collections.
- No public mutable collection is returned.
- No general equipment-status setter bypasses workflow rules.
- IDs remain immutable.
- Services receive repositories through constructors to support unit testing.
- Date and time values use the Java Time API.
- Exceptions reach the console boundary and become friendly messages.
- Every class has one clear main responsibility.

## 10 Phase 3 implementation order

1. Create Maven and Git structure.
2. Add the three custom exceptions.
3. Add `User` and its three subtypes.
4. Add the remaining entities.
5. Add generic repository.
6. Add compile-only service, factory, and facade skeletons.
7. Add one smoke test.
8. Verify `mvn test` succeeds.

Business functionality will then be added in later phases with tests rather than implemented as one large batch.
