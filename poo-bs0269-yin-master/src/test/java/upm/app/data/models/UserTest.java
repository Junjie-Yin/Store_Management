package upm.app.data.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upm.app.data.models.exceptions.InvalidAttributeException;
import upm.app.data.models.user.Role;
import upm.app.data.models.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder("test@example.com", "Test", "User", "12345678Z", Role.ALUMNO)
                .id(1)
                .build();
    }

    @Test
    void testUserConstructorWithSecondSurname() {
        User userWithSecondSurname = User.builder("test2@example.com", "Test", "User", "87654321A", Role.PROFESOR)
                .id(2)
                .secondSurname("Surname")
                .build();
        assertEquals("Surname", userWithSecondSurname.getSecondSurname());
    }

    @Test
    void testSetEmailValid() {
        user.setEmail("new@example.com");
        assertEquals("new@example.com", user.getEmail());
    }

    @Test
    void testSetEmailInvalidNull() {
        assertThrows(InvalidAttributeException.class, () -> user.setEmail(null));
    }

    @Test
    void testSetEmailInvalidFormat() {
        assertThrows(InvalidAttributeException.class, () -> user.setEmail("invalid-email"));
    }

    @Test
    void testSetNameValid() {
        user.setName("NewName");
        assertEquals("NewName", user.getName());
    }

    @Test
    void testSetNameInvalidNull() {
        assertThrows(InvalidAttributeException.class, () -> user.setName(null));
    }

    @Test
    void testSetNameInvalidEmpty() {
        assertThrows(InvalidAttributeException.class, () -> user.setName(""));
    }

    @Test
    void testSetFirstSurnameValid() {
        user.setFirstSurname("NewSurname");
        assertEquals("NewSurname", user.getFirstSurname());
    }

    @Test
    void testSetFirstSurnameInvalidNull() {
        assertThrows(InvalidAttributeException.class, () -> user.setFirstSurname(null));
    }

    @Test
    void testSetFirstSurnameInvalidEmpty() {
        assertThrows(InvalidAttributeException.class, () -> user.setFirstSurname(""));
    }

    @Test
    void testSetSecondSurnameValid() {
        user.setSecondSurname("NewSecondSurname");
        assertEquals("NewSecondSurname", user.getSecondSurname());
    }

    @Test
    void testSetSecondSurnameInvalidEmpty() {
        assertThrows(InvalidAttributeException.class, () -> user.setSecondSurname(""));
    }

    @Test
    void testSetDniValid() {
        user.setDni("98765432B");
        assertEquals("98765432B", user.getDni());
    }

    @Test
    void testSetDniInvalidNull() {
        assertThrows(InvalidAttributeException.class, () -> user.setDni(null));

    }

    @Test
    void testSetDniInvalidEmpty() {
        assertThrows(InvalidAttributeException.class, () -> user.setDni(""));
    }

    @Test
    void testSetRoleValid() {
        user.setRole(Role.PROFESOR);
        assertEquals(Role.PROFESOR, user.getRole());
    }

    @Test
    void testSetRoleInvalidNull() {
        assertThrows(InvalidAttributeException.class, () -> user.setRole(null));
    }
}