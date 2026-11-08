package upm.app.services;

import upm.app.data.models.user.User;
import upm.app.data.repositories.UserRepository;
import upm.app.services.exceptions.DuplicateException;
import upm.app.services.exceptions.NotFoundException;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        this.userRepository.findByDni(user.getDni())
                .ifPresent(u -> {
                    throw new DuplicateException("El DNI ya existe, y debiera ser único: " + user.getDni());
                });
        return userRepository.create(user);
    }

    public User read(Integer id) {
        return userRepository.read(id)
                .orElseThrow(() -> new NotFoundException("No existe un usuario con id: " + id));
    }

    public void delete(Integer id) {
        this.userRepository.read(id)
                .orElseThrow(() -> new NotFoundException("No existe un usuario con id: " + id));
        userRepository.delete(id);
    }
}
