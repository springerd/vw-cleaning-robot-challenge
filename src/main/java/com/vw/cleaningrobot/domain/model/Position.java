package com.vw.cleaningrobot.domain.model;

public record Position(Coordinate coordinate, Orientation orientation) {
    public Position {
        if (coordinate == null || orientation == null) {
            throw new IllegalArgumentException("Coordinate and orientation cannot be null");
        }
    }

    public Position turnLeft() {
        return new Position(coordinate, orientation.turnLeft());
    }

    public Position turnRight() {
        return new Position(coordinate, orientation.turnRight());
    }

    public Position moveForward() {
        Coordinate next = new Coordinate(
            coordinate.x() + orientation.dx(),
            coordinate.y() + orientation.dy()
        );
        return new Position(next, orientation);
    }
}
