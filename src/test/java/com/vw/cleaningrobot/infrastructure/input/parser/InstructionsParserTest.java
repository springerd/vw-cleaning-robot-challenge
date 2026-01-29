package com.vw.cleaningrobot.infrastructure.input.parser;

import com.vw.cleaningrobot.domain.model.Command;
import com.vw.cleaningrobot.domain.model.Instructions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class InstructionsParserTest {

    @Test
    void shouldParseValidInstructions() {
        Instructions instructions = InstructionsParser.parse("LMLMLMLMM");
        assertEquals(9, instructions.commands().size());
        assertEquals(Command.L, instructions.commands().get(0));
        assertEquals(Command.M, instructions.commands().get(1));
        assertEquals(Command.M, instructions.commands().get(8));
    }

    @Test
    void shouldParseMixedCaseInstructions() {
        Instructions instructions = InstructionsParser.parse("LmR");
        assertEquals(Command.L, instructions.commands().get(0));
        assertEquals(Command.M, instructions.commands().get(1));
        assertEquals(Command.R, instructions.commands().get(2));
    }

    @Test
    void shouldFilterWhitespace() {
        Instructions instructions = InstructionsParser.parse("L M R");
        assertEquals(3, instructions.commands().size());
        assertEquals(Command.L, instructions.commands().get(0));
        assertEquals(Command.M, instructions.commands().get(1));
        assertEquals(Command.R, instructions.commands().get(2));
    }

    @Test
    void shouldFilterAllTypesOfWhitespace() {
        Instructions instructions = InstructionsParser.parse("L\nM\tR M");
        assertEquals(4, instructions.commands().size());
    }

    @Test
    void shouldRejectEmptyAfterFiltering() {
        assertThrows(IllegalArgumentException.class, 
            () -> InstructionsParser.parse("   \n\t  "));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void shouldRejectNullEmptyOrBlank(String input) {
        assertThrows(IllegalArgumentException.class, () -> InstructionsParser.parse(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"LMXR", "123"})
    void shouldRejectInvalidCommands(String input) {
        assertThrows(IllegalArgumentException.class, () -> InstructionsParser.parse(input));
    }

    @Test
    void shouldHandleSingleCommand() {
        Instructions instructions = InstructionsParser.parse("L");
        assertEquals(1, instructions.commands().size());
        assertEquals(Command.L, instructions.commands().get(0));
    }
}
