package com.vw.cleaningrobot.infrastructure.output.adapter;

import com.vw.cleaningrobot.application.port.output.OutputPort;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileOutputAdapter implements OutputPort {

    private final Path filePath;

    public FileOutputAdapter(Path filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        this.filePath = filePath;
    }

    @Override
    public void write(String output) {
        try {
            Files.writeString(filePath, output + System.lineSeparator(), 
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to file: " + filePath, e);
        }
    }
}
