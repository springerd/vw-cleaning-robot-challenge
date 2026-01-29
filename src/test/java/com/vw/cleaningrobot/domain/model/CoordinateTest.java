package com.vw.cleaningrobot.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {

    @Test
    void shouldAllowNegativeCoordinates() {
        Coordinate coord = new Coordinate(-1, -1);
        assertEquals(-1, coord.x());
        assertEquals(-1, coord.y());
    }
}
