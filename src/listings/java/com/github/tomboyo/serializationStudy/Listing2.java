package com.github.tomboyo.serializationStudy;

import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.assertTrue;

/**
 * Listing 2. Serialization of member data
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
            throws Exception {

        final A in = new A(
                1,
                "cats",
                2,
                "dogs");

        final A out = SerializeUtil.deserialize(
                SerializeUtil.serialize(in),
                A.class);

        assertTrue("The persistent primitive int was serialized",
                out.persistentInt == in.persistentInt);
        assertTrue("The persistent String was serialized",
                out.persistentString.equals(in.persistentString));
    }

    @Test
    public void transientFieldsAreDefaulted() throws Exception {
        final A in = new A(
                1,
                "cats",
                2,
                "dogs");

        final A out = SerializeUtil.deserialize(
                SerializeUtil.serialize(in),
                A.class);

        assertTrue("The transient int has defaulted to 0",
                out.transientInt == 0);
        assertTrue("The transient String has defaulted to null",
                out.transientString == null);
    }

    @Test
    public void staticFieldsAreIgnored() throws Exception {
        final A in = new A(
                1,
                "cats",
                2,
                "dogs");

        A.staticInt = 5;
        byte[] serialized = SerializeUtil.serialize(in);
        assertTrue("The static field is not affected by serialization",
                A.staticInt == 5);

        A.staticInt = 12;
        SerializeUtil.deserialize(serialized, A.class);
        assertTrue("The static field is not affected by deserialization",
                A.staticInt == 12);
    }
}
