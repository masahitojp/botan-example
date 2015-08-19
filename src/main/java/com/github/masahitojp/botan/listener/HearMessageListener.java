package com.github.masahitojp.botan.listener;

import com.github.masahitojp.botan.exception.BotanException;
import com.github.masahitojp.botan.listener.BotanMessageListenerRegister;
import com.github.masahitojp.botan.utils.BotanUtils;

@SuppressWarnings("unused")
public class HearMessageListener implements BotanMessageListenerRegister {

    @Override
    public void register() {
        BotanUtils.hear(
                "(?<body>.+)",
                "hear",
                message -> {
//                    try {
//                        message.reply("hear:" + message.getMatcher().group("body"));
//                    } catch (BotanException e) {
//                        e.printStackTrace();
//                    }
                });
    }
}
