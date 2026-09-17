package iwfc.app;

import iwfc.domain.Equipment;
import iwfc.domain.FitnessSession;
import iwfc.domain.MaintenanceRequest;
import iwfc.domain.User;
import iwfc.exception.DuplicateDataException;
import iwfc.exception.InvalidBookingException;
import iwfc.exception.UnauthorizedAccessException;
import iwfc.pattern.EntityFactory;
import iwfc.repository.GenericRepository;
import iwfc.service.BookingService;
import iwfc.service.MaintenanceService;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/** Structural Facade and console entry point for IWFC. */
public final class IWFCFacade {
    private final GenericRepository<User> userRepository = new GenericRepository<>(User::getId);
    private final GenericRepository<Equipment> equipmentRepository = new GenericRepository<>(Equipment::getId);
    private final GenericRepository<FitnessSession> sessionRepository = new GenericRepository<>(FitnessSession::getId);
    private final GenericRepository<MaintenanceRequest> maintenanceRepository =
            new GenericRepository<>(MaintenanceRequest::getId);
    private final EntityFactory entityFactory = new EntityFactory();
    private final BookingService bookingService = new BookingService(
            userRepository, equipmentRepository, sessionRepository, LocalTime.of(6, 0), LocalTime.of(22, 0));
    private final MaintenanceService maintenanceService = new MaintenanceService(
            userRepository, equipmentRepository, sessionRepository, maintenanceRepository);

    public EntityFactory getEntityFactory() { return entityFactory; }
    public BookingService getBookingService() { return bookingService; }
    public MaintenanceService getMaintenanceService() { return maintenanceService; }

    /**
     * Creates the first Administrator through the Factory. This operation is
     * available only while the user repository is empty; all later account
     * creation requires an authorized Administrator.
     */
    public User initializeAdministrator(String id, String name) throws DuplicateDataException {
        if (!userRepository.findAll().isEmpty()) {
            throw new DuplicateDataException("IWFC has already been initialized");
        }
        User administrator = entityFactory.createUser(User.Role.ADMINISTRATOR, id, name);
        userRepository.add(administrator);
        maintenanceService.registerObserver(administrator);
        return administrator;
    }

    public User registerUser(User actor, User.Role role, String id, String name)
            throws UnauthorizedAccessException, DuplicateDataException {
        requireAdministrator(actor);
        User user = entityFactory.createUser(role, id, name);
        userRepository.add(user);
        maintenanceService.registerObserver(user);
        return user;
    }

    public List<User> viewUsers(User actor) throws UnauthorizedAccessException {
        requireAdministrator(actor);
        return userRepository.findAll();
    }

    public void deactivateUser(User actor, String userId) throws UnauthorizedAccessException {
        requireAdministrator(actor);
        requireUser(userId).deactivate();
    }

    public Equipment addEquipment(User actor, String id, String name, String location)
            throws UnauthorizedAccessException, DuplicateDataException {
        requireAdministrator(actor);
        Equipment equipment = entityFactory.createEquipment(id, name, location);
        equipmentRepository.add(equipment);
        return equipment;
    }

    public void updateEquipment(User actor, String equipmentId, String name, String location)
            throws UnauthorizedAccessException {
        requireAdministrator(actor);
        requireEquipment(equipmentId).updateDetails(name, location);
    }

    public void deactivateEquipment(User actor, String equipmentId)
            throws UnauthorizedAccessException {
        requireAdministrator(actor);
        requireEquipment(equipmentId).deactivate();
    }

    public List<Equipment> viewEquipment(User actor) throws UnauthorizedAccessException {
        requireAdministrator(actor);
        return equipmentRepository.findAll();
    }

    public FitnessSession scheduleSession(User actor, String sessionId, String title,
                                          LocalDateTime startTime, LocalDateTime endTime,
                                          String studio, Collection<String> equipmentIds,
                                          int capacity)
            throws UnauthorizedAccessException, InvalidBookingException, DuplicateDataException {
        User instructor = requireRole(actor, User.Role.INSTRUCTOR);
        return bookingService.scheduleSession(sessionId, title, instructor.getId(), studio,
                startTime, endTime, capacity, equipmentIds);
    }

