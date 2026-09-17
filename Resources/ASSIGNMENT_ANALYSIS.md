# CMP 7001 Assignment Analysis and Distinction-Level Project Recommendation

## Purpose

This document analyses the following assignment briefs:

- `Resources/CMP 7001_S1_PRAC1_25-26.docx`
- `Resources/CMP 7001_S1_PRES1_25-26.docx`

It separates compulsory requirements from optional enhancements and recommends a feasible distinction-level direction. No implementation decisions should be treated as final until the unclear requirements listed at the end have been confirmed.

## Executive recommendation

Develop an **Explainable Smart Maintenance and Operations System** for the Intelligent Wellness and Fitness Center (IWFC).

The main differentiator should be an explainable equipment-health engine that combines usage hours, fault history, urgency, and maintenance state to prioritise preventative maintenance. This remains within the required scenario while creating strong evidence of:

- advanced Java and object-oriented programming;
- meaningful use of collections and generics;
- appropriate creational, structural, and behavioural design patterns;
- thorough JUnit testing;
- measurable evaluation and data visualisation;
- security, privacy, accessibility, and professional UI/UX;
- critical reflection and high-quality documentation.

A deterministic, explainable risk model is preferable to machine learning because the assignment does not provide a credible training dataset and does not require AI.

This recommendation assumes approximately **4–6 weeks of part-time development**. It should be adjusted once the exact deadline and available weekly hours are known.

---

## 1. Compulsory requirements and deliverables

### 1.1 PRAC1: Java software prototype (75%)

#### Scenario and actors

The project must remain an **Intelligent Wellness and Fitness Center** management prototype with three principal actors:

- **Administrator**
  - Manages the equipment inventory.
  - Oversees user accounts.
  - Monitors the global maintenance log.
  - Assigns maintenance tasks and updates progress.
- **Instructor**
  - Schedules fitness sessions.
  - Tracks specialised-equipment usage.
  - Reports technical or mechanical faults.
- **Member**
  - Views available sessions.
  - Requests bookings.
  - Receives wellness or scheduling notifications.

#### Equipment tracking

Administrators must be able to:

- add equipment;
- edit equipment;
- deactivate equipment.

Every equipment unit must contain:

- a unique ID;
- a status such as Operational, Faulty, or Under Maintenance;
- a location such as Cardio Zone or Studio A;
- cumulative usage hours.

Usage hours must trigger preventative-maintenance alerts.

#### Session scheduling and booking

The prototype must:

- display available fitness slots;
- allow members to book specific sessions;
- prevent double-booking of the same equipment;
- prevent double-booking of the same studio space;
- validate scheduling constraints.

Recurring weekly classes are optional but explicitly described as highly encouraged.

#### Maintenance reporting

Instructors must be able to report equipment faults. Each maintenance request must include:

- equipment ID;
- description;
- urgency: Low, Medium, or High;
- status: Pending, Assigned, or Completed.

Administrators must be able to assign maintenance tasks and update their progress. The system must automatically generate notifications when a request's status changes.

#### Java architecture

The prototype must:

- be written in Java;
- contain **10–15 well-structured classes**;
- balance entity classes, controllers/services, and design-pattern implementation classes;
- demonstrate advanced programming proficiency.

#### Required constructs and OOP principles

The implementation must explicitly demonstrate:

- Java Collections, including structures such as `ArrayList` and `HashMap`;
- Generics;
- Abstraction;
- Encapsulation;
- Polymorphism.

Inheritance and reusability also appear in the learning outcomes and should be demonstrated where they are genuinely appropriate.

#### Required design patterns

At least one pattern must be implemented from each category:

| Category | Examples given by the brief |
|---|---|
| Creational | Factory, Singleton, or Builder |
| Structural | Facade, Adapter, or Composite |
| Behavioural | Observer, Strategy, or Command |

