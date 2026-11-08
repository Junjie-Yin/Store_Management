package upm.app.data.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upm.app.DependencyInjector;
import upm.app.data.models.user.Role;
import upm.app.data.models.user.User;
import upm.app.data.repositories.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private final UserRepository userRepository = DependencyInjector.getInstance().getUserRepository();

    @BeforeEach
    void setUp() {
        DependencyInjector.getInstance().getSeeder().seed();
    }

    @Test
    void testCreateUser() {
        int initialSize = userRepository.size();
        User newUser = User.builder("new@example.com", "New", "User", "11111111X", Role.ALUMNO)
                .build();
        User createdUser = userRepository.create(newUser);

        assertNotNull(createdUser.getId());
        assertEquals(initialSize + 1, userRepository.size());
        assertEquals(newUser.getEmail(), createdUser.getEmail());
        assertTrue(userRepository.read(createdUser.getId()).isPresent());
    }

    @Test
    void testUpdateUser() {
        User existingUser = userRepository.findByEmail("ana@example.com").orElseThrow();
        Integer userId = existingUser.getId();
        existingUser.setName("Ana Updated");
        existingUser.setRole(Role.ALUMNO);

        User updatedUser = userRepository.update(userId, existingUser);

        assertEquals("Ana Updated", updatedUser.getName());
        assertEquals(Role.ALUMNO, updatedUser.getRole());
        assertEquals("Ana Updated", userRepository.read(userId).orElseThrow().getName());
    }

    @Test
    void testUpdateUserNotFound() {
        User nonExistentUser = User.builder("nonexistent@example.com", "Non", "Existent", "00000000Y", Role.ALUMNO)
                .id(999)
                .build();
        assertThrows(EntityNotFoundException.class, () -> userRepository.update(999, nonExistentUser));
    }

    @Test
    void testReadUser() {
        User ana = userRepository.findByEmail("ana@example.com").orElseThrow();
        Integer anaId = ana.getId();

        Optional<User> user = userRepository.read(anaId);
        assertTrue(user.isPresent());
        assertEquals("Ana", user.get().getName());
    }

    @Test
    void testReadUserNotFound() {
        Optional<User> user = userRepository.read(999);
        assertFalse(user.isPresent());
    }

    @Test
    void testDeleteUser() {
        int initialSize = userRepository.size();
        User ana = userRepository.findByEmail("ana@example.com").orElseThrow();
        userRepository.delete(ana.getId());
        assertEquals(initialSize - 1, userRepository.size());
        assertFalse(userRepository.read(ana.getId()).isPresent());
    }

    @Test
    void testFindAllAllUsers() {
        List<User> users = userRepository.findAll();
        assertFalse(users.isEmpty());
        assertEquals(4, users.size());
    }

    @Test
    void testFindByEmail() {
        Optional<User> user = userRepository.findByEmail("juan@example.com");
        assertTrue(user.isPresent());
        assertEquals("Juan", user.get().getName());
    }

    @Test
    void testFindByEmailNotFound() {
        Optional<User> user = userRepository.findByEmail("nonexistent@example.com");
        assertFalse(user.isPresent());
    }

    @Test
    void testFindByDni() {
        Optional<User> user = userRepository.findByDni("12345678A"); // Ana's DNI
        assertTrue(user.isPresent());
        assertEquals("Ana", user.get().getName());
    }

    @Test
    void testFindByDniNotFound() {
        Optional<User> user = userRepository.findByDni("00000000Z");
        assertFalse(user.isPresent());
    }

    @Test
    void testFindByRole() {
        List<User> professors = userRepository.findByRole(Role.PROFESOR);
        assertEquals(2, professors.size()); // Ana and Carlos
        assertTrue(professors.stream().anyMatch(u -> u.getName().equals("Ana")));
        assertTrue(professors.stream().anyMatch(u -> u.getName().equals("Carlos")));

        List<User> students = userRepository.findByRole(Role.ALUMNO);
        assertEquals(2, students.size()); // Juan and Maria
        assertTrue(students.stream().anyMatch(u -> u.getName().equals("Juan")));
        assertTrue(students.stream().anyMatch(u -> u.getName().equals("María")));
    }
}
