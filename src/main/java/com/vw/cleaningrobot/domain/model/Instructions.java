package com.vw.cleaningrobot.domain.model;

import java.util.List;

public record Instructions(List<Command> commands) {
    public Instructions {
        if (commands == null || commands.isEmpty()) {
            throw new IllegalArgumentException("Instructions cannot be null or empty");
        }
        commands = List.copyOf(commands); // This will throw NPE if list contains null
    }
}
