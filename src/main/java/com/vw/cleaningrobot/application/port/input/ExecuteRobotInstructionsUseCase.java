package com.vw.cleaningrobot.application.port.input;

import com.vw.cleaningrobot.domain.model.Instructions;
import com.vw.cleaningrobot.domain.model.Position;

public interface ExecuteRobotInstructionsUseCase {
    Position execute(Position initialPosition, Instructions instructions);
}
