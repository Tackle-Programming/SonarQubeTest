package com.github.tomboyo.serializationStudy;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Listing 3: The `writeObject` and `readObject` methods control serial format
 *
 * (See Effective Java 2nd Ed. item 75, from which this listing draws heavily.)
 *
 * By implementing the writeObject and readObject methods in a class we may
 * control the precise form of a class' serialized data. This is typically
 * necessary because the default serialization mechanism captures the
 * implementation details of the serialized class (e.g. it captures the
 * pointers in a linked list or the array in a sequentially allocated list). A
 * class utilizing default serialization, once modified, may lose compatibility
 * with previous versions of itself.
 *
 * When we implement read/writeObject, our intent is to capture the logical
 * form of the class only, eliding implementation details whenever possible.
 * This gives us the flexibility to modify the internals of our classes as we
 * please.
 */
public class Listing3 {

    /**
     * A rudimentary singly-linked list of Strings. It is a circular list
     * whose beginning and end is marked by the sentinal Node "head".
     */
    private static final class List implements Serializable {

        private final class Node {
            private final String datum;
            private Node next;

            private Node(String datum) {
                this.datum = datum;
            }
        }

        transient private int size;
        transient private Node head;

        public List() {
            initialize();
        }

        // TODO See listing X
        private void initialize() {
            head = new Node(null);
            head.next = head;
            size = 0;
        }

        public void add(String datum) {
            Node node = new Node(datum);
            node.next = head.next;
            head.next = node;
            size += 1;
        }

        public boolean contains(String datum) {
            Node current = head;

            while ((current = current.next) != head)
                if (current.datum.equals(datum))
                    return true;

            return false;
        }

        /**
         * This lets us specify the serialized representation of the object.
         * Following Effective Java's example, we will output the number of
         * Strings held by this list followed by the strings themselves. When
         * we read the data back in later, we will be responsible for
         * re-creating the Nodes and pointers between them.
         */
        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeInt(size);

            Node current = head.next;
            while (current != head) {
                out.writeObject(current.datum);
                current = current.next;
            }
        }

        /**
         * Here we read the data serialized by writeObject back into an
         * instance of List. Note that a List has already been allocated and
         * our context is that list, so we simply manipulate member data to
         * reconstitute the object stream.
         */
        private void readObject(ObjectInputStream in)
                throws IOException, ClassNotFoundException {
            initialize(); // TODO see listing X
            int numberOfElements = in.readInt();
            for (int i = 0; i < numberOfElements; i++) {
                add((String) in.readObject());
            }
        }
    }

    @RunWith(Enclosed.class)
    public static final class ListTest {

        public static final class ListSerializationTests {

            @Test
            public void canBeSerialized() throws Exception {
                List in = new List();
                in.add("a");
                in.add("b");

                List out = SerializeUtil.deserialize(
                        SerializeUtil.serialize(in),
                        List.class);

                assertEquals(2, out.size);
                assertTrue(out.contains("a"));
                assertTrue(out.contains("b"));
            }
        }

        public static final class ListBehaviorTests {

            @Test
            public void testContainsMissingElement() {
                assertFalse(new List().contains("cats"));
            }

            @Test
            public void testAddAndGet() {
                List list = new List();
                list.add("a");
                list.add("b");
                list.add("c");

                assertTrue(list.contains("a"));
                assertTrue(list.contains("b"));
                assertTrue(list.contains("c"));
            }

            @Test
            public void testSizeOfList() {
                List list = new List();
                assertEquals(0, list.size);

                list.add("");
                assertEquals(1, list.size);

                list.add("");
                assertEquals(2, list.size);
            }
        }
    }
}
