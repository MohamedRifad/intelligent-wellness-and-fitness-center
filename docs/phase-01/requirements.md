# IWFC Software Requirements Specification

## 1 Project objective

The Intelligent Wellness and Fitness Center system will replace fragmented manual processes with one Java application for equipment oversight, fitness-session scheduling, member bookings, maintenance reporting, and automatic notifications.

The system will use a console interface and in-memory data. Its main assessment purpose is to demonstrate sound object-oriented design, Java collections, generics, design patterns, custom exceptions, and JUnit testing.

## 2 Scope

### Included

- Administrator, Instructor, and Member roles.
- Basic user-account administration.
- Equipment inventory and usage tracking.
- Fitness-session scheduling.
- Member bookings.
- Studio and equipment conflict detection.
- Fault reporting and maintenance workflow.
- Preventative-maintenance alerts.
- Automatic maintenance and schedule notifications.
- Role-based authorization.
- Pre-populated demonstration data.
- Console menus.
- JUnit tests.

### Optional after the mandatory system is complete

- Recurring weekly sessions.

### Excluded

- Database integration.
- Web or mobile application.
- Payment processing.
- Real email or SMS delivery.
- Medical diagnosis or real medical records.
- Machine learning.

## 3 Actors and permissions

| Capability | Administrator | Instructor | Member |
|---|:---:|:---:|:---:|
| View active equipment | Yes | Yes | No |
| Add, edit, or deactivate equipment | Yes | No | No |
| Record equipment usage | Yes | Yes | No |
| Manage user accounts | Yes | No | No |
| Create fitness sessions | No | Yes | No |
| View session schedule | Yes | Yes | Yes |
| Book a session | No | No | Yes |
| Report an equipment fault | No | Yes | No |
| View global maintenance log | Yes | No | No |
| View own reported maintenance requests | Yes | Yes | No |
| Assign and update maintenance work | Yes | No | No |
| Receive relevant notifications | Yes | Yes | Yes |

An unauthorized operation must throw `UnauthorizedAccessException` rather than relying only on a hidden menu option.

## 4 Functional requirements

### 4.1 User accounts

- **FR-U01:** The system shall store Administrator, Instructor, and Member accounts with unique IDs.
- **FR-U02:** An Administrator shall be able to add and view user accounts.
- **FR-U03:** An Administrator shall be able to deactivate a user account.
- **FR-U04:** The system shall prevent duplicate user IDs.
- **FR-U05:** The system shall prevent unauthorized roles from managing accounts or accessing administrator-only information.

### 4.2 Equipment tracking

- **FR-E01:** An Administrator shall be able to add equipment.
- **FR-E02:** An Administrator shall be able to edit equipment details.
- **FR-E03:** An Administrator shall be able to deactivate equipment.
- **FR-E04:** Every equipment item shall have a unique ID, name or type, status, location, cumulative usage hours, and active state.
- **FR-E05:** Equipment status shall support Operational, Faulty, and Under Maintenance.
- **FR-E06:** The system shall reject a duplicate equipment ID.
- **FR-E07:** An authorized user shall be able to record additional equipment usage hours.
- **FR-E08:** Usage hours shall never be negative or reduced accidentally.
- **FR-E09:** The system shall generate a preventative-maintenance alert when cumulative usage reaches the configured threshold.
- **FR-E10:** Deactivated, Faulty, or Under Maintenance equipment shall not be assigned to a new session.

### 4.3 Session scheduling

- **FR-S01:** An Instructor shall be able to schedule a fitness session.
- **FR-S02:** A session shall record a unique ID, title, Instructor, studio, start time, end time, capacity, and required equipment.
- **FR-S03:** The system shall reject a session whose end time is not after its start time.
- **FR-S04:** The system shall reject a session outside IWFC operating hours.
- **FR-S05:** The system shall reject overlapping sessions using the same studio.
- **FR-S06:** The system shall reject overlapping sessions using the same equipment.
- **FR-S07:** Adjacent sessions where one ends exactly when another begins shall be allowed.
- **FR-S08:** Users shall be able to view the session schedule appropriate to their role.
- **FR-S09:** The system may generate recurring weekly sessions after mandatory scheduling is complete.

### 4.4 Member booking

- **FR-B01:** A Member shall be able to view fitness sessions with available capacity.
- **FR-B02:** A Member shall be able to book a specific session.
- **FR-B03:** A session's booking collection shall connect active Members with that session.
- **FR-B04:** The system shall reject a duplicate booking by the same Member for the same session.
- **FR-B05:** The system shall reject a booking when session capacity is full.
- **FR-B06:** The system shall reject a booking for an unknown or unavailable session.
- **FR-B07:** A Member shall be able to view their bookings.
- **FR-B08:** A Member shall receive a relevant schedule notification when a booked session changes.

### 4.5 Maintenance reporting

- **FR-M01:** An Instructor shall be able to report an equipment fault.
- **FR-M02:** A maintenance request shall record a unique request ID, equipment ID, description, urgency, status, reporter, assignee, and timestamps.
- **FR-M03:** Urgency shall support Low, Medium, and High.
- **FR-M04:** Status shall support Pending, Assigned, and Completed.
- **FR-M05:** A newly reported request shall begin in Pending status.
- **FR-M06:** An Administrator shall be able to view the global maintenance log.
- **FR-M07:** An Administrator shall be able to assign a Pending request.
- **FR-M08:** An Administrator shall be able to complete an Assigned request.
- **FR-M09:** The system shall reject invalid maintenance transitions.
- **FR-M10:** Reporting a fault shall update the related equipment to Faulty.
- **FR-M11:** Assigning work shall update the equipment to Under Maintenance.
- **FR-M12:** Completing work shall return active equipment to Operational.
- **FR-M13:** The system shall automatically notify relevant observers whenever a request status changes.