This requires at least three meaningful pattern applications. The patterns should solve genuine design problems rather than exist solely as assessment checkboxes.

#### Custom exceptions

Custom exception handling is mandatory for:

- invalid bookings, including clashes or operation outside opening hours;
- unauthorised access;
- duplicate data, such as registering an existing equipment ID.

#### JUnit testing

JUnit tests must validate:

- session-scheduling logic;
- booking validation;
- maintenance workflow transitions;
- custom exception behaviour.

The brief also asks for an "intentional failing test." This is ambiguous and requires lecturer confirmation because a properly written `assertThrows` negative test passes when the expected exception is raised.

#### Version control

Use of GitHub or Bitbucket is mandatory. A strong submission should also show:

- meaningful incremental commits;
- feature branches where useful;
- tagged releases;
- a clear README;
- no committed generated binaries, credentials, or secrets.

#### Interface and persistence

- A console menu is the minimum accepted interface.
- A GUI is permitted.
- Database integration is not required.
- Data may be pre-populated or hard-coded.
- The brief says architecture and OOP matter more than visual appearance.

#### Written report

The practical brief specifies:

- 3,000 words;
- A4 paper;
- one-inch left and right margins;
- half-inch binding margin;
- one-inch header and footer;
- single-sided layout;
- 12-point Arial or Times New Roman;
- Harvard referencing;
- inclusion of the coversheet and feedback sheet;
- avoidance of copying the assignment question into the answer.

The example file name is `studentID_CMP7001_PRAC1`.

The report is described as a PDF submission to Moodle/Turnitin, while ICBT SIS instructions request Word format. This needs confirmation.

### 1.2 PRES1: presentation (25%)

The presentation assessment requires:

- an approximately 10-minute recorded presentation;
- a PowerPoint or equivalent slide deck;
- a walkthrough of relevant code;
- a demonstration of the running application;
- a YouTube or accessible OneDrive video;
- the video link on the first slide.

The presentation must cover:

- an introduction;
- an overview of the class diagram;
- use of polymorphism;
- selected design patterns and their implementation;
- exception handling;
- test results;
- errors discovered and corrected;
- unresolved defects or incomplete functionality;
- reflection on learned OOP techniques;
- techniques found challenging.

The stated equivalent workload is 1,000 words. The example file name is `studentID_CMP7001_PRES1`.

The final recording should be checked for:

- audible, clear narration;
- correct screen capture;
- working video permissions;
- a working link on the first slide.

### 1.3 Learning outcomes

The assessed learning outcomes are:

1. Critically evaluate abstraction, reusability, inheritance, and encapsulation.
2. Apply polymorphic constructs through generics and collections.
3. Write secure software through exception handling.
4. Solve a real-world problem using OOP and design patterns.
5. Demonstrate advanced proficiency in a high-level programming language.

The brief also references Cardiff Met EDGE attributes, including ethical awareness, digital capability, evaluation of software-engineering techniques, and alignment of software requirements with business needs.

---

## 2. What is required for a distinction

A distinction appears to begin at **70%**. The **80–100%** band expects work approaching publishable quality.

### 2.1 Expectations for 70–79%

- Detailed research.
- A compelling, well-supported motivation.
- Excellent design.
- Justified choices concerning data sources or APIs where applicable.
- Efficient and reliable implementation.
- Clear evidence of testing.
- Findings communicated through visualisation and storytelling.
- Excellent understanding during the presentation.
- Excellent report structure and referencing.
- Only minor errors.

### 2.2 Expectations for 80–100%

- Extensive rather than merely detailed research.
- Excellent rationale grounded in evidence.
- Exceptionally coherent design.
- A thorough test plan and results.
- Clearly interpreted findings and meaningful visualisations.
- Explicit limitations and conclusions.
- Authoritative explanation of implementation and design choices.
- Report and references approaching publishable quality.

### 2.3 Practical distinction standard

#### Architecture

