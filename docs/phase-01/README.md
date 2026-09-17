# Phase 1 Requirements and Planning

## Purpose

This phase converts the IWFC assignment brief into precise rules that can be implemented and tested. No Java code is created in this phase. Freezing the requirements first prevents missing a feature or building conflicting behaviour later.

## What was produced

- `requirements.md` defines the actors, functional requirements, technical requirements, business rules, assumptions, and acceptance criteria.
- `traceability.md` connects every requirement to its planned classes, implementation phase, test evidence, report section, and presentation evidence.

## How this phase was completed

1. Read both official assessment briefs.
2. Separated mandatory requirements from optional enhancements.
3. Identified duties stated only in the actor descriptions, such as user-account management and equipment-usage tracking.
4. Converted each requirement into a numbered, testable statement.
5. Defined scheduling, authorization, equipment, maintenance, and notification rules.
6. Recorded assumptions where the brief does not provide an exact rule.
7. Mapped each requirement to planned implementation and assessment evidence.

## Important decisions

- The project name remains **Intelligent Wellness and Fitness Center (IWFC)**.
- The first implementation will be a Java console application.
- Data will be stored in memory using Java collections; no database is required.
- Factory, Facade, and Observer will satisfy the three required pattern categories.
- Recurring weekly sessions are optional and will be attempted only after all mandatory requirements pass their tests.
- A maintenance alert threshold of 100 cumulative usage hours is an initial system assumption and will be configurable as a constant.
- Operating hours are initially 06:00 to 22:00 daily because the brief requires validation but does not define the hours.
- The brief's "intentional failing test" is interpreted as an operation that intentionally triggers a custom exception and is verified with JUnit `assertThrows`. The normal project build will remain green.

## Completion checklist

- [x] Actors and permissions identified.
- [x] Equipment requirements specified.
- [x] Scheduling and booking requirements specified.
- [x] Maintenance workflow specified.
- [x] Notifications specified.
- [x] Java, OOP, collection, generic, pattern, and exception requirements specified.
- [x] JUnit expectations specified.
- [x] GitHub, report, and presentation deliverables recorded.
- [x] Each requirement has an acceptance criterion.
- [x] Each requirement has planned implementation and evidence.

## Phase gate

Phase 1 is complete when the requirements and assumptions are accepted. Phase 2 will then create the detailed 10–15-class design, package structure, method contracts, and class diagram plan before any implementation begins.
