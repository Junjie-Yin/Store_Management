package upm.app.presentation.cli.commands;

import upm.app.data.models.user.User;
import upm.app.presentation.cli.Command;
import upm.app.presentation.view.View;
import upm.app.services.UserService;

import java.util.List;

public class ReadUser implements Command {
    private final View view;
    private final UserService userService;

    public ReadUser(View view, UserService userService) {
        this.view = view;
        this.userService = userService;
    }

    @Override
    public String name() {
        return "read-user";
    }

    @Override
    public List<String> params() {
        return List.of("id");
    }

    @Override
    public String helpMessage() {
        return "Lee un usuario por su ID";
    }

    @Override
    public void execute(String[] params) {
        Integer id = Integer.parseInt(params[0].trim());
        User foundUser = userService.read(id);
        view.show("Usuario encontrado:");
        view.showItem(foundUser.toString());
    }
}