- Separate domain entities, application services, repositories, validation, presentation, and notifications.
- Justify pattern selection against alternatives.
- Aim for low coupling and high cohesion.
- Use dependency injection rather than creating dependencies inside services.
- Avoid a God class or oversized menu controller.
- Enforce domain invariants centrally.

#### Critical evaluation

For every major pattern or technique, explain:

- the problem it solves;
- why it suits this system;
- which alternative was considered;
- its effect on coupling, testability, and extensibility;
- the complexity or disadvantages it introduces.

#### Technical depth

- Use generics for genuine type safety.
- Select collections based on behaviour and performance.
- Demonstrate meaningful runtime polymorphism.
- Model scheduling overlaps and maintenance states carefully.
- Map custom exceptions to safe user-facing messages.
- Keep IDs immutable and state transitions controlled.

#### Testing

- Include unit, parameterised, boundary, negative, and workflow tests.
- Test exact, partial, contained, and adjacent time intervals.
- Test invalid hours, duplicates, unauthorised actions, and invalid transitions.
- Summarise results quantitatively.
- Document defects discovered during testing and their fixes.
- Treat code coverage as supporting evidence rather than the sole quality measure.

#### Evaluation

Answer measurable questions, for example:

- Does indexed lookup outperform linear lookup as data volume grows?
- Does clash detection reject every overlap category?
- How do alternative maintenance policies affect alert prioritisation?
- How usable is the booking workflow?
- Which test types found which defects?

#### Documentation and presentation

- Maintain traceability from requirements to classes and tests.
- Keep UML consistent with the implementation.
- Explain architectural decisions and pattern trade-offs.
- Include a test plan, results, defect log, limitations, and conclusions.
- Use Harvard-cited research.
- Use diagrams and screenshots as evidence rather than decoration.

---

## 3. Fixed requirements and permitted customisation

### 3.1 Requirements that should remain unchanged

- Java implementation.
- IWFC health-and-fitness-centre domain.
- Administrator, Instructor, and Member roles.
- Equipment inventory and deactivation.
- Unique equipment ID, status, location, and usage hours.
- Preventative-maintenance alerts.
- Session viewing and booking.
- Prevention of studio and equipment double-booking.
- Fault reporting with the specified fields.
- Maintenance assignment and status updates.
- Automatic status-change notifications.
- 10–15 well-structured classes, unless clarified otherwise.
- Collections and generics.
- Abstraction, encapsulation, and polymorphism.
- At least one creational, structural, and behavioural pattern.
- Custom exceptions for invalid booking, unauthorised access, and duplicate data.
- JUnit testing of scheduling and maintenance workflows.
- GitHub or Bitbucket.
- Approximately 3,000-word report.
- Approximately 10-minute video and accompanying slides.
- Code walkthrough, running demonstration, testing discussion, and reflection.
- Video link on the first presentation slide.

### 3.2 Areas where customisation is permitted

- Fitness-centre persona, scale, and operating assumptions.
- Additional equipment, session, fault, and user attributes.
- Choice of pattern within each required category.
- Collection and generic abstraction choices.
- Console, JavaFX, Swing, or another suitable Java interface.
- Internal architecture and package structure.
- Generated or seeded datasets.
- Recurring sessions.
- Additional validation rules.
- Accessibility features.
- Notification presentation or channels.
- Dashboards and reports.
- Risk scoring and maintenance policies.
- Audit logging.
- Evaluation methods.
- Testing depth and tooling.
- Repository and developer documentation.

Customisation should extend the required domain rather than replace it.

---

## 4. Five original and feasible differentiation ideas

### Idea 1: Explainable Smart Maintenance and Operations

#### Concept

Add an explainable equipment-health engine that prioritises preventative maintenance.

#### Features

