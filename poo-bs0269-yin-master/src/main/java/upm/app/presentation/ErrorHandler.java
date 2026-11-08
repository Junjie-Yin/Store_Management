package upm.app.presentation;

import upm.app.presentation.cli.CommandLineInterface;
import upm.app.presentation.view.View;

public class ErrorHandler {

    public void handlesErrors(CommandLineInterface commandLineInterface, View view) {
        view.showTitle("App Tienda UPM");
        boolean exit = false;
        while (!exit) {
            try {
                exit = commandLineInterface.runCommands();
            } catch (Exception e) {
                view.showError("ERROR (" + e.getClass().getSimpleName() + ") >>> " + e.getMessage());
            }
        }
        view.showTitle("Hasta pronto!");
    }
}
