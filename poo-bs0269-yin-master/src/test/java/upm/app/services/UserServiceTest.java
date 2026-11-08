package upm.app.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upm.app.DependencyInjector;
import upm.app.data.models.user.Role;
import upm.app.data.models.user.User;
import upm.app.services.exceptions.DuplicateException;
import upm.app.services.exceptions.NotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private final UserService userService = DependencyInjector.getInstance().getUserService();
    private final DependencyInjector injector = DependencyInjector.getInstance();

    @BeforeEach
    void setUp() {
        injector.getSeeder().seed();
    }

    @Test
    void testCreateUser() {
        int initialSize = injector.getUserRepository().size();
        User newUser = User.builder("test@example.com", "Test", "User", "11111111T", Role.ALUMNO)
                .build();
        User createdUser = userService.create(newUser);

        assertNotNull(createdUser.getId());
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals(initialSize + 1, injector.getUserRepository().size());
    }

    @Test
    void testCreateUserDuplicateDni() {
        User existingUser = injector.getUserRepository().findByDni("12345678A").orElseThrow(); // Ana's DNI
        User newUserWithDuplicateDni = User.builder("another@example.com", "Another", "One", existingUser.getDni(), Role.ALUMNO)
                .build();
        assertThrows(DuplicateException.class, () -> userService.create(newUserWithDuplicateDni));
    }

    @Test
    void testReadUser() {
        User ana = injector.getUserRepository().findByEmail("ana@example.com").orElseThrow();
        User foundUser = userService.read(ana.getId());
        assertEquals(ana.getName(), foundUser.getName());
    }

    @Test
    void testReadUserNotFound() {
        assertThrows(NotFoundException.class, () -> userService.read(999));
    }

    @Test
    void testDeleteUser() {
        int initialSize = injector.getUserRepository().size();
        User ana = injector.getUserRepository().findByEmail("ana@example.com").orElseThrow();
        userService.delete(ana.getId());

        assertEquals(initialSize - 1, injector.getUserRepository().size());
        assertThrows(NotFoundException.class, () -> userService.read(ana.getId()));
    }

    @Test
    void testDeleteUserNotFound() {
        assertThrows(NotFoundException.class, () -> userService.delete(999));
    }
}
