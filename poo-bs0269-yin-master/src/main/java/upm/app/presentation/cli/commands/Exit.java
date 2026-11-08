package upm.app.presentation.cli.commands;

import upm.app.presentation.cli.Command;
import upm.app.presentation.cli.CommandLineInterface;

import java.util.List;

public class Exit implements Command {
    @Override
    public String name() {
        return CommandLineInterface.EXIT;
    }

    @Override
    public List<String> params() {
        return List.of();
    }

    @Override
    public String helpMessage() {
        return "Termina la ejecución de la aplicación";
    }

    @Override
    public void execute(String[] params) {
        System.out.println("Saliendo de la aplicación. ¡Hasta pronto!");
    }
}