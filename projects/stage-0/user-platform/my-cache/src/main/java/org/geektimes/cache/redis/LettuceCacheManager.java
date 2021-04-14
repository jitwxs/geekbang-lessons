package org.geektimes.cache.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.geektimes.cache.AbstractCacheManager;

import javax.cache.Cache;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.util.Properties;

/**
 * {@link javax.cache.CacheManager} based on Jedis
 */
public class LettuceCacheManager extends AbstractCacheManager {

    private final RedisClient redisClient;
    private final GenericObjectPool<StatefulRedisConnection<String, String>> pool;

    public LettuceCacheManager(CachingProvider cachingProvider, URI uri, ClassLoader classLoader, Properties properties) {
        super(cachingProvider, uri, classLoader, properties);

        this.redisClient = RedisClient.create(RedisURI.create(uri));
        this.pool = ConnectionPoolSupport.createGenericObjectPool(redisClient::connect, new GenericObjectPoolConfig());
    }

    @Override
    protected <K, V, C extends Configuration<K, V>> Cache doCreateCache(String cacheName, C configuration) {
        return new LettuceCache(this, cacheName, configuration, pool);
    }

    @Override
    protected void doClose() {
        pool.close();
        redisClient.shutdown();
    }
}
