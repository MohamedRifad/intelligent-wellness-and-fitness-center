# IWFC Requirements Traceability Matrix

## Purpose

This matrix ensures that no assignment requirement disappears between planning, implementation, testing, the report, and the presentation.

## Functional traceability

| Requirement | Planned implementation | Main test evidence | Phase |
|---|---|---|---:|
| FR-U01–U04 | `User`, role subclasses, `GenericRepository<User>`, `EntityFactory` | Unique accounts, duplicate ID, deactivation | 3–5 |
| FR-U05 | Facade/service permission checks | Member and Instructor denied privileged operations | 5–8 |
| FR-E01–E03 | Administrator operations through `IWFCFacade` | Add, edit and deactivate cases | 5 |
| FR-E04–E06 | `Equipment` and generic repository | Required fields and duplicate ID | 3–5 |
| FR-E07–E09 | Equipment usage method and threshold alert | Positive usage, invalid usage and threshold boundary | 5 |
| FR-E10 | Booking/scheduling equipment validation | Each unavailable status rejected | 6 |
| FR-S01–S04 | `FitnessSession` and `BookingService` | Valid schedule, invalid interval and operating hours | 6 |
| FR-S05–S07 | Interval conflict validator | Exact, partial, contained and adjacent cases | 6 |
| FR-S08 | Console schedule listing | Role-oriented integration test/demo | 8–9 |
| FR-S09 | Optional recurrence generator | Weekly generation and conflict rollback | Optional |
| FR-B01–B03 | `FitnessSession`, `BookingService`, Member menu | Availability and valid booking | 7 |
| FR-B04–B06 | Booking validation | Duplicate, capacity and unknown-session cases | 7 |
| FR-B07–B08 | Member booking history and Observer notification | History and changed-session notification | 7–8 |
| FR-M01–M05 | `MaintenanceRequest`, `MaintenanceService` | Valid report and initial state | 8 |
| FR-M06–M09 | Administrator maintenance operations | Permission and transition tests | 8 |
| FR-M10–M12 | Controlled equipment-status updates | Fault, assignment and completion states | 8 |
| FR-M13 | Observer registration and notification | Exactly one notification per state change | 8 |

## Technical traceability

| Requirement | Planned evidence | Report location | Presentation evidence |
|---|---|---|---|
| TR-01–TR-03 | Maven Java project and 10–15-type architecture | Architecture section | Class diagram |
| TR-04 | Ordered lists for sessions, bookings, logs, notifications | Collections subsection | Code walkthrough |
| TR-05 | ID-indexed repositories | Collections subsection | Repository code |
| TR-06 | `GenericRepository<T>` used by multiple entities | Generics subsection | Generic class demonstration |
| TR-07 | Abstract `User` | OOP subsection | Class diagram |
| TR-08 | Private fields and validated state methods | OOP subsection | Entity code |
| TR-09 | Role/observer behaviour through base references | OOP subsection | Running notification demo |
| TR-10 | `EntityFactory` | Patterns subsection | Factory code |
| TR-11 | `IWFCFacade` | Patterns subsection | Console-to-facade flow |
| TR-12 | `User.receiveNotification` observers and `MaintenanceService` publisher | Patterns subsection | Status-change demo |
| TR-13 | Three custom exception types | Exception section | Error demonstrations |
| TR-14 | Role-specific console menus | Implementation section | Live application demo |
| TR-15 | Seeded in-memory data | Implementation/limitations | Startup demonstration |

## Testing traceability

| Requirement | Planned JUnit coverage |
|---|---|
| QR-01 | Parameterized interval-overlap tests, availability tests, booking tests |
| QR-02 | Pending-to-Assigned-to-Completed and invalid transition tests |
| QR-03 | `InvalidBookingException`, `UnauthorizedAccessException`, and `DuplicateDataException` tests |
| QR-04 | `assertThrows` test intentionally attempts a conflicting booking |
| QR-05 | Happy path, boundaries, invalid input, unavailable resources, and permissions |
| QR-06 | Maven test report showing all normal tests pass |
| QR-07 | Console-level exception mapping test or demonstration |

## Assessment-deliverable traceability

| Requirement | Evidence to retain |
|---|---|
| DR-01–DR-03 | GitHub URL, commit history, tags, README, build/test/run commands |
| DR-04 | Final approximately 3,000-word PRAC1 report in required layout |
| DR-05 | Harvard citations, reference list, coversheet, feedback sheet |
| DR-06 | Slides, script, code walkthrough, test evidence, bugs, reflection, live demo |
| DR-07 | Tested YouTube or OneDrive URL on slide 1 |
| DR-08 | Correct student-ID-based filenames |

## Planned report evidence by phase

| Phase | Evidence to save |
|---:|---|
| 1 | Requirement list, assumptions, permission matrix, traceability matrix |
| 2 | Class responsibilities, UML plan, pattern choices and alternatives |
| 3 | Maven structure and successful initial build |
| 4 | Generic repository, factory, facade and observer explanation |
| 5 | Equipment and user-management screenshots/tests |
| 6 | Scheduling conflict cases and corrected defects |
| 7 | Member booking and capacity tests |
| 8 | Maintenance transitions and notification evidence |
| 9 | Complete console workflow screenshots |
| 10 | JUnit results, coverage, defect log and regression tests |
| 11 | Final evaluation, limitations and conclusions |

## Definition of done

A requirement is not complete merely because code exists. It is complete only when:

1. Its implementation works through the intended application layer.
2. Its normal and error behaviour have been tested.
3. The traceability matrix points to its evidence.
4. Its design can be explained in the report and presentation.
