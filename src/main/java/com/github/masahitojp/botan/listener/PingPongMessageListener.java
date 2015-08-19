package com.github.masahitojp.botan.listener;

import com.github.masahitojp.botan.exception.BotanException;
import com.github.masahitojp.botan.listener.BotanMessageListenerRegister;
import com.github.masahitojp.botan.utils.BotanUtils;

@SuppressWarnings("unused")
public class PingPongMessageListener implements BotanMessageListenerRegister {

    @Override
    public void register() {
        BotanUtils.respond(
                "ping",
                "ping method",
                message -> {
                    try {
                        message.reply("pong");
                    } catch (BotanException e) {
                        e.printStackTrace();
                    }
                });
    }
}