    public void bookSession(User actor, String sessionId)
            throws UnauthorizedAccessException, InvalidBookingException {
        User member = requireRole(actor, User.Role.MEMBER);
        bookingService.bookSession(member.getId(), sessionId);
    }

    public List<FitnessSession> viewAvailableSessions(User actor)
            throws UnauthorizedAccessException {
        requireRole(actor, User.Role.MEMBER);
        return bookingService.findAvailableSessions();
    }

    public List<FitnessSession> viewMyBookings(User actor)
            throws UnauthorizedAccessException, InvalidBookingException {
        User member = requireRole(actor, User.Role.MEMBER);
        return bookingService.findSessionsForMember(member.getId());
    }

    public MaintenanceRequest reportFault(User actor, String requestId, String equipmentId,
                                          String description, MaintenanceRequest.Urgency urgency)
            throws UnauthorizedAccessException, DuplicateDataException {
        User instructor = requireRole(actor, User.Role.INSTRUCTOR);
        return maintenanceService.reportFault(
                requestId, equipmentId, description, urgency, instructor.getId());
    }

    public void assignMaintenance(User actor, String requestId, String assignee)
            throws UnauthorizedAccessException {
        requireAdministrator(actor);
        maintenanceService.assignRequest(requestId, assignee);
    }

    public void completeMaintenance(User actor, String requestId)
            throws UnauthorizedAccessException {
        requireAdministrator(actor);
        maintenanceService.completeRequest(requestId);
    }

    public List<MaintenanceRequest> viewMaintenanceRequests(User actor)
            throws UnauthorizedAccessException {
        requireAdministrator(actor);
        return maintenanceService.findAllRequests();
    }

    public void recordSessionEquipmentUsage(User actor, String sessionId,
                                            String equipmentId, double hours)
            throws UnauthorizedAccessException {
        User instructor = requireRole(actor, User.Role.INSTRUCTOR);
        maintenanceService.recordSessionEquipmentUsage(
                instructor.getId(), sessionId, equipmentId, hours);
    }

    public boolean checkPreventativeMaintenance(User actor, String equipmentId)
            throws UnauthorizedAccessException {
        requireAdministrator(actor);
        return maintenanceService.checkPreventativeMaintenance(equipmentId);
    }

    private User requireAdministrator(User actor) throws UnauthorizedAccessException {
        return requireRole(actor, User.Role.ADMINISTRATOR);
    }

    private User requireRole(User actor, User.Role requiredRole) throws UnauthorizedAccessException {
        if (actor == null) {
            throw new UnauthorizedAccessException("An authenticated user is required");
        }
        User registeredActor = userRepository.findById(actor.getId())
                .orElseThrow(() -> new UnauthorizedAccessException("User is not registered with IWFC"));
        if (registeredActor != actor) {
            throw new UnauthorizedAccessException("Authenticated user does not match the registered account");
        }
        if (!registeredActor.isActive()) {
            throw new UnauthorizedAccessException("Inactive users cannot perform operations");
        }
        if (registeredActor.getRole() != requiredRole) {
            throw new UnauthorizedAccessException(requiredRole + " access is required");
        }
        return registeredActor;
    }

