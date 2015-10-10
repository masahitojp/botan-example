package com.github.masahitojp.botan.brain.redis;

import com.github.masahitojp.botan.brain.BotanBrain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RedisBrain implements BotanBrain {
    private static Logger log = LoggerFactory.getLogger(RedisBrain.class);
    private static String KEY = "botan:brain";
    private ConcurrentHashMap<String, String> data;
    private JedisPool pool;
    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    public RedisBrain() {
        final String host = Optional.of(System.getProperty("REDIS_HOST")).orElse("localhost");
        final String redisPort = Optional.of(System.getProperty("REDIS_PORT")).orElse("6379");
        int port;
        try {
            port = Integer.parseInt(redisPort);
        } catch (final NumberFormatException ex) {
            port = 6379;
        }
        startUP(host, port);
    }

    public RedisBrain(String host, int port) {
        startUP(host, port);
    }

    private void startUP(String host, int port) {
        this.data = new ConcurrentHashMap<>();
        this.pool = new JedisPool(host, port);
    }

    @Override
    public final ConcurrentHashMap<String, String> getData() {
        return data;
    }

    @Override
    public void initialize() {

        // initialize data
        try (final Jedis jedis = pool.getResource()) {
            jedis.hgetAll(KEY).forEach(data::put);
        } catch (final Exception e) {
            log.warn("{}", e);
        }

        service.scheduleWithFixedDelay(() -> {
            try (final Jedis jedis = pool.getResource()) {
                data.forEach((k, v) -> jedis.hset(KEY, k, v));
            } catch (final Exception e) {
                log.warn("{}", e);
            }
        }, 30, 5, TimeUnit.SECONDS);
    }

    @Override
    public void beforeShutdown() {
        log.debug("before shutdown");
        if (pool != null) {
            pool.destroy();
        }
        service.shutdown();
    }
}