- Equipment health score.
- Risk bands: Normal, Monitor, Service Soon, and Critical.
- Score based on usage hours, fault frequency, urgency, and time since maintenance.
- Human-readable explanation for every result.
- Configurable maintenance strategies.
- Prioritised administrator work queue.
- Health and fault-trend charts.
- Audit history showing what changed.
- Maintenance due-date simulation.

#### Technologies and patterns

- Java 21 or the lecturer-approved version.
- JavaFX for an optional GUI.
- Maven or Gradle.
- JUnit 5 and Mockito.
- Factory for equipment creation.
- Facade for centre operations.
- Observer for maintenance notifications.
- Strategy for alternative maintenance policies.
- Generic `Repository<T, ID>` if the class-count interpretation permits it.
- `HashMap` for unique equipment lookup.
- `ArrayList` for chronological logs.
- Java Time API.

#### Difficulty and time

- Difficulty: medium-high.
- Core implementation: about three weeks.
- UI, testing, evaluation, and documentation: another two to three weeks.

#### Risks

- The risk formula could appear arbitrary.
- Pattern and exception classes could exceed the 15-class limit.
- Dashboard work could consume time without improving OOP marks.

#### Mitigation

- Base thresholds on cited literature or transparent domain assumptions.
- Compare two deterministic strategies rather than claiming prediction accuracy.
- Keep every score explainable.
- Maintain a compact architecture.

#### Academic value

- LO1: encapsulated equipment state and evaluated abstractions.
- LO2: generic policy evaluation, collections, and polymorphic strategies.
- LO3: safe handling of invalid transitions and access.
- LO4: patterns solving real workflow problems.
- LO5: non-trivial Java logic, tests, and performance evaluation.

### Idea 2: Constraint-Aware Fair Booking Engine

#### Concept

Create an explainable scheduling system that resolves room, equipment, instructor, capacity, maintenance, and fairness constraints.

#### Features

- Interval-based clash detection.
- Studio, equipment, and instructor conflict validation.
- Waiting list and cancellation promotion.
- Recurring-session generation.
- Transparent priority rules.
- Alternative-slot suggestions.
- Human-readable rejection explanations.
- Utilisation and rejection-reason visualisations.

#### Technologies and patterns

- Strategy for priority rules.
- Command for booking and cancellation operations.
- Facade for scheduling.
- Builder or Factory for recurring sessions.
- Indexed collections.
- JUnit parameterised tests.
- Optional jqwik property-based tests.

#### Difficulty and time

- Difficulty: high.
- Estimated time: four to six weeks.

#### Risks

- Date/time edge cases expand quickly.
- Fairness rules can be subjective.
- Recurrence and cancellations add class-count pressure.

#### Academic value

This option makes algorithms, polymorphism, collections, generics, exception handling, and systematic testing highly visible. It is particularly strong for LO2, LO3, and LO5.

### Idea 3: Inclusive and Privacy-Aware Wellness Access

#### Concept

Add accessibility-aware session discovery and privacy-conscious user preferences without attempting medical diagnosis.

#### Features

- Member accessibility preferences.
- Low-impact, sensory-friendly, wheelchair-accessible, and beginner-friendly labels.
- Accessible session filtering.
- Data minimisation and privacy consent.
- Role-based access to sensitive preferences.
- Keyboard-accessible and scalable JavaFX interface.
- Anonymous accessibility-demand reports.

#### Technologies and patterns

- Strategy for eligibility and filtering.
- Adapter for notification channels.
- Factory for user roles.
- Facade for access-controlled operations.
- JavaFX accessibility properties.
- JUnit security and privacy tests.

#### Difficulty and time

- Difficulty: medium.
- Estimated time: three to five weeks.

#### Risks

- Health-related fields introduce ethical and privacy concerns.
- Accessibility claims need evaluation.
- Technical depth may appear lower than Ideas 1 and 2 unless carefully implemented.

#### Academic value

This is particularly strong for EDGE attributes, secure design, user-centred evaluation, encapsulation, and role-based exception handling.

