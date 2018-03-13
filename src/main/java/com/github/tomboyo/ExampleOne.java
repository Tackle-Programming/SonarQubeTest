package com.github.tomboyo;

import org.junit.Test;

import java.io.*;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

public class ExampleOne {

    class CanSerialize implements Serializable { }

    class CanNotSerialize { }

    @Test
    public void canSerializeMySerializableClass() {
        CanSerialize instance = new CanSerialize();
        CanSerialize copy = null;

        try {
            copy = deserialize(
                    serialize(instance),
                    CanSerialize.class);
        } catch (IOException e) {
            fail("Failed to serialize or deserialize the object");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            fail("Not applicable. This will be discussed in another example.");
        }

        assertTrue("Deserialized data occupies a different memory address",
                instance != copy);
    }

    @Test(expected = IOException.class)
    public void canNotSerializeMyNonSerializableClass() throws IOException {
        CanNotSerialize instance = new CanNotSerialize();

        serialize(instance);
        fail("Classes must implement java.io.Serializable to serialize");
    }

    private byte[] serialize(Object toSerialize)
            throws IOException {
        try(ByteArrayOutputStream byteStream =
                    new ByteArrayOutputStream();
            ObjectOutputStream objectStream =
                    new ObjectOutputStream(byteStream)
        ) {
            objectStream.writeObject(toSerialize);
            return  byteStream.toByteArray();
        }
    }

    private <T> T deserialize(byte[] serializedInstance, Class<T> type)
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