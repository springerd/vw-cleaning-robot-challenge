package com.vw.cleaningrobot.domain.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @ParameterizedTest
    @MethodSource("invalidPositionArguments")
    void shouldRejectInvalidPosition(Coordinate coord, Orientation orientation) {
        assertThrows(IllegalArgumentException.class, 
            () -> new Position(coord, orientation));
    }

    static Stream<Arguments> invalidPositionArguments() {
        return Stream.of(
            Arguments.of(null, Orientation.N),
            Arguments.of(new Coordinate(0, 0), null)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "0, 0, N, 0, 1",  // North
        "0, 0, E, 1, 0",  // East
        "1, 1, S, 1, 0",  // South
        "1, 1, W, 0, 1"   // West
    })
    void shouldMoveForward(int startX, int startY, Orientation orientation, int expectedX, int expectedY) {
        Position position = new Position(new Coordinate(startX, startY), orientation);
        Position moved = position.moveForward();
        
        assertEquals(new Coordinate(expectedX, expectedY), moved.coordinate());
        assertEquals(orientation, moved.orientation());
    }
}
