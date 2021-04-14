package org.geektimes.cache.serialize;

/**
 * 缓存序列化接口
 * @author jitwxs
 * @date 2021年04月14日 22:34
 */
public abstract class AbstractCacheSerialize<S, T> {
    protected Class<S> sourceClass;

    public AbstractCacheSerialize(Class<S> sourceClass) {
        this.sourceClass = sourceClass;
    }

    public abstract T serialize(S source);

    public abstract S deSerialize(T target);
}
