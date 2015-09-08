package com.github.masahitojp.botan.listener.cron;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.listener.BotanMessageListenerRegister;
import com.github.masahitojp.botan.message.BotanMessageSimple;
import com.google.gson.Gson;
import it.sauronsoftware.cron4j.Scheduler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class CronListener implements BotanMessageListenerRegister {

    public static ConcurrentHashMap<String, CronJob> hashMap = new ConcurrentHashMap<>();
    public static String dump() {
        final Gson gson = new Gson();
        final List<CronJob> jobs = hashMap.values().stream().map(data -> data).collect(Collectors.toList());
        return gson.toJson(jobs);
    }

    private void startUp(final Robot robot, final Scheduler scheduler) {
        final String to = "general@conference.cappybara.xmpp.slack.com";
        final String body = robot.getName() + " echo 週報 OR 勉強会　開始５分前です";
        // every minute.
        final String id = scheduler.schedule("* * * * *", () -> {
            robot.send(new BotanMessageSimple(body, to, to, to, -1));
        });
        hashMap.put(id, new CronJob("* * * * *", to, body));
    }

    @Override
    public void register(final Robot robot) {


        final Scheduler scheduler = new Scheduler();
        startUp(robot, scheduler);
        robot.respond(
                "job\\s+add\\s+\"(?<schedule>.+)\"\\s+(?<message>.+)$",
                "",
                message -> {
                    final String jobId = scheduler.schedule(message.getMatcher().group("schedule"), () -> {
                        message.reply(message.getMatcher().group("message"));
                    });
                    hashMap.put(jobId, new CronJob(message.getMatcher().group("schedule"), message.getFrom(), message.getMatcher().group("message")));
                    message.reply(jobId);
                }
        );

        robot.respond(
                "job\\s+ls$",
                "show job list",
                botanMessage -> {
                    final StringBuilder sb = new StringBuilder();
                    for (final Map.Entry<String, CronJob> a : hashMap.entrySet()) {
                        sb.append(String.format("%s: \"%s\" %s\n", a.getKey(), a.getValue().schedule, a.getValue().message));
                    }
                    final String result = sb.toString();
                    if (result.equals("")) {
                        botanMessage.reply("job is empty");
                    } else {
                        botanMessage.reply(result);
                    }
                }
        );

        robot.respond(
                "job\\s+rm\\s+(?<id>.+)$",
                "remove job from list",
                botanMessage -> {
                    final String idStr = botanMessage.getMatcher().group("id");
                    scheduler.deschedule(idStr);
                    hashMap.remove(idStr);
                    botanMessage.reply("job rm successful");
                }
        );

        // start cron4j scheduler.
        scheduler.start();

        robot.beforeShutdown(() -> {
            scheduler.stop();
            return true;
        });
    }
}
