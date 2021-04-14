package org.geektimes.cache.serialize;

/**
 * 缓存序列化接口
 * @author jitwxs
 * @date 2021年04月14日 22:34
 */
public interface ICacheSerialize<S, T> {
    T serialize(S source);

    S deSerialize(T target);
}
