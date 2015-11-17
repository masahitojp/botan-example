package com.github.masahitojp.botan.handlers;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandler;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;

import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class HelpMessageHandlers implements BotanMessageHandlers {
    @Override
    public void register(final Robot robot) {
        robot.respond("help\\z", "Displays all of the commands.",
                message -> message.reply(
                        robot.getHandlers().stream().sorted().map(BotanMessageHandler::toString)
                                .filter(str -> !str.equals(""))
                                .collect(Collectors.joining("\n")))
        );
    }
}
