package com.github.masahitojp.botan.listener;

import com.github.masahitojp.botan.utils.BotanUtils;

@SuppressWarnings("unused")
public class PingPongMessageListener implements BotanMessageListenerRegister {

    @Override
    public void register() {
        BotanUtils.respond(
                "ping",
                "ping method",
                message -> message.reply("pong"));
    }
}
