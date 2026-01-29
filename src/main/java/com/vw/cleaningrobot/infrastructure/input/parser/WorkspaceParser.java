package com.vw.cleaningrobot.infrastructure.input.parser;

import com.vw.cleaningrobot.domain.model.Coordinate;
import com.vw.cleaningrobot.domain.model.Workspace;

public class WorkspaceParser {

    private WorkspaceParser() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Workspace parse(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Workspace line cannot be null or empty");
        }

        String[] parts = line.trim().split("\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid workspace format. Expected: X Y");
        }

        try {
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            return new Workspace(new Coordinate(x, y));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid coordinates in: " + line, e);
        }
    }
}
