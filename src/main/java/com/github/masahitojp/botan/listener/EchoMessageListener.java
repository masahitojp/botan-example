package com.github.masahitojp.botan.listener;

import com.github.masahitojp.botan.Robot;

@SuppressWarnings("unused")
public class EchoMessageListener implements BotanMessageListenerRegister {

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
