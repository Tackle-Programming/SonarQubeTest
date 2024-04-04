package com.github.tomboyo.serializationStudy;

import org.junit.Test;

import java.io.*;

import static junit.framework.TestCase.assertTrue;

/**
 * Listing 1: Serialize and deserialize with `ObjectOuputStream` and
 * `ObjectInputStream`
 *
 * Java objects and primitives can be serialized into and deserialized from
 * byte sequences, which allows information to be shared over networks,
 * persisted to disc, and so on.
 *
 * An object which implements the java.io.Serializable or
 * java.io.Externalizable interfaces can be serialized. Objects which do
 * not cannot be serialized, and any attempt to do so will result in
 * java.io.NotSerializableExceptions. (This listing will focus on the
 * Serializable interface TODO: A later listing will focus on Externalizable.)
 *
 * Serializable objects are serialized using ObjectOutputStreams and
 * deserialized using ObjectInputStreams, as demonstrated below.
 */
public class Listing1 {

    private static class CanSerialize implements Serializable { }


    
    private static class CanNotSerialize { }

    @Test
    public void canSerializeTheSerializableClass() throws Exception {
        CanSerialize instance = new CanSerialize();
        Object copy;

        copy = deserialize(
                serialize(instance));

        assertTrue("Deserialized data occupies a different memory address",
                instance != copy);
        assertTrue("The deserialized object is reconstituted",
                copy != null);
        assertTrue("The deserialized object is the expected type",
                copy instanceof CanSerialize);
    }

    @Test(expected = NotSerializableException.class)
    public void canNotSerializeTheNonSerializableClass() throws IOException {
        serialize(new CanNotSerialize());
    }

    private byte[] serialize(Object toSerialize)
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

    private Object deserialize(byte[] serializedInstance)
            throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteStream =
                     new ByteArrayInputStream(serializedInstance);
             ObjectInputStream objectStream =
                     new ObjectInputStream(byteStream)
        ) {
            return objectStream.readObject();
        }
    }

}
