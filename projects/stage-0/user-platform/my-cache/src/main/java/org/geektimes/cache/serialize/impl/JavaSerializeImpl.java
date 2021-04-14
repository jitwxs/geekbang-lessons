package org.geektimes.cache.serialize.impl;

import org.geektimes.cache.serialize.ICacheSerialize;

import javax.cache.CacheException;
import java.io.*;

/**
 * Java 原生序列化
 * @author jitwxs
 * @date 2021年04月14日 22:38
 */
public class JavaSerializeImpl<T> implements ICacheSerialize<T, byte[]> {
    @Override
    public byte[] serialize(T source) {
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
    public T deSerialize(byte[] target) {
        if (target == null) {
            return null;
        }
        T value;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(target);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)
        ) {
            // byte[] -> Value
            value = (T) objectInputStream.readObject();
        } catch (Exception e) {
            throw new CacheException(e);
        }
        return value;
    }
}
