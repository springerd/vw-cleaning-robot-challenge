package com.vw.cleaningrobot.domain.model;

public class Robot {
    private Position position;
    private final Workspace workspace;

    public Robot(Position initialPosition, Workspace workspace) {
        if (initialPosition == null || workspace == null) {
            throw new IllegalArgumentException("Position and workspace cannot be null");
        }
        if (!workspace.isWithinBounds(initialPosition.coordinate())) {
            throw new IllegalArgumentException("Initial position is outside workspace bounds");
        }
        this.position = initialPosition;
        this.workspace = workspace;
    }

    public void execute(Command command) {
        position = switch (command) {
            case L -> position.turnLeft();
            case R -> position.turnRight();
            case M -> move();
        };
    }

    public void execute(Instructions instructions) {
        instructions.commands().forEach(this::execute);
    }

    private Position move() {
        Position next = position.moveForward();
        return workspace.isWithinBounds(next.coordinate()) ? next : position;
    }

    public Position getPosition() {
        return position;
    }
}
