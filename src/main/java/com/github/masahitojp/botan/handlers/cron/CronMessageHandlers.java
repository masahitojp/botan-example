package com.github.masahitojp.botan.handlers.cron;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;
import com.github.masahitojp.botan.message.BotanMessageSimple;
import com.google.gson.Gson;
import it.sauronsoftware.cron4j.InvalidPatternException;
import it.sauronsoftware.cron4j.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class CronMessageHandlers implements BotanMessageHandlers {
    public static String JOB_ADD_DESCRIPTION = "add job";
    public static String JOB_LIST_DESCRIPTION = "show job list";
    public static String JOB_RM_DESCRIPTION = "remove job from list";
    private static Logger logger = LoggerFactory.getLogger(CronMessageHandlers.class);
    private static String NAME_SPACE = "cronjob_";
    private final Object lock = new Object();
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
            int JOB_ID_MAX = 10000;
            id = random.nextInt(JOB_ID_MAX);
        } while (cronIds.containsKey(id));
        return id;
    }

    private void remember(final Robot robot) {
        final Gson gson = new Gson();
        robot.getBrain().getData().forEach((k, v) -> {
            if (k.startsWith(NAME_SPACE)) {

                final String json = robot.getBrain().getData().getOrDefault(k, "");
                final CronJob job = gson.fromJson(json, CronJob.class);

                try {
                    final String id = scheduler.schedule(job.schedule, () -> {
                        robot.send(new BotanMessageSimple(job.message, job.to, job.to, job.to, -1));
                    });
                    final int jobId = Integer.parseInt(k.substring(NAME_SPACE.length()));
                    cronIds.put(jobId, id);
                    runningJobs.put(id, job);
                } catch (final InvalidPatternException | NumberFormatException e) {
                    logger.warn("job register failed: {}", e);
                }
            }
        });

    }

    @Override
    public final void register(final Robot robot) {

        robot.respond(
                "job\\s+add\\s+\"(?<schedule>.+)\"\\s+(?<message>.+)$",
                JOB_ADD_DESCRIPTION,
                message -> {
                    try {
                        final Gson gson = new Gson();
                        final String id = scheduler.schedule(message.getMatcher().group("schedule"), () -> {
                            message.reply(message.getMatcher().group("message"));
                        });
                        final CronJob job =
                                new CronJob(message.getMatcher().group("schedule"), message.getTo(), message.getMatcher().group("message"));
                        runningJobs.put(id, job);
                        final int jobId;
                        synchronized (lock) {
                            jobId = genereteId();
                            cronIds.put(jobId, id);
                        }
                        robot.getBrain().getData().put(NAME_SPACE + jobId, gson.toJson(job));
                        message.reply(String.valueOf(jobId));
                    } catch (final InvalidPatternException e) {
                        message.reply("job register failed:" + e.getMessage());
                    }
                }
        );

        robot.respond(
                "job\\s+ls$",
                JOB_LIST_DESCRIPTION,
                botanMessage -> {
                    final StringBuilder sb = new StringBuilder();
                    cronIds.forEach((jobId,v) -> {
                        final CronJob job = runningJobs.get(v);
                        sb.append(String.format("%d: \"%s\" %s\n", jobId, job.schedule, job.message));
                    });
                    String result = sb.toString();
                    if (result.equals("")) {
                        result = "no jobs";
                    }
                    botanMessage.reply(result);
                }
        );

        robot.respond(
                "job\\s+rm\\s+(?<id>\\d+)$",
                JOB_RM_DESCRIPTION,
                message -> {
                    try {
                        final String idStr = message.getMatcher().group("id");
                        final int jobId = Integer.parseInt(idStr);
                        final String id = cronIds.get(jobId);
                        if (id == null || id.equals("")) {
                            message.reply(String.format("job rm failed: id %d not found", jobId));
                        } else {
                            scheduler.deschedule(id);
                            cronIds.remove(jobId);
                            runningJobs.remove(id);
                            robot.getBrain().getData().remove(NAME_SPACE + idStr);
                            message.reply("job rm successful");
                        }
                    } catch (final NumberFormatException e) {
                        message.reply("job rm failed");
                    }
                }
        );
    }

    @Override
    public final void beforeShutdown() {
        scheduler.stop();
    }
}
