package com.github.masahitojp.botan.handlers;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;

@SuppressWarnings("unused")
public class PingMessageHandlers implements BotanMessageHandlers {
    public static String DESCRIPTION = "ping method";

    @Override
    public void register(final Robot robot) {
        robot.respond(
                "ping\\z",
                DESCRIPTION,
                message -> message.reply("pong"));
    }
}
