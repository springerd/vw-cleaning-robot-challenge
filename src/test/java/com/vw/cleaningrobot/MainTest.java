package com.vw.cleaningrobot;

import com.vw.cleaningrobot.application.port.output.OutputPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private static class TestOutputAdapter implements OutputPort {
        private final List<String> outputs = new ArrayList<>();

        @Override
        public void write(String output) {
            outputs.add(output);
        }

        public List<String> getOutputs() {
            return outputs;
        }
    }

    @Test
    void shouldProcessSingleRobot() {
        String input = """
                5 5
                1 2 N
                LMLMLMLMM
                """;
        TestOutputAdapter output = new TestOutputAdapter();

        Main.run(new ByteArrayInputStream(input.getBytes()), output);

        assertEquals(1, output.getOutputs().size());
        assertEquals("1 3 N", output.getOutputs().get(0));
    }

    @Test
    void shouldProcessMultipleRobots() {
        String input = """
                5 5
                1 2 N
                LMLMLMLMM
                3 3 E
                MMRMMRMRRM
                """;
        TestOutputAdapter output = new TestOutputAdapter();

        Main.run(new ByteArrayInputStream(input.getBytes()), output);

        assertEquals(2, output.getOutputs().size());
        assertEquals("1 3 N", output.getOutputs().get(0));
        assertEquals("5 1 E", output.getOutputs().get(1));
    }

    @Test
    void shouldHandleRobotAtBoundary() {
        String input = """
                5 5
                0 0 S
                M
                """;
        TestOutputAdapter output = new TestOutputAdapter();

        Main.run(new ByteArrayInputStream(input.getBytes()), output);

        assertEquals(1, output.getOutputs().size());
        assertEquals("0 0 S", output.getOutputs().get(0));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid\n1 2 N\nM",           // Invalid workspace
        "5 5\n1 2 X\nM",               // Invalid position
        "5 5\n1 2 N\nLMXR",            // Invalid instructions
        "5 5\n1 2 N"                   // Missing instructions
    })
    void shouldThrowExceptionForInvalidInput(String input) {
        TestOutputAdapter output = new TestOutputAdapter();
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        
        assertThrows(IllegalArgumentException.class, () -> Main.run(inputStream, output));
    }
}
