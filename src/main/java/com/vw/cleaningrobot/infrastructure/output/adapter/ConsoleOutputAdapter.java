package com.vw.cleaningrobot.infrastructure.output.adapter;

import com.vw.cleaningrobot.application.port.output.OutputPort;

public class ConsoleOutputAdapter implements OutputPort {

    @Override
    public void write(String output) {
        System.out.println(output);
    }
}
