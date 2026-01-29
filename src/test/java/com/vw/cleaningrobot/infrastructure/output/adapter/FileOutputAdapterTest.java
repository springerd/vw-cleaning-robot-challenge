package com.vw.cleaningrobot.infrastructure.output.adapter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class FileOutputAdapterTest {

    private Path tempFile;
    private FileOutputAdapter adapter;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("test-output", ".txt");
        adapter = new FileOutputAdapter(tempFile);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    void shouldWriteToFile() throws IOException {
        adapter.write("1 3 N");
        
        String content = Files.readString(tempFile);
        assertEquals("1 3 N" + System.lineSeparator(), content);
    }

    @Test
    void shouldAppendToFile() throws IOException {
        adapter.write("1 3 N");
        adapter.write("5 1 E");
        
        String content = Files.readString(tempFile);
        String expected = "1 3 N" + System.lineSeparator() + 
                         "5 1 E" + System.lineSeparator();
        assertEquals(expected, content);
    }

    @Test
    void shouldThrowExceptionForNullPath() {
        assertThrows(IllegalArgumentException.class, () -> new FileOutputAdapter(null));
    }

    @Test
    void shouldThrowExceptionForInvalidPath() {
        Path invalidPath = Path.of("/invalid/path/file.txt");
        FileOutputAdapter invalidAdapter = new FileOutputAdapter(invalidPath);
        
        assertThrows(RuntimeException.class, () -> invalidAdapter.write("test"));
    }
}