## 5 Business rules

### Scheduling

- **BR-S01:** Initial operating hours are 06:00 inclusive to 22:00 inclusive.
- **BR-S02:** A valid session satisfies `start < end`.
- **BR-S03:** Two intervals overlap when `newStart < existingEnd` and `newEnd > existingStart`.
- **BR-S04:** A studio cannot host overlapping sessions.
- **BR-S05:** A piece of equipment cannot be allocated to overlapping sessions.
- **BR-S06:** A session capacity must be greater than zero.
- **BR-S07:** Only Operational and active equipment can be allocated.

### Equipment

- **BR-E01:** IDs are immutable after creation.
- **BR-E02:** Added usage must be greater than zero.
- **BR-E03:** The initial preventative-maintenance threshold is 100 hours.
- **BR-E04:** Deactivation prevents new scheduling but does not delete historical records.

### Maintenance

- **BR-M01:** Valid workflow is Pending to Assigned to Completed.
- **BR-M02:** Completed requests cannot be reopened in the minimum prototype.
- **BR-M03:** Only an Administrator can assign or complete a request.
- **BR-M04:** A request cannot reference unknown equipment.
- **BR-M05:** A request description cannot be blank.

### Authorization

- **BR-A01:** Application services shall validate permissions even when the console menu hides an option.
- **BR-A02:** Inactive accounts cannot perform operations.
- **BR-A03:** A Member cannot access the global maintenance log.

## 6 Technical requirements

- **TR-01:** The application shall be implemented in Java.
- **TR-02:** The production design shall contain 10–15 well-structured classes or principal production types.
- **TR-03:** The design shall balance entities, services/controllers, and pattern responsibilities.
- **TR-04:** The system shall use `ArrayList` for ordered records where appropriate.
- **TR-05:** The system shall use `HashMap` for unique-ID lookup where appropriate.
- **TR-06:** The system shall use a genuine generic abstraction such as `GenericRepository<T>`.
- **TR-07:** The implementation shall demonstrate abstraction.
- **TR-08:** The implementation shall demonstrate encapsulation.
- **TR-09:** The implementation shall demonstrate polymorphism.
- **TR-10:** The implementation shall use at least one creational pattern: Factory.
- **TR-11:** The implementation shall use at least one structural pattern: Facade.
- **TR-12:** The implementation shall use at least one behavioural pattern: Observer.
- **TR-13:** The implementation shall define custom exceptions for invalid bookings, unauthorized access, and duplicate data.
- **TR-14:** The minimum user interface shall be a console-based menu.
- **TR-15:** Data may be hard-coded or pre-populated and need not survive application restart.

## 7 Quality and testing requirements

- **QR-01:** JUnit shall validate scheduling and booking logic.
- **QR-02:** JUnit shall validate maintenance workflow transitions.
- **QR-03:** JUnit shall verify every mandatory custom exception.
- **QR-04:** At least one test shall intentionally trigger an error condition and verify the expected exception using `assertThrows`.
- **QR-05:** Tests shall cover successful, boundary, negative, and authorization cases.
- **QR-06:** The final normal test suite shall pass completely.
- **QR-07:** Console errors shall be clear and shall not expose stack traces to ordinary users.

## 8 Delivery requirements

- **DR-01:** Source code shall be managed using GitHub or Bitbucket.
- **DR-02:** Git history shall contain meaningful incremental commits.
- **DR-03:** The repository shall include build, test, and run instructions.
- **DR-04:** PRAC1 documentation shall be approximately 3,000 words and follow the specified report formatting.
- **DR-05:** The report shall use Harvard referencing and include the required coversheet and feedback sheet.
- **DR-06:** PRES1 shall include an approximately 10-minute video, slides, code walkthrough, running demonstration, test results, bug discussion, and reflection.
- **DR-07:** The video shall be uploaded to YouTube or OneDrive and linked from the first slide.
- **DR-08:** Final filenames shall follow the student-ID, module-code, and assessment-ID convention.

## 9 Acceptance criteria

The prototype is accepted when:

1. All three actors can perform their permitted core workflows through the console.
2. Unauthorized operations are rejected by service-level checks.
3. Equipment can be added, edited, deactivated, located, and usage-tracked.
4. Duplicate equipment and user IDs are rejected.
5. Preventative alerts occur at the documented threshold.
6. Sessions can be scheduled only with valid times, available studios, and available equipment.
7. Members can view and book available sessions without exceeding capacity or duplicating bookings.
8. Instructors can report faults with all required attributes.
9. Administrators can view, assign, and complete maintenance requests only through valid transitions.
10. Status changes automatically produce relevant notifications.
11. Factory, Facade, and Observer patterns are visible in the implementation and can be explained.
12. Collections, generics, abstraction, encapsulation, inheritance, and polymorphism have real uses.
13. All mandatory JUnit tests pass, including exception-triggering tests.
14. GitHub evidence, report evidence, and presentation evidence are complete.

## 10 Assumptions requiring confirmation

- Whether interfaces, enums, exceptions, nested classes, UI classes, and tests count toward the 10–15-class limit.
- Whether Java 21 is permitted; the implementation should use the lecturer-approved version.
- Whether an `assertThrows` negative test satisfies the phrase "intentional failing test."
- Exact operating hours and maintenance threshold expected by the lecturer.
- Exact Moodle and ICBT submission formats and deadlines.

Until clarified, the project will use conservative choices documented above.