### Idea 4: IWFC Digital-Twin Simulation

#### Concept

Create a simulation mode for a week of equipment usage, bookings, faults, and maintenance decisions.

#### Features

- Deterministic seeded simulation.
- Simulated usage accumulation.
- Configurable demand and failure scenarios.
- Reactive-versus-preventative maintenance comparison.
- Utilisation, downtime, and rejected-booking metrics.
- Replayable evaluation scenarios.
- Exportable results.

#### Technologies and patterns

- Builder for simulation scenarios.
- Strategy for maintenance policies.
- Observer for simulation events.
- Facade or Composite for orchestration.
- Seeded `Random` for reproducibility.
- JavaFX charts or CSV output.

#### Difficulty and time

- Difficulty: high.
- Estimated time: five to seven weeks.

#### Risks

- The simulation can become a second project.
- Simulated findings cannot be presented as real-world evidence.
- Significant pressure on the class limit.
- Core requirements could receive insufficient attention.

#### Academic value

This provides the strongest visualisation, data storytelling, experimentation, and performance evidence, but carries the greatest delivery risk.

### Idea 5: Event-Driven Maintenance Accountability

#### Concept

Model important actions as auditable events to create a transparent timeline of who changed what and why.

#### Features

- Append-only activity timeline.
- Maintenance-state transition history.
- Notification history.
- Role-based action permissions.
- Selected undoable commands where safe.
- Resolution-time and overdue-task metrics.
- Administrator accountability dashboard.
- Search by user, equipment, urgency, and status.

#### Technologies and patterns

- Command for operations.
- Observer for notifications and audit events.
- Facade for use cases.
- Factory for event creation.
- Generic event log.
- Java Streams for metrics.
- JUnit tests for ordering, permissions, and transitions.

#### Difficulty and time

- Difficulty: medium-high.
- Estimated time: four to five weeks.

#### Risks

- Complete event sourcing would be excessive.
- Undo is unsafe for some actions.
- Audit logging may seem administrative without clear evaluation.

#### Academic value

This offers strong evidence of behavioural patterns, encapsulation, secure operations, polymorphic events, collections, and traceable testing.

---

## 5. Comparison of the five ideas

Scores are relative, where 5 is strongest.

| Idea | Originality | Feasibility | Technical depth | User value | Low development risk | Distinction potential |
|---|---:|---:|---:|---:|---:|---:|
| Explainable smart maintenance | 4.5 | 4.5 | 4.5 | 5.0 | 4.0 | 5.0 |
| Fair booking engine | 4.0 | 3.5 | 5.0 | 4.5 | 3.0 | 4.5 |
| Inclusive/privacy-aware access | 4.5 | 4.5 | 3.5 | 4.5 | 4.0 | 4.0 |
| Digital-twin simulation | 5.0 | 2.5 | 5.0 | 4.0 | 2.0 | 4.5 |
| Event-driven accountability | 4.0 | 4.0 | 4.5 | 4.0 | 3.5 | 4.5 |

### Overall ranking

1. **Explainable smart maintenance** — best balance of originality, value, evidence, and feasibility.
2. **Constraint-aware fair booking** — strongest algorithmic alternative.
3. **Event-driven accountability** — strongest architecture and pattern alternative.
4. **Digital twin** — highest ceiling but highest scope risk.
5. **Inclusive/privacy-aware access** — valuable and distinctive, but slightly less technically deep unless extended carefully.

---

## 6. Recommended option

The strongest option is **Explainable Smart Maintenance and Operations**.

It directly deepens the compulsory preventative-maintenance requirement rather than adding an unrelated feature. It creates a coherent assessment narrative:

1. Equipment accumulates usage.
2. Faults and maintenance events affect equipment condition.
3. A strategy calculates an explainable risk score.
4. Administrators receive prioritised alerts.
5. Observer-based notifications communicate changes.
6. Tests validate scoring and workflow rules.
7. Visualisations show risk, downtime, and maintenance outcomes.
8. The report evaluates alternative strategies and their limitations.
9. The presentation demonstrates the complete workflow.

