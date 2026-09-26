# IWFC PRES1 speaking script

Target duration: approximately 10 minutes, including a short live console demonstration. The same prompts are embedded in the PowerPoint speaker notes. Sources are the CMP 7001 briefs, implemented Java code, final technical evidence pack, PRAC1 report and Maven Surefire XML.

## Slide 1 — Introduction (0:00–0:35)

I developed the Intelligent Wellness and Fitness Center, or IWFC, as a Java 21 prototype for equipment oversight, fitness sessions, booking and maintenance. I will show the requirements, class design, three patterns, test evidence and the running console. The private GitHub repository is on this slide. Before final submission I must record this presentation, put the video on YouTube or an accessible student OneDrive, replace the visible video-link reminder with its real URL, and test tutor access.

## Slide 2 — Problem and objective (0:35–1:15)

The scenario describes fragmented manual records. Without one place to check schedules and equipment state, it is easy to miss a resource conflict or lose track of a fault. My objective was a small unified management tool with explicit business rules. This is an academic, in-memory prototype. The brief does not require a database.

## Slide 3 — Core workflows (1:15–2:00)

An Administrator manages accounts and equipment, and controls maintenance assignment and completion. An Instructor creates sessions, records session-equipment usage and reports faults. A Member sees available sessions and books a place. Booking and scheduling enforce operating hours, resource availability, overlap rules and capacity. A reported fault moves through Pending, Assigned and Completed.

## Slide 4 — Class diagram (2:00–3:15)

The diagram names all 15 top-level production files. The abstract User owns shared identity, active state and notification behaviour. Administrator, Instructor and Member extend it. Equipment, FitnessSession and MaintenanceRequest hold domain rules. BookingService handles ordinary and atomic weekly scheduling, bookings, confirmations and wellness tips. MaintenanceService manages faults, multiple open requests, usage and filtered notifications. IWFCFacade exposes the public use cases, including scheduleWeeklySessions, viewAllSessions and loadSampleData. EntityFactory centralises creation and GenericRepository<T> provides type-safe storage. The three checked custom exceptions express booking, access and duplicate-data failures. Nested enums live inside existing source files, so they do not increase the 15-file count.

## Slide 5 — Design patterns (3:15–4:10)

Factory has a real job: EntityFactory creates all three user roles and equipment, while the Facade calls it for setup. Facade has a real job: IWFCFacade validates the registered active actor and coordinates services and repositories behind task-level methods. Observer has a real job: MaintenanceService sends new-fault and 100-hour preventative alerts to active Administrators. Assignment and completion reach the active Instructor who reported the fault. Unrelated or inactive users do not receive an event.

## Slide 6 — OOP decisions (4:10–4:55)

User is an abstraction with common behaviour. Its subclasses override getRoleDescription, so the application can work with a User reference and still invoke role-specific output. That is the direct polymorphism example. Private fields and named transition methods protect entity state. GenericRepository<T> is reused for users, equipment, sessions and requests without unsafe casts. Maps serve ID lookup, sets prevent duplicate bookings and alerts, and lists preserve ordered notifications and snapshots.

## Slide 7 — Exceptions (4:55–5:40)

The system throws InvalidBookingException for scheduling or booking rule failures. UnauthorizedAccessException covers wrong roles, inactive or unregistered actors and usage ownership. DuplicateDataException protects unique IDs across entity types. A maintenance request permits only Pending to Assigned to Completed, and an invalid transition raises IllegalStateException. The console catches these failures and shows a clear message instead of terminating unexpectedly.

## Slide 8 — Testing (5:40–6:30)

The verified Maven Surefire result is 14 suites and 142 tests, with no failures, errors or skips. Tests cover the domain, Factory and repository, both services, Facade authorization, console and end-to-end integration. New cases prove all-or-nothing weekly recurrence, Member notification filtering, equipment status with other open requests and sample-data console views. Negative tests deliberately invoke invalid conditions with assertThrows; the suite remains green because those exceptions are expected. Important boundaries include exactly 06:00 and 22:00, one to twelve recurrence weeks, capacity, and 99.9 versus 100 equipment-usage hours.

## Slide 9 — Integration and reflection (6:30–7:20)

The integration suite drives the successful workflow through public Facade methods. It also rejects duplicate equipment, a conflicting session, unauthorized maintenance-log access and an invalid status transition. One design issue was the preventative alert latch: if an alert could occur only once ever, later maintenance cycles would never alert. Completion now rearms the next threshold at the current total plus 100 hours. Tests prove a second cycle alerts once while repeated checks within a cycle do not.

## Slide 10 — Console demonstration (7:20–8:35)

Switch from the slide to the terminal. Enter A1 and Rifad for the one-time Administrator. Select option 2 to load the sample data, then option 3. Point out S1 at 2/2 and all four Weekly Stretch occurrences. Select option 1 and show the booking confirmation, deterministic wellness tip, affected-session schedule notice and 100-hour preventative alert. End with equipment Operational and request Completed, then select option 0. The console calls public Facade methods rather than manipulating repositories directly.

## Slide 11 — Limitations (8:35–9:20)

The prototype is intentionally in memory, so a restart loses data. It checks registered actor identity and role, but does not implement passwords or a real authentication service. Concurrent bookings do not have database transactions. Weekly recurrence uses a fixed seven-day interval and occurrence count rather than a general calendar-rule engine. Notifications remain in process rather than using durable email or mobile delivery. Next steps are persistent transactional storage, credential authentication, concurrency-safe booking and external notification delivery.

## Slide 12 — Conclusion (9:20–10:00)

The result is a working Java prototype with 15 production files, three patterns used in actual workflows and 142 passing tests. The hardest part was keeping the architecture compact while adding atomic weekly recurrence, filtered Member notifications and correct equipment status across multiple requests. Testing the boundary and rejected cases made the design more precise. Before submission, replace slide one's video reminder with the real recording link and check both audio and screen capture.

## Recording and submission checklist

1. Record approximately 10 minutes with visible code, test results and a running console demonstration.
2. Upload the recording to YouTube or student OneDrive; confirm the tutor can open it.
3. Replace the first-slide video reminder with the real link. Do not leave a fictitious URL.
4. Check audio, screen capture and the displayed class diagram.
5. Follow the exact Moodle/Turnitin and ICBT submission instructions in the PRES1 brief, including any required cover and feedback sheets.
