# Intelligent Wellness and Fitness Center

IWFC is a Java console prototype for equipment oversight, fitness-session scheduling, member bookings, maintenance reporting, and automatic notifications.

## Current status

The prototype contains the complete validated administration, scheduling, booking,
maintenance, equipment-usage, notification, and guided console-demonstration workflows.
It preserves the required 15-file production architecture.

## Requirements

- JDK 21 or newer
- Apache Maven 3.9 or newer

The project compiles against Java 21 bytecode for compatibility.

## Build and test

From PowerShell in the project directory, using the bundled Maven runtime:

```powershell
.\.tools\maven\apache-maven-3.9.16\bin\mvn.cmd --batch-mode clean test
```

If Maven 3.9+ is installed on `PATH`, the equivalent command is `mvn clean test`.

## Launch the console demonstration

Build the runnable JAR without repeating the tests, then launch the application:

```powershell
.\.tools\maven\apache-maven-3.9.16\bin\mvn.cmd --batch-mode -DskipTests package
java -cp target\iwfc-management-system-1.0.0-SNAPSHOT.jar iwfc.app.IWFCFacade
```

The console performs one-time Administrator setup. Choose option `1` to run the
guided end-to-end demonstration and option `0` to exit.

For a strict Java 21 production compilation check:

```powershell
$sourceFiles = Get-ChildItem src/main/java -Recurse -Filter *.java | ForEach-Object FullName
javac --release 21 -Xlint:all -Werror -d target/strict-java21 $sourceFiles
```

## Documentation

- `docs/phase-01/` contains requirements and traceability.
- `docs/phase-02/` contains the architecture and PlantUML class diagram.
- `docs/phase-03/` explains the project foundation.
- `docs/phase-04/` documents the implemented business batches.
- `docs/phase-05/` documents end-to-end integration verification.
