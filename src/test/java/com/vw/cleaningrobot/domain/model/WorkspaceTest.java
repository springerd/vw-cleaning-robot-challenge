package com.vw.cleaningrobot.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class WorkspaceTest {

    @Test
    void shouldThrowExceptionForNullCoordinate() {
        assertThrows(IllegalArgumentException.class, () -> new Workspace(null));
    }

    @ParameterizedTest
    @CsvSource({"-1, 5", "5, -1", "-1, -1"})
    void shouldThrowExceptionForNegativeBounds(int x, int y) {
        Coordinate coordinate = new Coordinate(x, y);
        assertThrows(IllegalArgumentException.class, 
            () -> new Workspace(coordinate));
    }

    @Test
    void shouldThrowExceptionForZeroWorkspace() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Workspace(new Coordinate(0, 0)));
    }

    @ParameterizedTest
    @CsvSource({
        "0, 0, true",
        "5, 5, true",
        "2, 3, true",
        "5, 0, true",
        "0, 5, true"
    })
    void shouldBeWithinBounds(int x, int y, boolean expected) {
        Workspace workspace = new Workspace(new Coordinate(5, 5));
        Coordinate coordinate = new Coordinate(x, y);
        
        assertEquals(expected, workspace.isWithinBounds(coordinate));
    }

    @ParameterizedTest
    @CsvSource({
        "6, 5",
        "5, 6",
        "6, 6",
        "10, 10"
    })
    void shouldBeOutsideBounds(int x, int y) {
        Workspace workspace = new Workspace(new Coordinate(5, 5));
        Coordinate coordinate = new Coordinate(x, y);
        
        assertFalse(workspace.isWithinBounds(coordinate));
    }
}
