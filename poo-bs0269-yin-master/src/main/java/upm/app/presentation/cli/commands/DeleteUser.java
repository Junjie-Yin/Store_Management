package upm.app.presentation.cli.commands;

import upm.app.presentation.cli.Command;
import upm.app.presentation.view.View;
import upm.app.services.UserService;

import java.util.List;

public class DeleteUser implements Command {
    private final View view;
    private final UserService userService;

    public DeleteUser(View view, UserService userService) {
        this.view = view;
        this.userService = userService;
    }

    @Override
    public String name() {
        return "delete-user";
    }

    @Override
    public List<String> params() {
        return List.of("id");
    }

    @Override
    public String helpMessage() {
        return "Elimina un usuario por su ID";
    }

    @Override
    public void execute(String[] params) {
        Integer id = Integer.parseInt(params[0].trim());
        userService.delete(id);
        view.show("Usuario con ID " + id + " eliminado correctamente.");
    }
}