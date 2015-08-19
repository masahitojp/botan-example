package com.github.masahitojp.botan.listener;

import com.github.masahitojp.botan.exception.BotanException;
import com.github.masahitojp.botan.listener.BotanMessageListenerRegister;
import com.github.masahitojp.botan.utils.BotanUtils;

@SuppressWarnings("unused")
public class EchoMessageListener implements BotanMessageListenerRegister {

    @Override
    public void register() {
        BotanUtils.respond(
                "echo\\s+(?<body>.+)",
                "echo your message",
                message -> {
                    try {
                        message.reply(message.getMatcher().group("body"));
                    } catch (BotanException e) {
                        e.printStackTrace();
                    }
                });
    }
}
