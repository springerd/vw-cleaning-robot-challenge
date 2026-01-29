package com.vw.cleaningrobot.domain.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class OrientationTest {

    @ParameterizedTest
    @CsvSource({
        "N, W",
        "W, S",
        "S, E",
        "E, N"
    })
    void shouldTurnLeft(Orientation from, Orientation expected) {
        assertEquals(expected, from.turnLeft());
    }

    @ParameterizedTest
    @CsvSource({
        "N, E",
        "E, S",
        "S, W",
        "W, N"
    })
    void shouldTurnRight(Orientation from, Orientation expected) {
        assertEquals(expected, from.turnRight());
    }
}
