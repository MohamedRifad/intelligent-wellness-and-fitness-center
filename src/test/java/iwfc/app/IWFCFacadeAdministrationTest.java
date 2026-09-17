package iwfc.app;

import iwfc.domain.Administrator;
import iwfc.domain.Equipment;
import iwfc.domain.User;
import iwfc.exception.DuplicateDataException;
import iwfc.exception.UnauthorizedAccessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IWFCFacadeAdministrationTest {
    @Test
    void initializesFirstAdministratorThroughFacade() throws DuplicateDataException, UnauthorizedAccessException {
        IWFCFacade facade = new IWFCFacade();

        User administrator = facade.initializeAdministrator("A1", "Asha");

        assertInstanceOf(Administrator.class, administrator);
        assertEquals(User.Role.ADMINISTRATOR, administrator.getRole());
        assertEquals(administrator, facade.viewUsers(administrator).getFirst());
    }

    @Test
    void preventsSecondInitialization() throws DuplicateDataException {
        IWFCFacade facade = new IWFCFacade();
        facade.initializeAdministrator("A1", "Asha");

        assertThrows(DuplicateDataException.class,
                () -> facade.initializeAdministrator("A2", "Second Administrator"));
    }

    @ParameterizedTest
    @EnumSource(User.Role.class)
    void administratorRegistersEveryRoleUsingFactory(User.Role role)
            throws DuplicateDataException, UnauthorizedAccessException {
        IWFCFacade facade = new IWFCFacade();
        User administrator = facade.initializeAdministrator("A1", "Asha");

        User created = facade.registerUser(administrator, role, "U-" + role.name(), "Created User");

        assertEquals(role, created.getRole());
        assertTrue(facade.viewUsers(administrator).contains(created));
    }

    @Test
    void rejectsDuplicateUserId() throws DuplicateDataException, UnauthorizedAccessException {
        IWFCFacade facade = new IWFCFacade();
        User administrator = facade.initializeAdministrator("A1", "Asha");
        facade.registerUser(administrator, User.Role.MEMBER, "M1", "Maya");

        assertThrows(DuplicateDataException.class,
                () -> facade.registerUser(administrator, User.Role.MEMBER, "M1", "Mina"));
    }

    @Test
    void rejectsBlankUserInput() throws DuplicateDataException {
        IWFCFacade facade = new IWFCFacade();
        User administrator = facade.initializeAdministrator("A1", "Asha");

        assertThrows(IllegalArgumentException.class,
                () -> facade.registerUser(administrator, User.Role.MEMBER, " ", "Maya"));
        assertThrows(IllegalArgumentException.class,
                () -> facade.registerUser(administrator, User.Role.MEMBER, "M1", " "));
        assertThrows(NullPointerException.class,
                () -> facade.registerUser(administrator, null, "M1", "Maya"));
    }

    @Test
    void administratorListsAndDeactivatesUser() throws DuplicateDataException, UnauthorizedAccessException {
        IWFCFacade facade = new IWFCFacade();
        User administrator = facade.initializeAdministrator("A1", "Asha");
        User member = facade.registerUser(administrator, User.Role.MEMBER, "M1", "Maya");

        facade.deactivateUser(administrator, member.getId());

        assertFalse(member.isActive());
        assertEquals(2, facade.viewUsers(administrator).size());
        assertThrows(UnsupportedOperationException.class,
                () -> facade.viewUsers(administrator).add(member));
    }

    @Test
    void rejectsUnknownUserId() throws DuplicateDataException {
        IWFCFacade facade = new IWFCFacade();
        User administrator = facade.initializeAdministrator("A1", "Asha");

        assertThrows(IllegalArgumentException.class,
                () -> facade.deactivateUser(administrator, "UNKNOWN"));
        assertThrows(IllegalArgumentException.class,
                () -> facade.deactivateUser(administrator, " "));
    }

    @ParameterizedTest
    @EnumSource(value = User.Role.class, names = "ADMINISTRATOR", mode = EnumSource.Mode.EXCLUDE)
    void rejectsNonAdministratorUserManagement(User.Role role)
            throws DuplicateDataException, UnauthorizedAccessException {
        IWFCFacade facade = new IWFCFacade();
        User administrator = facade.initializeAdministrator("A1", "Asha");
        User nonAdministrator = facade.registerUser(administrator, role, "U1", "Other User");

        assertThrows(UnauthorizedAccessException.class,
                () -> facade.registerUser(nonAdministrator, User.Role.MEMBER, "M2", "Mina"));
        assertThrows(UnauthorizedAccessException.class,
                () -> facade.viewUsers(nonAdministrator));
        assertThrows(UnauthorizedAccessException.class,
                () -> facade.deactivateUser(nonAdministrator, administrator.getId()));
    }

    @Test
    void rejectsNullUnregisteredAndInactiveAdministrators()
            throws DuplicateDataException, UnauthorizedAccessException {
        IWFCFacade facade = new IWFCFacade();
        User registeredAdministrator = facade.initializeAdministrator("A1", "Asha");
        User fabricatedAdministrator = new Administrator("A1", "Impostor");

        assertThrows(UnauthorizedAccessException.class, () -> facade.viewUsers(null));
        assertThrows(UnauthorizedAccessException.class,
                () -> facade.viewUsers(fabricatedAdministrator));

        facade.deactivateUser(registeredAdministrator, registeredAdministrator.getId());
        assertThrows(UnauthorizedAccessException.class,
                () -> facade.viewUsers(registeredAdministrator));
    }

    @Test
    void administratorAddsUpdatesListsAndDeactivatesEquipment()
            throws DuplicateDataException, UnauthorizedAccessException {
        IWFCFacade facade = new IWFCFacade();
        User administrator = facade.initializeAdministrator("A1", "Asha");

        Equipment equipment = facade.addEquipment(administrator, "EQ1", "Spin Bike", "Studio A");
        facade.updateEquipment(administrator, "EQ1", "Premium Spin Bike", "Studio B");

        assertEquals("EQ1", equipment.getId());
        assertEquals("Premium Spin Bike", equipment.getName());
        assertEquals("Studio B", equipment.getLocation());
        assertEquals(equipment, facade.viewEquipment(administrator).getFirst());

        facade.deactivateEquipment(administrator, "EQ1");
        assertFalse(equipment.isActive());
    }

    @Test
    void rejectsDuplicateEquipmentId() throws DuplicateDataException, UnauthorizedAccessException {
        IWFCFacade facade = new IWFCFacade();
        User administrator = facade.initializeAdministrator("A1", "Asha");
        facade.addEquipment(administrator, "EQ1", "Spin Bike", "Studio A");

        assertThrows(DuplicateDataException.class,
                () -> facade.addEquipment(administrator, "EQ1", "Rower", "Cardio Zone"));
    }

    @Test
    void rejectsBlankEquipmentInput() throws DuplicateDataException {
        IWFCFacade facade = new IWFCFacade();
        User administrator = facade.initializeAdministrator("A1", "Asha");

        assertThrows(IllegalArgumentException.class,
                () -> facade.addEquipment(administrator, " ", "Bike", "Studio A"));
        assertThrows(IllegalArgumentException.class,
                () -> facade.addEquipment(administrator, "EQ1", " ", "Studio A"));
        assertThrows(IllegalArgumentException.class,
                () -> facade.addEquipment(administrator, "EQ1", "Bike", " "));
    }

    @Test
    void rejectsUnknownEquipmentId() throws DuplicateDataException {
        IWFCFacade facade = new IWFCFacade();
        User administrator = facade.initializeAdministrator("A1", "Asha");

        assertThrows(IllegalArgumentException.class,
                () -> facade.updateEquipment(administrator, "UNKNOWN", "Bike", "Studio A"));
        assertThrows(IllegalArgumentException.class,
                () -> facade.deactivateEquipment(administrator, "UNKNOWN"));
        assertThrows(IllegalArgumentException.class,
                () -> facade.updateEquipment(administrator, " ", "Bike", "Studio A"));
    }

    @ParameterizedTest
    @EnumSource(value = User.Role.class, names = "ADMINISTRATOR", mode = EnumSource.Mode.EXCLUDE)
    void rejectsNonAdministratorEquipmentOperations(User.Role role)
            throws DuplicateDataException, UnauthorizedAccessException {
        IWFCFacade facade = new IWFCFacade();
        User administrator = facade.initializeAdministrator("A1", "Asha");
        User nonAdministrator = facade.registerUser(administrator, role, "U1", "Other User");
        facade.addEquipment(administrator, "EQ1", "Bike", "Studio A");

        assertThrows(UnauthorizedAccessException.class,
                () -> facade.addEquipment(nonAdministrator, "EQ2", "Rower", "Cardio Zone"));
        assertThrows(UnauthorizedAccessException.class,
                () -> facade.updateEquipment(nonAdministrator, "EQ1", "Updated", "Studio B"));
        assertThrows(UnauthorizedAccessException.class,
                () -> facade.deactivateEquipment(nonAdministrator, "EQ1"));
        assertThrows(UnauthorizedAccessException.class,
                () -> facade.viewEquipment(nonAdministrator));
    }

    @Test
    void equipmentListIsReadOnly() throws DuplicateDataException, UnauthorizedAccessException {
        IWFCFacade facade = new IWFCFacade();
        User administrator = facade.initializeAdministrator("A1", "Asha");
        Equipment equipment = facade.addEquipment(administrator, "EQ1", "Bike", "Studio A");

        assertThrows(UnsupportedOperationException.class,
                () -> facade.viewEquipment(administrator).add(equipment));
    }
}
