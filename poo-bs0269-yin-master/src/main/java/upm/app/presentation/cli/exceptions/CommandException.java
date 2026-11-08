package upm.app.presentation.cli.exceptions;

public class CommandException extends RuntimeException {
    private static final String DESCRIPTION = "Comando incorrecta";

    public CommandException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }
}
