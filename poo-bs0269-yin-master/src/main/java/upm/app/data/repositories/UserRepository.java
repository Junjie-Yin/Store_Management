package upm.app.data.repositories;

import upm.app.data.models.user.Role;
import upm.app.data.models.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends GenericRepository<User> {
    Optional<User> findByEmail(String email);

    Optional<User> findByDni(String dni);

    List<User> findByRole(Role role);
}
