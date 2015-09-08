package com.github.masahitojp.botan.listener.cron;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.listener.BotanMessageListenerRegister;
import com.github.masahitojp.botan.message.BotanMessageSimple;
import it.sauronsoftware.cron4j.Scheduler;

@SuppressWarnings("unused")
public class CronListener implements BotanMessageListenerRegister {

    @Override
    public void register(final Robot robot) {
        final String to = "general@conference.cappybara-xmpp.xmpp.slack.com";
        final String body = "capybara echo 週報 OR 勉強会　開始５分前です";

        final Scheduler scheduler = new Scheduler();
        // every minute.
        scheduler.schedule("* * * * *", () -> {
            robot.receive(new BotanMessageSimple(body, to, to, to, -1));
        });

        robot.respond(
                "job\\s+add\\s+\"(?<schedule>.+)\"\\s+(?<message>.+)$",
                "",
                message -> scheduler.schedule("* * * * *", () -> {
                    message.reply(message.getMatcher().group("message"));
                })
        );

        // start cron4j scheduler.
        scheduler.start();

        robot.beforeShutdown(() -> {
            scheduler.stop();
            return true;
        });
    }
}