A small feature from Idea 2, such as explainable alternative-slot suggestions, should be considered only after every essential requirement is complete and tested.

---

## 7. Distinction-level feature roadmap

### Phase 0: Confirm constraints

Confirm:

- exact due date;
- available weekly development hours;
- approved Java version;
- whether JavaFX dependencies are allowed;
- interpretation of the 10–15-class rule;
- meaning of "intentional failing test";
- exact submission formats.

### Phase 1: Requirements and evidence planning

#### Essential

- Convert every compulsory requirement into a numbered acceptance criterion.
- Create a requirement-to-class-to-test traceability matrix.
- Define operating hours and scheduling-overlap rules.
- Define valid maintenance transitions.
- Define role permissions.
- Document all assumptions.

#### Distinction enhancement

Define evaluation questions before implementation, such as:

- Does indexed lookup improve equipment retrieval?
- Which maintenance strategy produces the most useful prioritisation?
- Are risk explanations understandable?

### Phase 2: Architecture

A possible compact model is:

#### Entities

1. `User` — abstract.
2. `Administrator`.
3. `Instructor`.
4. `Member`.
5. `Equipment`.
6. `FitnessSession`.
7. `Booking`.
8. `MaintenanceRequest`.

#### Services and infrastructure

9. `GenericRepository<T, ID>`.
10. `BookingService`.
11. `MaintenanceService`.
12. `IWFCFacade`.

#### Pattern-focused abstractions

13. `EntityFactory`.
14. `MaintenancePolicy` with compact strategy implementations.
15. `NotificationObserver` with concrete delivery behaviour.

This is provisional. Custom exceptions, enums, implementations, and UI controllers could cause the total to exceed 15, so the counting rule must be confirmed first.

#### Architecture rules

- Use immutable IDs.
- Use enums for status, urgency, and maintenance state.
- Use `LocalDateTime` rather than strings for scheduling.
- Isolate UI code from domain logic.
- Inject repositories and observers into services.
- Do not allow UI classes to modify collections directly.

### Phase 3: Core functionality

#### Essential

- Equipment creation, editing, and deactivation.
- Usage logging and threshold alerts.
- Session creation and viewing.
- Booking and clash prevention.
- Instructor fault reporting.
- Administrator assignment and status updates.
- Automatic notifications.
- Role enforcement.
- Required custom exceptions.

#### Optional

- Recurring sessions.
- Waiting list.
- Suggested alternative sessions.
- Equipment risk score.
- Maintenance priority queue.
- Audit timeline.

### Phase 4: Professional UI/UX

#### Essential quality

Even a console interface should have:

- role-specific menus;
- consistent commands;
- no raw stack traces shown to users;
- confirmation before destructive changes;
- clear recovery instructions;
- readable tabular output;
- sensible defaults.

#### Recommended JavaFX enhancement

- Role-specific dashboards.
- Persistent navigation.
- Status chips using text as well as colour.
- Calendar or session list.
- Equipment health cards.
- Maintenance priority queue.
- Empty, success, and error states.
- Confirmation before equipment deactivation.
- Inline validation.
- Clear booking-conflict explanations.

The UI should support the demonstration and usability evidence without replacing architecture and testing work.

### Phase 5: Accessibility

#### Essential

- Keyboard-operable workflows.
- Logical tab order.
- Visible focus indicators.
- Text labels for controls.
- No status communicated through colour alone.
- Clear validation messages.
- Plain-language terminology.
- Adequate contrast.
- Resizable text and layout.

#### Optional evaluation

- WCAG-informed heuristic review.
- Task-based usability test with three to five participants.
- Record completion rates, errors, and qualitative feedback.

### Phase 6: Security and privacy

#### Essential

