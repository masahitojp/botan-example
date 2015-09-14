package com.github.masahitojp.botan.responder;

import com.github.masahitojp.botan.Robot;

@SuppressWarnings("unused")
public class PingPongMessageListener implements BotanMessageResponderRegister {

    @Override
    public void register(final Robot robot) {
        robot.respond(
                "ping",
                "ping method",
                message -> message.reply("pong"));
    }
}
