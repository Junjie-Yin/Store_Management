package upm.app.presentation.cli;

import upm.app.presentation.cli.commands.Exit;
import upm.app.presentation.cli.commands.Help;
import upm.app.presentation.cli.exceptions.BadRequestException;
import upm.app.presentation.cli.exceptions.CommandException;
import upm.app.presentation.view.View;

import java.util.*;

public class CommandLineInterface {
    public static final String EXIT = "exit";
    public static final String HELP = "help";
    private static final String COMMAND_DELIMITER_EXPRESSION = "[" + Command.COMMAND_PARAM_SEPARATOR + "\\r\\n]";
    public final View view;
    private final Map<String, Command> commands;

    public CommandLineInterface(View view) {
        this.view = view;
        this.commands = new HashMap<>();
        this.add(new Help(this));
        this.add(new Exit());
    }

    public void add(Command command) {
        this.commands.put(command.name(), command);
    }

    public Map<String, Command> getCommands() {
        return Collections.unmodifiableMap(commands);
    }

    public boolean runCommands() {
        Scanner scanner = new Scanner(System.in).useDelimiter(COMMAND_DELIMITER_EXPRESSION);
        boolean exit;
        do {
            exit = runCommand(scanner);
        } while (!exit);
        return true;
    }

    public boolean runCommand(Scanner scanner) {
        this.view.showCommandPrompt();
        String commandName = scanner.next().trim();

        if (EXIT.equals(commandName)) {
            return true;
        }

        Command command = this.commands.get(commandName);
        if (command == null) {
            throw new CommandException("Comando '" + commandName + "' no existe.");
        }

        String[] params = this.scanParamsIfNeededAssured(scanner, commandName);
        command.execute(params);

        return false;
    }

    private String[] scanParamsIfNeededAssured(Scanner scanner, String commandName) {
        Command command = commands.get(commandName);
        List<String> expectedParams = command.params();
        if (expectedParams.isEmpty()) {
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            return new String[0];
        }

        if (!scanner.hasNext()) {
            throw new BadRequestException("Se esperaban parámetros para el comando '" + commandName + "' pero no se encontraron.");
        }

        String paramsLine = scanner.next().trim();
        String[] foundParams = paramsLine.isEmpty() ? new String[0] : paramsLine.split(Command.PARAM_SEPARATOR);


        if (expectedParams.size() != foundParams.length) {
            throw new BadRequestException("Número de parámetros incorrecto para '" + commandName + "'.\n" +
                    "Esperados: " + expectedParams.stream().map(p -> "<" + p + ">").toList() +
                    ", encontrados: " + Arrays.toString(foundParams) + "\nUso: " + command.help());
        }
        return foundParams;
    }
}
