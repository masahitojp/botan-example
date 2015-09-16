package com.github.masahitojp.botan.handlers;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandler;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;

@SuppressWarnings("unused")
public class HelpMessageHandlers implements BotanMessageHandlers {
    @Override
    public void register(final Robot robot) {
        robot.respond("help", "show help", message -> {
            final StringBuilder builder = new StringBuilder();
            for (final BotanMessageHandler listener : robot.getHandlers()) {
                final String line = listener.toString();
                builder.append(line);
            }
            message.reply(builder.toString());
        });
    }
}
