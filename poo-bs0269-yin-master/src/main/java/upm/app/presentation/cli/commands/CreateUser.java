package upm.app.presentation.cli.commands;

import upm.app.data.models.user.Role;
import upm.app.data.models.user.User;
import upm.app.presentation.cli.Command;
import upm.app.presentation.view.View;
import upm.app.services.UserService;

import java.util.List;

public class CreateUser implements Command {
    private final View view;
    private final UserService userService;

    public CreateUser(View view, UserService userService) {
        this.view = view;
        this.userService = userService;
    }

    @Override
    public String name() {
        return "create-user";
    }

    @Override
    public List<String> params() {
        return List.of("email", "name", "firstSurname", "dni", "role");
    }

    @Override
    public String helpMessage() {
        return "Se crea un usuario";
    }

    @Override
    public void execute(String[] params) {
        String email = params[0].trim();
        String name = params[1].trim();
        String firstSurname = params[2].trim();
        String dni = params[3].trim();
        Role role = Role.valueOf(params[4].trim().toUpperCase());

        User newUser = User.builder(email, name, firstSurname, dni, role)
                .build();
        User createdUser = this.userService.create(newUser);
        this.view.show(createdUser.toString());
    }
}
