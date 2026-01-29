package com.vw.cleaningrobot.infrastructure.input.parser;

import com.vw.cleaningrobot.domain.model.Command;

public class CommandParser {

    private CommandParser() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Command fromChar(char c) {
        return switch (c) {
            case 'L', 'l' -> Command.L;
            case 'R', 'r' -> Command.R;
            case 'M', 'm' -> Command.M;
            default -> throw new IllegalArgumentException("Invalid command: " + c);
        };
    }
}
