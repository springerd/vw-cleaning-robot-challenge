package com.vw.cleaningrobot.infrastructure.output.adapter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class ConsoleOutputAdapterTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private ConsoleOutputAdapter adapter;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
        adapter = new ConsoleOutputAdapter();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void shouldWriteToConsole() {
        adapter.write("1 3 N");
        assertEquals("1 3 N" + System.lineSeparator(), outputStream.toString());
    }

    @Test
    void shouldWriteMultipleLines() {
        adapter.write("1 3 N");
        adapter.write("5 1 E");
        
        String expected = "1 3 N" + System.lineSeparator() + 
                         "5 1 E" + System.lineSeparator();
        assertEquals(expected, outputStream.toString());
    }
}
