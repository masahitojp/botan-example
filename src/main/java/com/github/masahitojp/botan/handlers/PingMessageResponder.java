package com.github.masahitojp.botan.handlers;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;

@SuppressWarnings("unused")
public class PingMessageResponder implements BotanMessageHandlers {

    @Override
    public void register(final Robot robot) {
        robot.respond(
                "ping",
                "ping method",
                message -> message.reply("pong"));
    }
}
