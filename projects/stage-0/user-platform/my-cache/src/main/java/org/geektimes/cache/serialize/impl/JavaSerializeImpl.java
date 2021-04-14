package org.geektimes.cache.serialize.impl;

import org.geektimes.cache.serialize.AbstractCacheSerialize;

import javax.cache.CacheException;
import java.io.*;

/**
 * Java 原生序列化
 * @author jitwxs
 * @date 2021年04月14日 22:38
 */
public class JavaSerializeImpl<S> extends AbstractCacheSerialize<S, byte[]> {
    public JavaSerializeImpl(Class<S> sourceClass) {
        super(sourceClass);
    }

    @Override
    public byte[] serialize(S source) {
        if(source == null) {
            return null;
        }

        byte[] bytes;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(source);
            bytes = outputStream.toByteArray();
        } catch (IOException e) {
            throw new CacheException(e);
        }
        return bytes;
    }

    @Override
    public S deSerialize(byte[] target) {
        if (target == null) {
            return null;
        }
        S value;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(target);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)
        ) {
            // byte[] -> Value
            value = (S) objectInputStream.readObject();
        } catch (Exception e) {
            throw new CacheException(e);
        }
        return value;
    }
}
