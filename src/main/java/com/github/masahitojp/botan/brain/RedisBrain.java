package com.github.masahitojp.botan.brain;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Optional;
import java.util.Set;

public class RedisBrain implements BotanBrain {
    private final String host;
    private final int port;
    private JedisPool pool = null;

    public RedisBrain(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public final Optional<byte[]> get(byte[] key) {
        try (final Jedis jedis = pool.getResource()) {
            return Optional.ofNullable(jedis.get(key));
        }
    }

    @Override
    public final Optional<byte[]> put(byte[] key, byte[] value) {
        try (final Jedis jedis = pool.getResource()) {
            return Optional.ofNullable(jedis.set(key, value).getBytes());
        }
    }

    @Override
    public final Optional<byte[]> delete(final byte[] key) {
        try (final Jedis jedis = pool.getResource()) {
            final Optional<byte[]> result = this.get(key);
            jedis.del(key);
            return result;
        }
    }

    @Override
    public Set<byte[]> keys(byte[] startsWith) {
        try (final Jedis jedis = pool.getResource()) {
            return jedis.keys(startsWith);
        }
    }

    @Override
    public void initialize() {
        pool = new JedisPool(host, port);
    }

    @Override
    public void beforeShutdown() {
        if (pool != null) {
            pool.destroy();
        }
    }
}
