package com.github.masahitojp.botan.listener;

import com.github.masahitojp.botan.exception.BotanException;
import com.github.masahitojp.botan.utils.BotanUtils;

@SuppressWarnings("unused")
public class PlusPlusMessageListener implements BotanMessageListenerRegister {
    private static String KEY_FORMAT = "plusplus.key.%s";
    @Override
    public void register() {
        BotanUtils.respond(
                "\\s*(?<body>.+)\\Q++\\E",
                "string++",
                message -> {
                    try {
                        final String body = message.getMatcher().group("body");
                        final int result = message.getBrain().incr(String.format(KEY_FORMAT, body));
                        message.reply(String.format("%s : total(%d)", body, result));
                    } catch (BotanException e) {
                        e.printStackTrace();
                    }
                });
    }
}
