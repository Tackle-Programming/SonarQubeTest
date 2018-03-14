package com.github.tomboyo.serializationStudy;

import java.io.*;

public final class SerializeUtil {

    public static byte[] serialize(Object toSerialize)
            throws IOException {
        try(ByteArrayOutputStream byteStream =
                    new ByteArrayOutputStream();
            ObjectOutputStream objectStream =
                    new ObjectOutputStream(byteStream)
        ) {
            objectStream.writeObject(toSerialize);
            return byteStream.toByteArray();
        }
    }

    public static <T> T deserialize(byte[] serializedInstance, Class<T> type)
            throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteStream =
                     new ByteArrayInputStream(serializedInstance);
             ObjectInputStream objectStream =
                     new ObjectInputStream(byteStream)
        ) {
            return type.cast(objectStream.readObject());
        }
    }
}
