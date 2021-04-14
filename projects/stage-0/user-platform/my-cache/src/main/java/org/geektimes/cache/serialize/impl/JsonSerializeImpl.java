package org.geektimes.cache.serialize.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geektimes.cache.serialize.AbstractCacheSerialize;

import javax.cache.CacheException;
import java.io.IOException;

/**
 * Json 序列化
 * @author jitwxs
 * @date 2021年04月14日 22:38
 */
public class JsonSerializeImpl<S> extends AbstractCacheSerialize<S, String> {
    public JsonSerializeImpl(Class<S> sourceClass) {
        super(sourceClass);
    }

    @Override
    public String serialize(S source) {
        if(source == null) {
            return "";
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public S deSerialize(String target) {
        if(target == null || "".equals(target)) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(target, sourceClass);
        } catch (IOException e) {
            throw new CacheException(e);
        }
    }
}
