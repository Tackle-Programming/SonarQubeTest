package com.github.tomboyo.serializationStudy;

import org.junit.Test;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;

/**
 * Listing 2. Serialization of fields
 *
 * When an object is serialized, its references will be serialized
 * recursively to deeply represent the object.
 *
 * By default, all non-transient and non-static fields are serialized. All of
 * an objects members which can not be serialized must be marked transient or
 * else NotSerializableExceptions will be raised upon serialization of the
 * containing class.
 *
 * Data which is inherently volatile, such as locks and counters, should always
 * be marked transient. It makes little sense to serialize such data.
 */
public class Listing2 {

    private static final class A implements Serializable {
        private static int staticInt = 5;

        private final int persistentInt;
        private final String persistentString;
        private final transient int transientInt;
        private final transient String transientString;

        A(
                int persistentInt,
                String persistentString,
                int transientInt,
                String transientString) {
            this.persistentInt = persistentInt;
            this.transientInt = transientInt;

            this.persistentString = persistentString;
            this.transientString = transientString;
        }
    }

    @Test
    public void nontransientFieldsAreSerialized()
            throws IOException, ClassNotFoundException {

        final A in = new A(
                1,
                "cats",
                2,
                "dogs");

        final A out = SerializeUtil.deserialize(
                SerializeUtil.serialize(in),
                A.class);

        assertEquals("The persistent primitive int was serialized",
                out.persistentInt, in.persistentInt);
        assertEquals("The persistent String was serialized",
                out.persistentString, in.persistentString);
    }

    @Test
    public void transientFieldsAreDefaulted()
            throws IOException, ClassNotFoundException{
        final A in = new A(
                1,
                "cats",
                2,
                "dogs");

        final A out = SerializeUtil.deserialize(
                SerializeUtil.serialize(in),
                A.class);

        assertEquals("The transient int has defaulted to 0",
                0, out.transientInt);
        assertEquals("The transient String has defaulted to null",
                null, out.transientString);
    }

    @Test
    public void staticFieldsAreIgnored()
            throws IOException, ClassNotFoundException {
        final A in = new A(
                1,
                "cats",
                2,
                "dogs");

        A.staticInt = 5;
        byte[] serialized = SerializeUtil.serialize(in);
        assertEquals("The static field is not affected by serialization",
                5, A.staticInt);

        A.staticInt = 12;
        SerializeUtil.deserialize(serialized, A.class);
        assertEquals("The static field is not affected by deserialization",
                12, A.staticInt);
    }

    private static final class B implements Serializable {
        // Object is not serializable!
        Object notSerializable;

        public B (Object object) {
            this.notSerializable = object;
        }
    }

    @Test(expected = NotSerializableException.class)
    public void nonserializableFieldsWillThrowExceptions() throws IOException {
        SerializeUtil.serialize(
                new B(new Object()));
    }
}
