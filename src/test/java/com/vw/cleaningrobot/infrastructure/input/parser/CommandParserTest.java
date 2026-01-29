package com.vw.cleaningrobot.infrastructure.input.parser;

import com.vw.cleaningrobot.domain.model.Command;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {

    @ParameterizedTest
    @CsvSource({
        "L, L",
        "l, L",
        "R, R",
        "r, R",
        "M, M",
        "m, M"
    })
    void shouldParseCommand(char input, Command expected) {
        assertEquals(expected, CommandParser.fromChar(input));
    }

    @ParameterizedTest
    @ValueSource(chars = {'X', '1', ' ', 'A'})
    void shouldThrowExceptionForInvalidCommand(char input) {
        assertThrows(IllegalArgumentException.class, () -> CommandParser.fromChar(input));
    }
}
