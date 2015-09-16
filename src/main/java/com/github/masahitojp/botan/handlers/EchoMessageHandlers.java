package com.github.masahitojp.botan.handlers;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;

@SuppressWarnings("unused")
public class EchoMessageHandlers implements BotanMessageHandlers {

    @Override
    public final void register(final Robot robot) {
        robot.respond(
                "echo\\s+(?<body>.+)",
                "echo your message",
                message -> {
                    String body = message.getMatcher().group("body");
                    message.reply(body);
                }
        );
    }
}
