package com.tugo.learn;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

public class Test
{
  public static void main(String[] args)
  {
    String s = "I was here\n";
    byte[] data = s.getBytes();
    ByteBuffer out = ByteBuffer.wrap(data);
    ByteBuffer copy = ByteBuffer.allocate(12);
    Path file = Paths.get("/tmp/test1");

    try (FileChannel fc = (FileChannel.open(file, READ, WRITE))) {
      int nread;
      do {
        nread = fc.read(copy);
      } while (nread != -1 && copy.hasRemaining());


      fc.position(0);
      while (out.hasRemaining()) {
        fc.write(out);
      }
      out.rewind();
      // Move to the end of the file.  Copy the first 12 bytes to
      // the end of the file.  Then write "I was here!" again.
      long length = fc.size();
      System.out.println("length of the file is " + length);
      fc.position(length-1);
      copy.flip();
      while (copy.hasRemaining())
        fc.write(copy);
      while (out.hasRemaining())
        fc.write(out);


    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
