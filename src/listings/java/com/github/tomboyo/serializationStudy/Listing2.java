package com.github.tomboyo.serializationStudy;

import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.assertTrue;

/**
 * Listing 2. Serialization of member data
 * 
 * When an object is serialized, its members are serialized too so that they
 * can be reconstituted along with their containing instance. Members which
 * can not or should not be serialized must be marked transient.
 *
 * ObjectOutputStreams will attempt to serialize any object member which is
 * not marked `transient`, regardless of any other modifiers on the members
 * (to include visibility modifiers). All of an objects members which can not
 * be serialized (do not implement Serializable) must be marked transient or
 * else NotSerializableExceptions will be raised upon any attempt to
 * serialize instances of the containing class.
 *
 * Data which is inherently volatile, such as locks and counters, should always
 * be marked transient. It makes little sense to serialize such data.
 */
public class Listing2 {

    private static final class A implements Serializable {
        private final int persistentInt;
        private final String persistentString;
        private final transient int transientInt;
        private final transient String transientString;

        public A(
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
    public void testTransientFieldsAndSerialization() throws Exception {

        final A in = new A(
                1,
                "cats",
                2,
                "dogs");

        try {
            final A out = SerializeUtil.deserialize(
                    SerializeUtil.serialize(in),
                    A.class);

            assertTrue("The persistent primitive int was serialized",
                    out.persistentInt == in.persistentInt);
            assertTrue("The persistent String was serialized",
                    out.persistentString.equals(in.persistentString));

            assertTrue("The transient int has defaulted to 0",
                    out.transientInt == 0);
            assertTrue("The transient String has defaulted to null",
                    out.transientString == null);

        } catch (Exception e) {
            // This listing is not concerned with exceptions. These are here for
            // development only.
            e.printStackTrace();
            throw e;
        }
    }
}
