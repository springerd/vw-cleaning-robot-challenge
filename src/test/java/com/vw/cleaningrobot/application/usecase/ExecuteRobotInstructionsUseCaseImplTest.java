package com.vw.cleaningrobot.application.usecase;

import com.vw.cleaningrobot.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExecuteRobotInstructionsUseCaseImplTest {

    private ExecuteRobotInstructionsUseCaseImpl useCase;
    private Workspace workspace;

    @BeforeEach
    void setUp() {
        workspace = new Workspace(new Coordinate(5, 5));
        useCase = new ExecuteRobotInstructionsUseCaseImpl(workspace);
    }

    @Test
    void shouldExecuteRobotInstructions() {
        Position position = new Position(new Coordinate(1, 2), Orientation.N);
        Instructions instructions = new Instructions(List.of(Command.L, Command.M, Command.L, Command.M, Command.L, Command.M, Command.L, Command.M, Command.M));
        
        Position result = useCase.execute(position, instructions);
        
        assertEquals(new Coordinate(1, 3), result.coordinate());
        assertEquals(Orientation.N, result.orientation());
    }

    @Test
    void shouldExecuteSecondExample() {
        Position position = new Position(new Coordinate(3, 3), Orientation.E);
        Instructions instructions = new Instructions(List.of(Command.M, Command.M, Command.R, Command.M, Command.M, Command.R, Command.M, Command.R, Command.R, Command.M));
        
        Position result = useCase.execute(position, instructions);
        
        assertEquals(new Coordinate(5, 1), result.coordinate());
        assertEquals(Orientation.E, result.orientation());
    }

    @Test
    void shouldHandleRobotAtOrigin() {
        Position position = new Position(new Coordinate(0, 0), Orientation.N);
        Instructions instructions = new Instructions(List.of(Command.M));
        
        Position result = useCase.execute(position, instructions);
        
        assertEquals(new Coordinate(0, 1), result.coordinate());
        assertEquals(Orientation.N, result.orientation());
    }

    @Test
    void shouldHandleRobotAtUpperBound() {
        Position position = new Position(new Coordinate(5, 5), Orientation.N);
        Instructions instructions = new Instructions(List.of(Command.M));
        
        Position result = useCase.execute(position, instructions);
        
        assertEquals(new Coordinate(5, 5), result.coordinate());
        assertEquals(Orientation.N, result.orientation());
    }
}
