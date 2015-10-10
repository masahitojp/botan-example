package com.github.masahitojp.botan.brain.mapdb;

import com.github.masahitojp.botan.brain.BotanBrain;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Optional;
import java.util.concurrent.*;

public class MapDBBrain implements BotanBrain {
    private static Logger log = LoggerFactory.getLogger(MapDBBrain.class);
    private static String KEY = "botan:brain";
    private final ConcurrentHashMap<String, String> data;
    private final DB db;
    private final ConcurrentMap<String, byte[]> inner;
    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    @SuppressWarnings("unused")
    public MapDBBrain() {
        this(
                Optional.ofNullable(System.getProperty("MAPDB_PATH")).orElse("./botan_map_db"),
                Optional.ofNullable(System.getProperty("MAPDB_TABLE_NAME")).orElse("botan")
        );
    }

    public MapDBBrain(final String path, final String tableName) {
        data = new ConcurrentHashMap<>();
        db = DBMaker.newFileDB(new File(path)).closeOnJvmShutdown().make();
        inner = db.createHashMap(tableName).make();
    }

    @Override
    public final ConcurrentHashMap<String, String> getData() {
        return data;
    }
    @Override
    public void beforeShutdown() {
        service.shutdown();
        db.close();
    }
    @Override
    public void initialize() {

        final byte[] a = inner.getOrDefault(KEY, new byte[]{});
        if (a != null && a.length > 0) {
            deserialize(a);
        }

        service.scheduleAtFixedRate(() -> {
            final byte[] result = serialize();
            if (result != null && result.length > 0) {
                inner.put(KEY, result);
            }
            db.commit();
        }, 5, 5, TimeUnit.SECONDS);
    }

    private byte[] serialize() {
        try (
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                final ObjectOutputStream oos = new ObjectOutputStream(baos)
        ) {
            oos.writeObject(data);
            oos.flush();
            return baos.toByteArray();
        } catch (final Exception e) {
            log.warn("{}", e);
        }
        return null;
    }

    private void deserialize(final byte[] storedData) {
        try {
            ByteArrayInputStream bi = new ByteArrayInputStream(storedData);
            ObjectInputStream si = new ObjectInputStream(bi);
            final ConcurrentHashMap<String, String> d = (ConcurrentHashMap<String, String>) si.readObject();
            d.forEach(data::put);
        } catch (Exception e) {
            log.warn("{}", e);
        }
    }

}