- Central role and permission checks.
- `UnauthorizedAccessException`.
- Input validation at application boundaries.
- No internal stack traces exposed to ordinary users.
- No plain-text passwords if authentication is added.
- No secrets or participant data committed to Git.
- Anonymous test data.

#### Optional

- Password hashing if real credentials are introduced.
- Audit log for privileged actions.
- Automatic session timeout.
- Privacy notice and consent for usability testing.
- Anonymised exports.

If authentication is only simulated, document that limitation honestly.

### Phase 7: Error handling

Document an exception hierarchy covering:

- invalid bookings;
- operation outside opening hours;
- duplicate equipment or data;
- unauthorised actions;
- invalid maintenance transitions;
- missing entities;
- invalid input.

Every exception should have a clear domain purpose, tests, and a safe user-facing message. Avoid empty `catch` blocks and overly broad exception handling.

### Phase 8: Testing

#### Essential JUnit cases

- Booking at opening and closing boundaries.
- Exact duplicate booking.
- Partial overlap at the start.
- Partial overlap at the end.
- One interval contained inside another.
- Adjacent non-overlapping sessions.
- Same time in different studios.
- Same equipment assigned to different sessions.
- Booking deactivated or faulty equipment.
- Duplicate equipment ID.
- Unauthorised maintenance-log access.
- Valid and invalid maintenance transitions.
- Notification triggered exactly once per status change.

#### Distinction enhancements

- JUnit 5 parameterised tests.
- Mockito tests for observers and repositories.
- Property-based scheduling tests where permitted.
- JaCoCo coverage report.
- PIT mutation testing.
- Performance comparison of linear and indexed lookup.
- Defect log connecting failures to fixes.
- Regression tests for corrected defects.

Do not leave an unexplained failing test in the normal build. If the lecturer requires a deliberately red test, preserve it separately and explain its purpose.

### Phase 9: Performance

#### Essential

- Justify collection selection.
- Avoid repeated full-list searches when indexed lookup is appropriate.
- Test behaviour with increasing generated-data volumes.
- Report environment, dataset size, repetition count, and limitations.

#### Optional

- JMH benchmarking.
- Booking-conflict index by room and date.
- Cached dashboard summaries with controlled invalidation.

### Phase 10: Evaluation

A distinction-level evaluation should include:

- functional requirement coverage;
- test results;
- coverage and mutation-test results;
- performance results;
- accessibility/usability findings;
- comparison of maintenance strategies;
- documented defects and fixes;
- remaining limitations;
- threats to validity;
- evidence-based recommendations.

Useful visualisations include:

- tests by category and result;
- defect discovery and correction timeline;
- equipment risk distribution;
- maintenance requests by urgency and status;
- lookup or validation time by dataset size;
- requirement coverage.

Every visualisation should answer a defined question and be accompanied by interpretation.

### Phase 11: Documentation

#### Repository documentation

- README with setup, build, test, and run instructions.
- Java version and dependency details.
- Sample roles or credentials where applicable.
- Architecture overview.
- Known limitations.
- Testing and coverage commands.
- Meaningful Git history.

#### Suggested 3,000-word report allocation

1. Introduction and problem rationale — 250 words.
2. Research and requirements — 350 words.
3. Architecture and class design — 500 words.
4. OOP, generics, and collections — 400 words.
5. Design patterns and alternatives — 450 words.
6. Exception handling and security — 250 words.
7. Testing and results — 450 words.
8. Evaluation, limitations, and conclusion — 350 words.

Essential arguments and results must remain in the main report because the brief says appendix content is not normally considered when determining the final grade.

#### Suggested 10-minute presentation structure

- 0:00–0:40 — problem and originality.
- 0:40–1:40 — architecture and class diagram.
- 1:40–2:40 — OOP, collections, and generics.
- 2:40–4:10 — required pattern categories.
- 4:10–5:00 — exceptions and security.
- 5:00–6:50 — live application demonstration.
- 6:50–8:20 — tests, defects, and corrections.
- 8:20–9:20 — evaluation and limitations.
- 9:20–10:00 — reflection and conclusion.

