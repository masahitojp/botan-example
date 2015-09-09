package com.github.masahitojp.botan.listener.cron;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.listener.BotanMessageListenerRegister;
import com.github.masahitojp.botan.message.BotanMessageSimple;
import com.google.gson.Gson;
import it.sauronsoftware.cron4j.InvalidPatternException;
import it.sauronsoftware.cron4j.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class CronListener implements BotanMessageListenerRegister {
    private static Logger logger = LoggerFactory.getLogger(CronListener.class);
    private static String NAME_SPACE = "cronjob_";

    private final ConcurrentHashMap<Integer, String> cronIds = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CronJob> runningJobs = new ConcurrentHashMap<>();
    private Scheduler scheduler;

    @Override
    public final void initialize(final Robot robot) {
        scheduler = new Scheduler();
        // start cron4j scheduler.
        scheduler.start();

        remember(robot);
    }

    private int genereteId() {
        final Random random = new Random();
        int id;
        do {
            id =random.nextInt(10000);
        } while (cronIds.containsKey(id));
        return id;
    }

    private void remember(final Robot robot) {
        final Gson gson = new Gson();
        robot.getBrain().search(NAME_SPACE).forEach(entry -> {
            final String json = new String(entry.getValue());
            final CronJob job = gson.fromJson(json , CronJob.class);

            try {
                // every minute.
                final String id = scheduler.schedule(job.schedule, () -> {
                    robot.receive(new BotanMessageSimple(job.message, job.to, job.to, job.to, -1));
                });
                int index = entry.getKey().indexOf(NAME_SPACE);
                int jobId = Integer.parseInt(entry.getKey().substring(index));
                cronIds.put(jobId, id);
                runningJobs.put(NAME_SPACE + jobId, job);
            } catch (final InvalidPatternException | NumberFormatException e) {
                logger.warn("job register failed: {}", e);
            }
        });
    }

    @Override
    public void register(final Robot robot) {

        robot.respond(
                "job\\s+add\\s+\"(?<schedule>.+)\"\\s+(?<message>.+)$",
                "add job",
                message -> {
                    try {
                        final Gson gson = new Gson();
                        final String id = scheduler.schedule(message.getMatcher().group("schedule"), () -> {
                            message.reply(message.getMatcher().group("message"));
                        });
                        final CronJob job =
                                new CronJob(message.getMatcher().group("schedule"), message.getFrom(), message.getMatcher().group("message"));
                        runningJobs.put(id, job);
                        int jobId = genereteId();
                        cronIds.put(jobId, id);
                        robot.getBrain().put(NAME_SPACE + jobId, gson.toJson(job).getBytes());
                        message.reply(id);
                    } catch (final InvalidPatternException e) {
                        message.reply("job register failed:" + e.getMessage());
                    }
                }
        );

        robot.respond(
                "job\\s+ls$",
                "show job list",
                botanMessage -> {
                    final StringBuilder sb = new StringBuilder();
                    for (final Map.Entry<String, CronJob> a : runningJobs.entrySet()) {
                        sb.append(String.format("%s: \"%s\" %s\n", a.getKey(), a.getValue().schedule, a.getValue().message));
                    }
                    final String result = sb.toString();
                    botanMessage.reply(result);
                }
        );

        robot.respond(
                "job\\s+rm\\s+(?<id>d+)$",
                "remove job from list",
                botanMessage -> {
                    final String idStr = botanMessage.getMatcher().group("id");
                    final int jobId = Integer.parseInt(idStr);
                    final String id = cronIds.get(jobId);
                    scheduler.deschedule(id);
                    runningJobs.remove(id);
                    robot.getBrain().delete(NAME_SPACE + id);
                    botanMessage.reply("job rm successful");
                }
        );
    }

    @Override
    public void beforeShutdown() {
            scheduler.stop();
    }
}
