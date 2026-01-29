package com.vw.cleaningrobot.application.usecase;

import com.vw.cleaningrobot.application.port.input.ExecuteRobotInstructionsUseCase;
import com.vw.cleaningrobot.domain.model.*;

public class ExecuteRobotInstructionsUseCaseImpl implements ExecuteRobotInstructionsUseCase {
    
    private final Workspace workspace;

    public ExecuteRobotInstructionsUseCaseImpl(Workspace workspace) {
        this.workspace = workspace;
    }

    @Override
    public Position execute(Position initialPosition, Instructions instructions) {
        Robot robot = new Robot(initialPosition, workspace);
        robot.execute(instructions);
        return robot.getPosition();
    }
}
