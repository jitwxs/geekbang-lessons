package org.geektimes.cache.redis;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.geektimes.cache.AbstractCache;
import org.geektimes.cache.ExpirableEntry;
import org.geektimes.cache.serialize.impl.JsonSerializeImpl;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import java.io.*;
import java.util.Set;

public class LettuceCache<K extends Serializable, V extends Serializable> extends AbstractCache<K, V, String> {

    private final GenericObjectPool<StatefulRedisConnection<String, String>> pool;

    public LettuceCache(CacheManager cacheManager, String cacheName, Configuration<K, V> configuration, GenericObjectPool<StatefulRedisConnection<String, String>> pool) {
        super(cacheManager, cacheName, configuration, JsonSerializeImpl.class);
        this.pool = pool;
    }

    @Override
    protected boolean containsEntry(K key) throws CacheException, ClassCastException {
        try (StatefulRedisConnection<String, String> connection = pool.borrowObject()) {
            RedisCommands<String, String> commands = connection.sync();

            return commands.exists(keySerialize.serialize(key)) > 0L;
        } catch (Exception e) {
            throw new CacheException("LettuceCache containsEntry ex", e);
        }
    }

    @Override
    protected ExpirableEntry<K, V> getEntry(K key) throws CacheException, ClassCastException {
        final String keyStr = keySerialize.serialize(key);

        try (StatefulRedisConnection<String, String> connection = pool.borrowObject()) {
            RedisCommands<String, String> commands = connection.sync();

            return ExpirableEntry.of(keySerialize.deSerialize(keyStr), valueSerialize.deSerialize(commands.get(keyStr)));
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    protected void putEntry(ExpirableEntry<K, V> entry) throws CacheException, ClassCastException {
        try (StatefulRedisConnection<String, String> connection = pool.borrowObject()) {
            RedisCommands<String, String> commands = connection.sync();
            commands.set(keySerialize.serialize(entry.getKey()), valueSerialize.serialize(entry.getValue()));
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    protected ExpirableEntry<K, V> removeEntry(K key) throws CacheException, ClassCastException {
        final ExpirableEntry<K, V> oldEntry = getEntry(key);
        if(oldEntry == null) {
            return null;
        }

        try (StatefulRedisConnection<String, String> connection = pool.borrowObject()) {
            RedisCommands<String, String> commands = connection.sync();
            commands.del(keySerialize.serialize(oldEntry.getKey()));

            return oldEntry;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    protected void clearEntries() throws CacheException {
        // TODO
    }


    @Override
    protected Set<K> keySet() {
        // TODO
        return null;
    }

    @Override
    protected void doClose() {
        this.pool.close();
    }
}
