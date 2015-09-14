package com.github.masahitojp.botan.responder;

import com.github.masahitojp.botan.Robot;

@SuppressWarnings("unused")
public class HelpMessageResponder implements BotanMessageResponderRegister {
    @Override
    public void register(final Robot robot) {
        robot.respond("help", "show help", message -> {
            final StringBuilder builder = new StringBuilder();
            for (final BotanMessageResponder listener : robot.getListeners()) {
                final String line = listener.toString();
                builder.append(line);
            }
            message.reply(builder.toString());
        });
    }
}
