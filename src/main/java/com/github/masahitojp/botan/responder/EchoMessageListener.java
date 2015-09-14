package com.github.masahitojp.botan.responder;

import com.github.masahitojp.botan.Robot;

@SuppressWarnings("unused")
public class EchoMessageListener implements BotanMessageResponderRegister {

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
