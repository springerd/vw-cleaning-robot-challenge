package com.vw.cleaningrobot.domain.model;

public record Workspace(Coordinate upperRight) {
    public Workspace {
        if (upperRight == null) {
            throw new IllegalArgumentException("Upper right coordinate cannot be null");
        }
        if (upperRight.x() < 0 || upperRight.y() < 0) {
            throw new IllegalArgumentException("Workspace bounds cannot be negative");
        }
        if (upperRight.x() == 0 && upperRight.y() == 0) {
            throw new IllegalArgumentException("Workspace cannot be 0x0");
        }
    }

    public boolean isWithinBounds(Coordinate coordinate) {
        return coordinate.x() >= 0 && coordinate.x() <= upperRight.x() &&
               coordinate.y() >= 0 && coordinate.y() <= upperRight.y();
    }
}
