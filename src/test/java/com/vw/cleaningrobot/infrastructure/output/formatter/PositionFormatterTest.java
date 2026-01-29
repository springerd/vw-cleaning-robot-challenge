package com.vw.cleaningrobot.infrastructure.output.formatter;

import com.vw.cleaningrobot.domain.model.Coordinate;
import com.vw.cleaningrobot.domain.model.Orientation;
import com.vw.cleaningrobot.domain.model.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class PositionFormatterTest {

    @ParameterizedTest
    @CsvSource({
        "1, 2, N, '1 2 N'",
        "3, 3, E, '3 3 E'",
        "5, 1, E, '5 1 E'",
        "0, 0, S, '0 0 S'"
    })
    void shouldFormatPosition(int x, int y, Orientation orientation, String expected) {
        Position position = new Position(new Coordinate(x, y), orientation);
        assertEquals(expected, PositionFormatter.format(position));
    }

    @Test
    void shouldThrowExceptionForNullPosition() {
        assertThrows(IllegalArgumentException.class, () -> PositionFormatter.format(null));
    }
}