---

## 8. Scope control

### 8.1 Non-negotiable baseline

- All three required workflows.
- All three actors.
- 10–15 classes.
- Java collections and generics.
- Abstraction, encapsulation, and polymorphism.
- One pattern from each required category.
- Three mandatory custom-exception areas.
- JUnit coverage of booking and maintenance.
- Source control.
- Report.
- Video, slides, code walkthrough, and running demonstration.

### 8.2 Distinction-critical enhancements

- Requirement traceability.
- Explainable maintenance policy.
- Strong and accurate class diagram.
- Pattern trade-off evaluation.
- Comprehensive boundary and negative testing.
- Defect and regression evidence.
- Visualised findings.
- Performance evaluation.
- Explicit limitations.
- Polished README and presentation.

### 8.3 Add only if time remains

- JavaFX interface.
- Recurring sessions.
- Waiting list.
- Alternative-slot suggestions.
- CSV import/export.
- JMH benchmarks.
- Mutation testing.
- Small usability study.
- Audit timeline.

### 8.4 Avoid unless explicitly approved

- Machine learning.
- Mobile application.
- Cloud deployment.
- Microservices.
- Real payment processing.
- Real medical data.
- Wearable-device integration.
- Complex database or authentication platform.
- Multiple external APIs.
- Full event sourcing.
- Large analytics subsystem.

These additions could increase scope without improving performance against the stated learning outcomes.

---

## 9. Unclear or conflicting requirements requiring confirmation

1. **The rubric appears to be reused from another assignment.** It refers to social computing, data sources/APIs, visualisations, and storytelling even though the Java IWFC prototype does not require an API or external dataset.
2. **"Intentional failing test" is ambiguous.** Confirm whether this means a passing negative test using `assertThrows`, or an actual red test retained as evidence.
3. **The 10–15-class counting rule is unclear.** Confirm whether exception classes, enums, interfaces, records, JavaFX controllers, tests, nested classes, and concrete implementations count.
4. **Submission formats conflict.** Moodle/Turnitin requests PDF, while ICBT SIS requests Word format. Confirm whether both are required.
5. **Submission times differ.** PRAC1 states 2:00 pm, while PRES1 states 4:00 pm. Exact dates are deferred to Moodle.
6. **Presentation submission is unclear.** Confirm whether Moodle requires the slide deck as PDF, the original PowerPoint, both formats, or a separate document containing the video link.
7. **PRES1 specifies both 10 minutes and a 1,000-word equivalent.** Confirm whether the word count applies to slides, notes, a script, or only estimated workload.
8. **GUI and visual expectations are inconsistent.** The practical brief prioritises architecture and OOP over aesthetics, while the generic rubric mentions excellent design and visualisations.
9. **The report structure is not prescribed.** The structure proposed in this analysis is a recommendation, not a contractual template.
10. **Database integration is optional but not explicitly discouraged.** Given the class limit and learning outcomes, in-memory repositories are safer unless persistence is approved.
11. **Operating hours are referenced but not defined.** They must be confirmed or recorded as an explicit system assumption.
12. **Notification delivery is unspecified.** An in-application notification log should be sufficient, but email or SMS is not expressly required.
13. **The academic level and pass mark are unclear.** The template states 40% for undergraduate and 50% for postgraduate work.

---

## 10. Recommended next decision

Before implementation begins:

1. Confirm the ambiguous requirements with the lecturer.
2. Confirm the deadline and available development hours.
3. Approve or revise the Explainable Smart Maintenance direction.
4. Freeze an acceptance-criteria list.
5. Design the class model only after the 10–15-class counting rule is known.

Do not start optional enhancements until every compulsory requirement has an implementation and a passing test.
