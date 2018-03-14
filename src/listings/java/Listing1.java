package com.github.tomboyo.serializationStudy;

import org.junit.Test;

import java.io.*;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

/**
 * Listing 1: Serialize and deserialize an object.
 *
 * Serialization is the process of converting an eligible object into a sequence
 * of bytes which can be reconstituted into an object at a later time.
 * Deserialization is the process of reconstituting those bytes back into an
 * object.
 *
 * An object which is eligible for serialization implements the
 * java.io.Serializable interface, which is simply a marker interface.
 * Objects which do not implement Serializable can not be serialized. Any
 * attempt to do so will result in a java.io.NotSerializableException.
 *
 * Serializable objects are serialized using ObjectOutputStreams and
 * deserialized using ObjectInputStreams, as demonstrated below.
 */
public class Listing1 {

    private static class CanSerialize implements Serializable { }

    private static class CanNotSerialize { }

    @Test
    public void canSerializeTheSerializableClass() {
        CanSerialize instance = new CanSerialize();
        Object copy = null;

        try {
            copy = deserialize(
                    serialize(instance));
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to serialize or deserialize the object");
        } catch (ClassNotFoundException e) {
            fail("Not applicable. This will be discussed in another example.");
        }

        assertTrue("Deserialized data occupies a different memory address",
                instance != copy);
        assertTrue("The deserialized object is reconstituted",
                copy != null);
        assertTrue("The deserialized object is the expected type",
                copy instanceof CanSerialize);
    }

    @Test(expected = IOException.class)
    public void canNotSerializeTheNonSerializableClass() throws IOException {
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