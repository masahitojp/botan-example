package com.github.masahitojp.botan.brain;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.stream.Collectors;

public class MapDBBrain implements BotanBrain {
    private final String path;
    private final String tableName;
    private DB db;
    private ConcurrentNavigableMap<byte[], byte[]> data;

    public MapDBBrain(String path, String tableName) {
        this.path = path;
        this.tableName = tableName;
    }

    @Override
    public final Optional<byte[]> get(final byte[]key ) {
        return Optional.ofNullable(data.get(key));
    }

    @Override
    public final Optional<byte[]> put(byte[] key, byte[] value) {
        final Optional<byte[]> result = Optional.ofNullable(data.put(key, value));
        db.commit();
        return result;
    }

    @Override
    public final Optional<byte[]> delete(byte[] key) {
        final Optional<byte[]> result = Optional.ofNullable(data.remove(key));
        db.commit();
        return result;
    }


    @Override
    public Set<byte[]> keys(byte[] startsWith) {
        return this.data.keySet()
                .stream()
                .filter(key -> Arrays.asList(key).indexOf(startsWith) == 0)
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
