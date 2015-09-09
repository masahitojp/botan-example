package com.github.masahitojp.botan.brain;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapDBBrain implements BotanBrain {
    private final StampedLock lock = new StampedLock();
    private final String path;
    private final String tableName;
    private DB db;
    private ConcurrentNavigableMap<String, byte[]> data;

    public MapDBBrain(String path, String tableName) {
        this.path = path;
        this.tableName = tableName;
    }

    @Override
    public final Optional<byte[]> get(String key) {
        return Optional.ofNullable(data.get(key));
    }

    @Override
    public final Optional<byte[]> put(String key, byte[] value) {
        final Optional<byte[]> result = Optional.ofNullable(data.put(key, value));
        db.commit();
        return result;
    }

    @Override
    public final Optional<byte[]> delete(String key) {
        final Optional<byte[]> result = Optional.ofNullable(data.remove(key));
        db.commit();
        return result;
    }

    private int getInteger(String key, Function<Integer, Integer> func) {
        final long stamp = lock.writeLock();
        final byte[] value = data.get(key);
        final int before;
        final ByteBuffer buffer;
        if (value != null) {
            buffer = ByteBuffer.wrap(value);
            before = buffer.getInt();
        } else {
            before = 0;
            buffer = ByteBuffer.allocate(4);
        }
        final int result = func.apply(before);
        buffer.clear();
        buffer.putInt(result);
        data.put(key, buffer.array());
        lock.unlock(stamp);
        db.commit();
        return result;
    }

    @Override
    public int incr(String key) {
        return getInteger(key, t -> t + 1);
    }

    @Override
    public int decr(String key) {
        return getInteger(key, t -> (t - 1) > 0 ? (t - 1) : 0);
    }

    @Override
    public Set<Map.Entry<String, byte[]>> search(String startsWith) {
        return this.data.entrySet()
                .stream()
                .filter(e -> e.getKey().startsWith(startsWith))
                .collect(Collectors.toSet());
    }

    @Override
    public void initialize() {
        db = DBMaker.fileDB(new File(path))
                .transactionDisable()
                .closeOnJvmShutdown()
                .make();
        data = db.treeMap(tableName);
    }

    @Override
    public void beforeShutdown() {
        db.close();
    }
}
