package upm.app.data.repositories.map;

import upm.app.data.models.user.Role;
import upm.app.data.models.user.User;
import upm.app.data.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserRepositoryMap extends GenericRepositoryMap<User> implements UserRepository {

    public UserRepositoryMap() {
        super();
    }

    public Optional<User> findByEmail(String email) {
        return map.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public Optional<User> findByDni(String dni) {
        return map.values().stream()
                .filter(u -> u.getDni().equalsIgnoreCase(dni))
                .findFirst();
    }

    public List<User> findByRole(Role role) {
        return map.values().stream()
                .filter(user -> role.equals(user.getRole()))
                .toList();
    }
}