    private User requireUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown user ID: " + userId));
    }

    private Equipment requireEquipment(String equipmentId) {
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown equipment ID: " + equipmentId));
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            runConsole(scanner, System.out);
        }
    }

    static void runConsole(Scanner scanner, PrintStream output) {
        Objects.requireNonNull(scanner, "Scanner is required");
        Objects.requireNonNull(output, "Output is required");
        IWFCFacade facade = new IWFCFacade();

        output.println("============================================================");
        output.println(" Intelligent Wellness and Fitness Center (IWFC)");
        output.println(" Presentation Console");
        output.println("============================================================");
        output.println("One-time Administrator setup");

        User administrator = null;
        while (administrator == null) {
            String administratorId = readRequired(scanner, output, "Administrator ID: ");
            if (administratorId == null) {
                output.println("Input ended. IWFC closed safely.");
                return;
            }
            String administratorName = readRequired(scanner, output, "Administrator name: ");
            if (administratorName == null) {
                output.println("Input ended. IWFC closed safely.");
                return;
            }
            try {
                administrator = facade.initializeAdministrator(administratorId, administratorName);
                output.println("[SUCCESS] Administrator account created for "
                        + administrator.getName() + ".");
            } catch (DuplicateDataException | IllegalArgumentException exception) {
                output.println("[ERROR] " + exception.getMessage());
            }
        }

        boolean running = true;
        while (running) {
            output.println();
            output.println("1 - Run guided IWFC workflow demonstration");
            output.println("0 - Exit");
            output.print("Select an option: ");
            if (!scanner.hasNextLine()) {
                output.println();
                output.println("Input ended. IWFC closed safely.");
                return;
            }

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> runGuidedDemonstration(facade, administrator, output);
                case "0" -> running = false;
                default -> output.println("[ERROR] Enter 1 to run the demo or 0 to exit.");
            }
        }
        output.println("Thank you for using IWFC.");
    }

    private static String readRequired(Scanner scanner, PrintStream output, String prompt) {
        while (true) {
            output.print(prompt);
            if (!scanner.hasNextLine()) return null;
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) return value;
            output.println("[ERROR] This value is required. Please try again.");
        }
    }

    private static void runGuidedDemonstration(IWFCFacade facade, User administrator,
                                               PrintStream output) {
        output.println();
        output.println("--- Guided IWFC workflow ---");
        try {
            output.println("1. Registering demonstration Instructor and Member...");
            User instructor = facade.registerUser(
                    administrator, User.Role.INSTRUCTOR, "DEMO-I1", "Nimal Perera");
            User member = facade.registerUser(
                    administrator, User.Role.MEMBER, "DEMO-M1", "Maya Silva");

            output.println("2. Adding fitness equipment...");
            Equipment equipment = facade.addEquipment(
                    administrator, "DEMO-EQ1", "Spin Bike", "Studio A");

            output.println("3. Scheduling a session...");
            LocalDate demonstrationDate = LocalDate.now().plusDays(1);
            FitnessSession session = facade.scheduleSession(
                    instructor, "DEMO-S1", "Morning Spin",
                    demonstrationDate.atTime(9, 0), demonstrationDate.atTime(10, 0),
                    "Studio A", List.of(equipment.getId()), 12);

            output.println("4. Booking the Member into the session...");
            facade.bookSession(member, session.getId());

            output.println("5. Recording session-equipment usage...");
            facade.recordSessionEquipmentUsage(
                    instructor, session.getId(), equipment.getId(), 2.5);

            output.println("6. Reporting an equipment fault...");
            MaintenanceRequest request = facade.reportFault(
                    instructor, "DEMO-R1", equipment.getId(), "Resistance control failure",
                    MaintenanceRequest.Urgency.HIGH);

            output.println("7. Administrator reviewing and assigning maintenance...");
            facade.viewMaintenanceRequests(administrator);
            facade.assignMaintenance(administrator, request.getId(), "Technician A");
            output.println("   Instructor notification: "
                    + instructor.getNotifications().getLast());

            output.println("8. Administrator completing maintenance...");
            facade.completeMaintenance(administrator, request.getId());
            output.println("   Instructor notification: "
                    + instructor.getNotifications().getLast());

            output.println("[STATUS] Session: " + session.getTitle()
                    + ", booked members: " + session.getBookedMemberIds().size());
            output.println("[STATUS] Equipment: " + equipment.getStatus()
                    + ", usage: " + equipment.getCumulativeUsageHours() + " hours");
            output.println("[STATUS] Maintenance request: " + request.getStatus());
            output.println("[SUCCESS] Guided IWFC workflow completed.");
        } catch (Exception exception) {
            output.println("[ERROR] Demonstration could not continue: " + exception.getMessage());
            output.println("[STATUS] The console remains available; select another option.");
        }
    }
}
