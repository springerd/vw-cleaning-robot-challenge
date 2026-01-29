package com.vw.cleaningrobot.infrastructure.input.parser;

import com.vw.cleaningrobot.domain.model.Coordinate;
import com.vw.cleaningrobot.domain.model.Workspace;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class WorkspaceParserTest {

    @Test
    void shouldParseValidWorkspace() {
        Workspace workspace = WorkspaceParser.parse("5 5");
        assertEquals(new Coordinate(5, 5), workspace.upperRight());
    }

    @Test
    void shouldHandleExtraSpaces() {
        Workspace workspace = WorkspaceParser.parse("  10   20  ");
        assertEquals(new Coordinate(10, 20), workspace.upperRight());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void shouldRejectNullOrEmpty(String input) {
        assertThrows(IllegalArgumentException.class, () -> WorkspaceParser.parse(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"5", "5 5 5", "5 5 N"})
    void shouldRejectInvalidFormat(String input) {
        assertThrows(IllegalArgumentException.class, () -> WorkspaceParser.parse(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"A 5", "5 B", "A B"})
    void shouldRejectInvalidCoordinates(String input) {
        assertThrows(IllegalArgumentException.class, () -> WorkspaceParser.parse(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1 5", "5 -1", "-1 -1"})
    void shouldRejectNegativeCoordinates(String input) {
        assertThrows(IllegalArgumentException.class, () -> WorkspaceParser.parse(input));
    }
}
