package com.github.masahitojp.botan.listener;

import com.github.masahitojp.botan.Robot;

@SuppressWarnings("unused")
public class HelpMessageListener implements BotanMessageListenerRegister {
    @Override
    public void register(final Robot robot) {
        robot.respond("help", "show help", message -> {
            final StringBuilder builder = new StringBuilder();
            for (final BotanMessageListener listener : robot.getListeners()) {
                final String line = listener.toString();
                builder.append(line);
            }
            message.reply(builder.toString());
        });
    }
}
