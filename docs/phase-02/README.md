# Phase 2 Detailed System Design

## Purpose

This phase defines how the IWFC requirements will be implemented before Java code is written. It fixes the class count, responsibilities, relationships, patterns, and important method contracts.

## Outputs

- `architecture.md` explains the 15-class design in simple technical language.
- `class-diagram.puml` is PlantUML source for the planned class diagram.

## Design approach

The design uses exactly 15 top-level production classes:

- Seven domain classes for users, equipment, sessions, and maintenance.
- One generic repository.
- Three application/design-pattern classes.
- One facade and application entry point.
- Three custom exception classes.

A separate `Booking` class is deliberately not used. `FitnessSession` stores booked Member IDs in a collection, and `BookingService` controls all booking behaviour. This preserves every booking requirement while keeping the top-level class count at 15.

## Patterns

- Factory: `EntityFactory`
- Facade: `IWFCFacade`
- Observer: `User` objects act as observers and `MaintenanceService` acts as the subject

## Phase gate

Phase 2 is complete when the class responsibilities, relationships, method contracts, and UML plan match every Phase 1 requirement. Phase 3 will create the Maven and Git foundation and then implement only the minimum class skeletons needed for a successful build.
