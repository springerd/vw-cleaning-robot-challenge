package com.vw.cleaningrobot.infrastructure.output.formatter;

import com.vw.cleaningrobot.domain.model.Position;

public class PositionFormatter {

    private PositionFormatter() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String format(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        return position.coordinate().x() + " " + 
               position.coordinate().y() + " " + 
               position.orientation();
    }
}
