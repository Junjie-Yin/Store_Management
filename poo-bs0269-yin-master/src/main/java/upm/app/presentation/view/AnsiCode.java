package upm.app.presentation.view;

public enum AnsiCode {
    RESET("\u001B[0m"),

    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m"),

    BACKGROUND_BLACK("\u001B[40m"),
    BACKGROUND_RED("\u001B[41m"),
    BACKGROUND_GREEN("\u001B[42m"),
    BACKGROUND_YELLOW("\u001B[43m"),
    BACKGROUND_BLUE("\u001B[44m"),
    BACKGROUND_PURPLE("\u001B[45m"),
    BACKGROUND_CYAN("\u001B[46m"),
    BACKGROUND_WHITE("\u001B[47m"),

    BOLD("\u001B[1m"),
    DIM("\u001B[2m"),
    ITALIC("\u001B[3m"),
    UNDERLINE("\u001B[4m"),
    BLINK("\u001B[5m"),
    REVERSE("\u001B[7m"),
    HIDDEN("\u001B[8m"),
    STRIKETHROUGH("\u001B[9m"),

    RETURN("\r"),
    INDENT("\t"),

    WARNING("\u26A0"),    // ⚠
    FIRE("\uD83D\uDD25"), // 🔥

    COPY_RIGHT("\u00A9");

    private final String code;

    AnsiCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }

    public String apply(String text) {
        return code + text + RESET.code;
    }

    public String apply(String text, AnsiCode... others) {
        StringBuilder formattedText = new StringBuilder();
        formattedText.append(code);
        for (AnsiCode ansiCode : others) {
            formattedText.append(ansiCode.code);
        }
        formattedText.append(text).append(RESET.code);
        return formattedText.toString();
    }
}

