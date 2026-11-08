package upm.app.presentation.cli;

import java.util.List;

public interface Command {
    String COMMAND_PARAM_SEPARATOR = "#";
    String PARAM_SEPARATOR = ",";

    String name();

    List<String> params();

    String helpMessage();

    void execute(String[] params);

    default String help() {
        StringBuilder result = new StringBuilder(this.name());
        if (!this.params().isEmpty()) {
            result.append(COMMAND_PARAM_SEPARATOR).append(String.join(PARAM_SEPARATOR, this.params()));
        }
        return result.append(". ").append(this.helpMessage()).append(".").toString();
    }
}
