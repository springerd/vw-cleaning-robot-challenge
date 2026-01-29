package com.vw.cleaningrobot.infrastructure.input.parser;

import com.vw.cleaningrobot.domain.model.Coordinate;
import com.vw.cleaningrobot.domain.model.Orientation;
import com.vw.cleaningrobot.domain.model.Position;

public class PositionParser {

    private PositionParser() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Position parse(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Position line cannot be null or empty");
        }

        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid position format. Expected: X Y O");
        }

        try {
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            Orientation orientation = Orientation.valueOf(parts[2].toUpperCase());

            return new Position(new Coordinate(x, y), orientation);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid coordinates in: " + line, e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid orientation in: " + line, e);
        }
    }
}
