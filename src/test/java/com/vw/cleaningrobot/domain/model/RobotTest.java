package com.vw.cleaningrobot.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RobotTest {

    private final Workspace workspace = new Workspace(new Coordinate(5, 5));

    @ParameterizedTest
    @MethodSource("invalidRobotArguments")
    void shouldRejectInvalidRobot(Position position, Workspace workspace) {
        assertThrows(IllegalArgumentException.class, 
            () -> new Robot(position, workspace));
    }

    static Stream<Arguments> invalidRobotArguments() {
        Workspace ws = new Workspace(new Coordinate(5, 5));
        Position validPos = new Position(new Coordinate(1, 2), Orientation.N);
        Position outsidePos = new Position(new Coordinate(10, 10), Orientation.N);
        
        return Stream.of(
            Arguments.of(null, ws),
            Arguments.of(validPos, null),
            Arguments.of(outsidePos, ws)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "0, 0, S",  // Bottom edge
        "5, 5, N",  // Top edge
        "5, 5, E",  // Right edge
        "0, 0, W"   // Left edge
    })
    void shouldIgnoreMovementOutsideWorkspace(int x, int y, Orientation orientation) {
        Position position = new Position(new Coordinate(x, y), orientation);
        Robot robot = new Robot(position, workspace);
        
        robot.execute(Command.M);
        
        assertEquals(new Coordinate(x, y), robot.getPosition().coordinate());
    }

    @Test
    void shouldExecuteExampleFromChallenge1() {
        Position position = new Position(new Coordinate(1, 2), Orientation.N);
        Robot robot = new Robot(position, workspace);
        Instructions instructions = new Instructions(List.of(Command.L, Command.M, Command.L, Command.M, Command.L, Command.M, Command.L, Command.M, Command.M));
        
        robot.execute(instructions);
        
        assertEquals(new Coordinate(1, 3), robot.getPosition().coordinate());
        assertEquals(Orientation.N, robot.getPosition().orientation());
    }

    @Test
    void shouldExecuteExampleFromChallenge2() {
        Position position = new Position(new Coordinate(3, 3), Orientation.E);
        Robot robot = new Robot(position, workspace);
        Instructions instructions = new Instructions(List.of(Command.M, Command.M, Command.R, Command.M, Command.M, Command.R, Command.M, Command.R, Command.R, Command.M));
        
        robot.execute(instructions);
        
        assertEquals(new Coordinate(5, 1), robot.getPosition().coordinate());
        assertEquals(Orientation.E, robot.getPosition().orientation());
    }

    @Test
    void shouldHandleMultipleTurnsWithoutMoving() {
        Robot robot = new Robot(new Position(new Coordinate(2, 2), Orientation.N), workspace);
        robot.execute(new Instructions(List.of(Command.L, Command.L, Command.L, Command.L)));
        
        assertEquals(new Coordinate(2, 2), robot.getPosition().coordinate());
        assertEquals(Orientation.N, robot.getPosition().orientation());
    }

    @Test
    void shouldHandleMovementInAllDirections() {
        // Test that robot can move in all 4 cardinal directions
        Robot robot = new Robot(new Position(new Coordinate(2, 2), Orientation.N), workspace);
        
        // Move North
        robot.execute(Command.M);
        assertEquals(new Coordinate(2, 3), robot.getPosition().coordinate());
        
        // Turn and move East
        robot.execute(Command.R);
        robot.execute(Command.M);
        assertEquals(new Coordinate(3, 3), robot.getPosition().coordinate());
        
        // Turn and move South
        robot.execute(Command.R);
        robot.execute(Command.M);
        assertEquals(new Coordinate(3, 2), robot.getPosition().coordinate());
        
        // Turn and move West
        robot.execute(Command.R);
        robot.execute(Command.M);
        assertEquals(new Coordinate(2, 2), robot.getPosition().coordinate());
    }

    @Test
    void shouldMaintainPositionAfterMultipleBlockedMoves() {
        Robot robot = new Robot(new Position(new Coordinate(0, 0), Orientation.S), workspace);
        robot.execute(new Instructions(List.of(Command.M, Command.M, Command.M, Command.M)));
        
        assertEquals(new Coordinate(0, 0), robot.getPosition().coordinate());
        assertEquals(Orientation.S, robot.getPosition().orientation());
    }
}
