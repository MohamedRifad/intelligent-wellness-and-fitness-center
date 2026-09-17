# Phase 6 Console Demonstration

## Scope

Phase 6 adds a deliberately small presentation console inside the existing `IWFCFacade` entry point. No production file or type was added, and no validated business rule was changed.

## Console flow

1. Display the IWFC startup screen.
2. Request the one-time Administrator ID and name.
3. Reject blank setup values without terminating.
4. Present two choices: run the guided workflow or exit.
5. The guided workflow uses only public `IWFCFacade` operations to register an Instructor and Member, add equipment, schedule a session, book the Member, record usage, report a fault, review and assign maintenance, complete maintenance, and display notifications and final status.
6. Invalid menu input and workflow exceptions produce understandable messages and return control to the menu.
7. End-of-input closes the application safely.

The console does not read or mutate repositories or services directly.

## Automated console coverage

`IWFCFacadeConsoleTest` contains two passing tests:

- It supplies blank setup input, an invalid menu option, a successful guided workflow, a repeated workflow that safely reports duplicate data, and a normal exit.
- It verifies that end-of-input during initial setup closes safely without an exception.

## Commands

Run all tests:

```powershell
.\.tools\maven\apache-maven-3.9.16\bin\mvn.cmd --batch-mode clean test
```

Build and launch the console:

```powershell
.\.tools\maven\apache-maven-3.9.16\bin\mvn.cmd --batch-mode -DskipTests package
java -cp target\iwfc-management-system-1.0.0-SNAPSHOT.jar iwfc.app.IWFCFacade
```

## Verification

Strict Java 21 compilation:

```text
STRICT_JAVA21_COMPILE_SUCCESS
Production source files: 15
```

Maven and JUnit:

```text
[INFO] Compiling 15 source files with javac [debug release 21] to target\classes
[INFO] Compiling 14 source files with javac [debug release 21] to target\test-classes
[INFO] Running iwfc.app.IWFCFacadeConsoleTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] Tests run: 126, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Manual scripted launch result:

```text
[SUCCESS] Administrator account created for Asha.
[STATUS] Session: Morning Spin, booked members: 1
[STATUS] Equipment: OPERATIONAL, usage: 2.5 hours
[STATUS] Maintenance request: COMPLETED
[SUCCESS] Guided IWFC workflow completed.
Thank you for using IWFC.
```

## Files changed

Production file modified:

- `src/main/java/iwfc/app/IWFCFacade.java`

Test file added:

- `src/test/java/iwfc/app/IWFCFacadeConsoleTest.java`

Documentation modified or added:

- `README.md`
- `docs/phase-06/README.md`
