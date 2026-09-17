package iwfc.service;

import iwfc.domain.Equipment;
import iwfc.domain.FitnessSession;
import iwfc.domain.User;
import iwfc.exception.DuplicateDataException;
import iwfc.exception.InvalidBookingException;
import iwfc.repository.GenericRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/** Coordinates IWFC session scheduling and member booking rules. */
public final class BookingService {
    private final GenericRepository<User> userRepository;
    private final GenericRepository<Equipment> equipmentRepository;
    private final GenericRepository<FitnessSession> sessionRepository;
    private final LocalTime openingTime;
    private final LocalTime closingTime;

    public BookingService(GenericRepository<User> userRepository,
                          GenericRepository<Equipment> equipmentRepository,
                          GenericRepository<FitnessSession> sessionRepository,
                          LocalTime openingTime, LocalTime closingTime) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.equipmentRepository = Objects.requireNonNull(equipmentRepository);
        this.sessionRepository = Objects.requireNonNull(sessionRepository);
        this.openingTime = Objects.requireNonNull(openingTime);
        this.closingTime = Objects.requireNonNull(closingTime);
    }

    public FitnessSession scheduleSession(String sessionId, String title, String instructorId,
                                          String studio, LocalDateTime startTime,
                                          LocalDateTime endTime, int capacity,
                                          Collection<String> equipmentIds)
            throws InvalidBookingException, DuplicateDataException {
        validateOperatingHours(startTime, endTime);
        User instructor = requireActiveUser(instructorId, User.Role.INSTRUCTOR, "Instructor");

        FitnessSession candidate;
        try {
            candidate = new FitnessSession(sessionId, title, instructor.getId(), studio,
                    startTime, endTime, capacity, equipmentIds);
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new InvalidBookingException(exception.getMessage());
        }

        if (sessionRepository.containsId(candidate.getId())) {
            throw new DuplicateDataException("Duplicate session ID: " + candidate.getId());
        }

        validateEquipmentState(candidate);
        validateScheduleConflicts(candidate);
        sessionRepository.add(candidate);
        return candidate;
    }

    public void bookSession(String memberId, String sessionId) throws InvalidBookingException {
        User member = requireActiveUser(memberId, User.Role.MEMBER, "Member");
        String validatedSessionId;
        try {
            validatedSessionId = requireText(sessionId, "Session ID");
        } catch (IllegalArgumentException exception) {
            throw new InvalidBookingException(exception.getMessage());
        }
        FitnessSession session = sessionRepository.findById(validatedSessionId)
                .orElseThrow(() -> new InvalidBookingException("Unknown session ID: " + sessionId));

        if (!session.isActive()) {
            throw new InvalidBookingException("The session is not available for booking");
        }
        validateEquipmentState(session);
        if (session.containsMember(member.getId())) {
            throw new InvalidBookingException("Member is already booked into this session");
        }
        if (!session.hasCapacity()) {
            throw new InvalidBookingException("The session has reached capacity");
        }
        session.addMember(member.getId());
    }

    public List<FitnessSession> findAvailableSessions() {
        return sessionRepository.findAll().stream()
                .filter(FitnessSession::isActive)
                .filter(FitnessSession::hasCapacity)
                .filter(this::hasAvailableEquipment)
                .toList();
    }

    public List<FitnessSession> findSessionsForMember(String memberId) throws InvalidBookingException {
        User member = requireActiveUser(memberId, User.Role.MEMBER, "Member");
        return sessionRepository.findAll().stream()
                .filter(session -> session.containsMember(member.getId()))
                .toList();
    }

    private void validateOperatingHours(LocalDateTime startTime, LocalDateTime endTime)
            throws InvalidBookingException {
        if (startTime == null || endTime == null) {
            throw new InvalidBookingException("Session start and end times are required");
        }
        if (!endTime.isAfter(startTime)) {
            throw new InvalidBookingException("Session end time must be after start time");
        }
        if (!startTime.toLocalDate().equals(endTime.toLocalDate())) {
            throw new InvalidBookingException("A session must start and end on the same day");
        }
        if (startTime.toLocalTime().isBefore(openingTime)
                || endTime.toLocalTime().isAfter(closingTime)) {
            throw new InvalidBookingException(
                    "Session must be within operating hours " + openingTime + "-" + closingTime);
        }
    }

    private void validateEquipmentState(FitnessSession session) throws InvalidBookingException {
        for (String equipmentId : session.getEquipmentIds()) {
            Equipment equipment = equipmentRepository.findById(equipmentId)
                    .orElseThrow(() -> new InvalidBookingException("Unknown equipment ID: " + equipmentId));
            if (!equipment.isAvailableForScheduling()) {
                throw new InvalidBookingException("Equipment is unavailable: " + equipmentId);
            }
        }
    }

    private void validateScheduleConflicts(FitnessSession candidate) throws InvalidBookingException {
        for (FitnessSession existing : sessionRepository.findAll()) {
            if (!existing.isActive() || !candidate.overlaps(existing)) continue;
            if (candidate.getInstructorId().equals(existing.getInstructorId())) {
                throw new InvalidBookingException("Instructor is already scheduled at this time");
            }
            if (candidate.usesStudio(existing.getStudio())) {
                throw new InvalidBookingException("Studio is already scheduled at this time");
            }
            if (candidate.usesAnyEquipment(existing.getEquipmentIds())) {
                throw new InvalidBookingException("Equipment is already scheduled at this time");
            }
        }
    }

    private User requireActiveUser(String userId, User.Role role, String label)
            throws InvalidBookingException {
        String validatedId;
        try {
            validatedId = requireText(userId, label + " ID");
        } catch (IllegalArgumentException exception) {
            throw new InvalidBookingException(exception.getMessage());
        }
        User user = userRepository.findById(validatedId)
                .orElseThrow(() -> new InvalidBookingException("Unknown " + label.toLowerCase() + " ID: " + validatedId));
        if (!user.isActive()) {
            throw new InvalidBookingException(label + " account is inactive");
        }
        if (user.getRole() != role) {
            throw new InvalidBookingException(label + " role is required");
        }
        return user;
    }

    private boolean hasAvailableEquipment(FitnessSession session) {
        return session.getEquipmentIds().stream()
                .map(equipmentRepository::findById)
                .allMatch(optional -> optional.isPresent() && optional.orElseThrow().isAvailableForScheduling());
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        return value.trim();
    }
}
