package com.vw.cleaningrobot.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InstructionsTest {

    @ParameterizedTest
    @NullAndEmptySource
    void shouldRejectNullOrEmptyCommands(List<Command> commands) {
        assertThrows(IllegalArgumentException.class, 
            () -> new Instructions(commands));
    }

    @Test
    void shouldRejectListContainingNullCommands() {
        List<Command> commands = Arrays.asList(Command.L, null, Command.M);
        assertThrows(NullPointerException.class, 
            () -> new Instructions(commands));
    }

    @Test
    void shouldBeImmutable() {
        Instructions instructions = new Instructions(List.of(Command.L, Command.M, Command.R));
        List<Command> commands = instructions.commands();
        assertThrows(UnsupportedOperationException.class, 
            () -> commands.add(Command.M));
    }
}
