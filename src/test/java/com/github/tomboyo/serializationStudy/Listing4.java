package com.github.tomboyo.serializationStudy;

import static org.junit.Assert.assertArrayEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Listing 4: Demonstrate serialization of data to a file.
 * 
 * In this listing we serialize an array of longs and stream the output to a
 * temporary file. We then read the data back into memory and confirm the
 * deserialized data matches the serialized data.
 * 
 * A curious reader could redirect the serialized output to a permanent file in
 * their /tmp directory and view the data using a hex editor such as hexedit.
 * This will reveal the regular format that serialized data takes. In partuclar
 * this technique can be useful to identify causes of corrupt serialized data.
 */
public final class Listing4 {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void serializeAndDeserializeWithFile() throws Exception {
    long[] longs = new long[10];
    File file = folder.newFile();
    
    ObjectOutputStream out = new ObjectOutputStream(
      new FileOutputStream(file));
    out.writeObject(longs);
    out.flush();
    out.close();

    ObjectInputStream in = new ObjectInputStream(
      new FileInputStream(file));
    long[] deserialized = (long[]) in.readObject();
    in.close();

    assertArrayEquals(longs, deserialized);
  }
}
