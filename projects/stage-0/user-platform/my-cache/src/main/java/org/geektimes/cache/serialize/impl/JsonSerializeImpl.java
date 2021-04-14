package org.geektimes.cache.serialize.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geektimes.cache.serialize.ICacheSerialize;

import javax.cache.CacheException;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;

/**
 * Json 序列化
 * @author jitwxs
 * @date 2021年04月14日 22:38
 */
public class JsonSerializeImpl<T> implements ICacheSerialize<T, String> {
    private Class<T> clazz;

    public JsonSerializeImpl() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        clazz = (Class<T>) type.getActualTypeArguments()[0];
    }

    @Override
    public String serialize(T source) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public T deSerialize(String target) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(target, clazz);
        } catch (IOException e) {
            throw new CacheException(e);
        }
    }
}
