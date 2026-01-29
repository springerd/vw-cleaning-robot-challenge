package com.vw.cleaningrobot.infrastructure.input.parser;

import com.vw.cleaningrobot.domain.model.Coordinate;
import com.vw.cleaningrobot.domain.model.Orientation;
import com.vw.cleaningrobot.domain.model.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class PositionParserTest {

    @Test
    void shouldParseValidPosition() {
        Position position = PositionParser.parse("1 2 N");
        assertEquals(new Coordinate(1, 2), position.coordinate());
        assertEquals(Orientation.N, position.orientation());
    }

    @Test
    void shouldParseLowercaseOrientation() {
        Position position = PositionParser.parse("3 4 e");
        assertEquals(Orientation.E, position.orientation());
    }

    @Test
    void shouldHandleExtraSpaces() {
        Position position = PositionParser.parse("  1   2   N  ");
        assertEquals(new Coordinate(1, 2), position.coordinate());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void shouldRejectNullOrEmpty(String input) {
        assertThrows(IllegalArgumentException.class, () -> PositionParser.parse(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1 2", "1 2 N E", "1"})
    void shouldRejectInvalidFormat(String input) {
        assertThrows(IllegalArgumentException.class, () -> PositionParser.parse(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"A 2 N", "1 B N", "A B N"})
    void shouldRejectInvalidCoordinates(String input) {
        assertThrows(IllegalArgumentException.class, () -> PositionParser.parse(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1 2 X", "1 2 Z", "1 2 1"})
    void shouldRejectInvalidOrientation(String input) {
        assertThrows(IllegalArgumentException.class, () -> PositionParser.parse(input));
    }
}
