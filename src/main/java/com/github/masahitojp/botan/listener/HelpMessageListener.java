package com.github.masahitojp.botan.listener;

import com.github.masahitojp.botan.exception.BotanException;
import com.github.masahitojp.botan.listener.BotanMessageListenerRegister;
import com.github.masahitojp.botan.listener.BotanMessageListenerSetter;
import com.github.masahitojp.botan.utils.BotanUtils;

@SuppressWarnings("unused")
public class HelpMessageListener implements BotanMessageListenerRegister {
    @Override
    public void register() {
        BotanUtils.respond("help", "show help", message -> {
            try {
                final StringBuilder builder = new StringBuilder();
                for (final BotanMessageListenerSetter setter : BotanUtils.getActions()) {
                    final String line = String.format("%s %s - %s\n", message.getRobotName(), setter.getPatterString(), setter.getDescription());
                    builder.append(line);
                }
                message.reply(builder.toString());
            } catch (BotanException e) {
                e.printStackTrace();
            }
        });
    }
}
