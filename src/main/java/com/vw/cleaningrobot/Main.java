package com.vw.cleaningrobot;

import com.vw.cleaningrobot.application.port.input.ExecuteRobotInstructionsUseCase;
import com.vw.cleaningrobot.application.port.output.OutputPort;
import com.vw.cleaningrobot.application.usecase.ExecuteRobotInstructionsUseCaseImpl;
import com.vw.cleaningrobot.domain.model.Instructions;
import com.vw.cleaningrobot.domain.model.Position;
import com.vw.cleaningrobot.domain.model.Workspace;
import com.vw.cleaningrobot.infrastructure.input.parser.InstructionsParser;
import com.vw.cleaningrobot.infrastructure.input.parser.PositionParser;
import com.vw.cleaningrobot.infrastructure.input.parser.WorkspaceParser;
import com.vw.cleaningrobot.infrastructure.output.adapter.ConsoleOutputAdapter;
import com.vw.cleaningrobot.infrastructure.output.formatter.PositionFormatter;

import java.io.InputStream;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            run(System.in, new ConsoleOutputAdapter());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    static void run(InputStream input, OutputPort output) {
        try (Scanner scanner = new Scanner(input)) {
            // Parse workspace
            String workspaceLine = scanner.nextLine();
            Workspace workspace = WorkspaceParser.parse(workspaceLine);

            // Create use case
            ExecuteRobotInstructionsUseCase useCase = new ExecuteRobotInstructionsUseCaseImpl(workspace);

            // Process robots
            while (scanner.hasNextLine()) {
                String positionLine = scanner.nextLine();
                if (positionLine.isBlank()) {
                    break;
                }

                if (!scanner.hasNextLine()) {
                    throw new IllegalArgumentException("Missing instructions for position: " + positionLine);
                }
                String instructionsLine = scanner.nextLine();

                // Parse input
                Position initialPosition = PositionParser.parse(positionLine);
                Instructions instructions = InstructionsParser.parse(instructionsLine);

                // Execute
                Position result = useCase.execute(initialPosition, instructions);

                // Output
                String formatted = PositionFormatter.format(result);
                output.write(formatted);
            }
        }
    }
}
