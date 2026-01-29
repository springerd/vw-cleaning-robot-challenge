package com.vw.cleaningrobot.infrastructure.input.parser;

import com.vw.cleaningrobot.domain.model.Command;
import com.vw.cleaningrobot.domain.model.Instructions;
import java.util.List;

public class InstructionsParser {

    private InstructionsParser() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Instructions parse(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Instruction string cannot be null or empty");
        }

        List<Command> commands = input.chars()
                .filter(c -> !Character.isWhitespace(c))
                .mapToObj(c -> CommandParser.fromChar((char) c))
                .toList();

        return new Instructions(commands);
    }
}
