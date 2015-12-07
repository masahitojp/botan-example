package com.github.masahitojp.botan.brain.redis;

import com.github.masahitojp.botan.brain.BotanBrain;
import com.github.masahitojp.botan.utils.BotanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RedisBrain implements BotanBrain {
    private static Logger log = LoggerFactory.getLogger(RedisBrain.class);
    private static String KEY = "botan:brain";
    private ConcurrentHashMap<String, String> data;
    private ConcurrentHashMap<String, String> old;
    private JedisPool pool;
    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    public RedisBrain() {
        final String host = BotanUtils.envToOpt("REDIS_HOSTNAME").orElse("localhost");
        final String redisPort = BotanUtils.envToOpt("REDIS_PORT").orElse("6379");
        final String redisPassword = BotanUtils.envToOpt("REDIS_PASSWORD").orElse("");
        final int timeout = 2000;
        int port;
        try {
            port = Integer.parseInt(redisPort);
        } catch (final NumberFormatException ex) {
            port = 6379;
        }
        startUP(host, port, timeout, redisPassword);
    }

    @SuppressWarnings("unused")
    public RedisBrain(String host, int port, String password) {
        startUP(host, port, 2000, password);
    }

    private void startUP(String host, int port, int timeout, String password) {
        this.data = new ConcurrentHashMap<>();
        if (password!= null && !password.equals("")) {
            final JedisPoolConfig poolConfig = new JedisPoolConfig();
            this.pool = new JedisPool(poolConfig, host, port, timeout, password);
        } else {
            this.pool = new JedisPool(host, port);
        }
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
        old = new ConcurrentHashMap<>(data);

        service.scheduleWithFixedDelay(() -> {
            if(!equalMaps(old, data)) {
                try (final Jedis jedis = pool.getResource()) {
                    data.forEach((k, v) -> jedis.hset(KEY, k, v));
                } catch (final Exception e) {
                    log.warn("{}", e);
                }
                old = new ConcurrentHashMap<>(data);
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

    @Override
    public void beforeShutdown() {
        log.debug("before shutdown");
        if (pool != null) {
            pool.destroy();
        }
        service.shutdown();
    }

    private <K,V> boolean equalMaps(Map<K,V> m1, Map<K,V> m2) {
        if (m1.equals(m2)) return true;
        if (m1.size() != m2.size()) return false;
        for (final K key: m1.keySet())
            if (!m1.get(key).equals(m2.get(key)))
                return false;
        return true;
    }
}
