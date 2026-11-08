package upm.app.presentation.cli.commands;

import upm.app.presentation.cli.Command;
import upm.app.presentation.cli.CommandLineInterface;
import upm.app.presentation.view.View;

import java.util.List;

public class Help implements Command {
    private final CommandLineInterface cli;
    private final View view;

    public Help(CommandLineInterface cli) {
        this.cli = cli;
        this.view = cli.view;
    }

    @Override
    public String name() {
        return CommandLineInterface.HELP;
    }

    @Override
    public List<String> params() {
        return List.of();
    }

    @Override
    public String helpMessage() {
        return "Muestra la ayuda de los comandos disponibles";
    }

    @Override
    public void execute(String[] params) {
        this.view.showImportant("Comandos disponibles:");
        this.cli.getCommands().values().forEach(command -> this.view.showImportant("\t" + command.help()));
    }
}