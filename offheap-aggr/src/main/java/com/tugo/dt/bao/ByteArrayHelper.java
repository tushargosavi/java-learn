package com.tugo.dt.bao;

import java.nio.ByteBuffer;

public class ByteArrayHelper
{

  public static final boolean getBoolean(byte[] memory, int index) {
    return memory[index] != 0;
  }

  public final void putBoolean(byte[] memory, int index, boolean value) {
    memory[index] = (byte) (value ? 1 : 0);
  }

  public static final char getChar(byte[] memory, int index) {
    return (char) ( ((memory[index    ] & 0xff) << 8) |
        (memory[index + 1] & 0xff) );
  }

  public static final void putChar(byte[] memory, int index, char value) {
    memory[index    ] = (byte) (value >> 8);
    memory[index + 1] = (byte) value;
  }

  public static final short getShort(byte[] memory, int index) {
    return (short) (
        ((memory[index    ] & 0xff) << 8) |
            ((memory[index + 1] & 0xff)) );
  }

  public static final void putShort(byte[] memory, int index, short value) {
    memory[index    ] = (byte) (value >> 8);
    memory[index + 1] = (byte) value;
  }

  public static final int getInt(byte[] memory, int index) {
    return ((memory[index    ] & 0xff) << 24)
        | ((memory[index + 1] & 0xff) << 16)
        | ((memory[index + 2] & 0xff) <<  8)
        | ((memory[index + 3] & 0xff)     );
  }

  public static final int getIntLittleEndian(byte[] memory, int index) {
    return ((memory[index    ] & 0xff)      )
        | ((memory[index + 1] & 0xff) <<  8)
        | ((memory[index + 2] & 0xff) << 16)
        | ((memory[index + 3] & 0xff) << 24);
  }

  public static final int getIntBigEndian(byte[] memory, int index) {
    return ((memory[index    ] & 0xff) << 24)
        | ((memory[index + 1] & 0xff) << 16)
        | ((memory[index + 2] & 0xff) <<  8)
        | ((memory[index + 3] & 0xff)      );
  }

  public static final void putInt(byte[] memory, int index, int value) {
    memory[index    ] = (byte) (value >> 24);
    memory[index + 1] = (byte) (value >> 16);
    memory[index + 2] = (byte) (value >> 8);
    memory[index + 3] = (byte) value;
  }

  public static final void putIntLittleEndian(byte[] memory, int index, int value) {
    memory[index    ] = (byte) value;
    memory[index + 1] = (byte) (value >>  8);
    memory[index + 2] = (byte) (value >> 16);
    memory[index + 3] = (byte) (value >> 24);
  }

  public static final void putIntBigEndian(byte[] memory, int index, int value) {
    memory[index    ] = (byte) (value >> 24);
    memory[index + 1] = (byte) (value >> 16);
    memory[index + 2] = (byte) (value >> 8);
    memory[index + 3] = (byte) value;
  }

  public static final long getLong(byte[] memory, int index) {
    return (((long) memory[index    ] & 0xff) << 56)
        | (((long) memory[index + 1] & 0xff) << 48)
        | (((long) memory[index + 2] & 0xff) << 40)
        | (((long) memory[index + 3] & 0xff) << 32)
        | (((long) memory[index + 4] & 0xff) << 24)
        | (((long) memory[index + 5] & 0xff) << 16)
        | (((long) memory[index + 6] & 0xff) <<  8)
        | (((long) memory[index + 7] & 0xff)      );
  }
  public static final long getLongLittleEndian(byte[] memory, int index) {
    return (((long) memory[index    ] & 0xff)      )
        | (((long) memory[index + 1] & 0xff) <<  8)
        | (((long) memory[index + 2] & 0xff) << 16)
        | (((long) memory[index + 3] & 0xff) << 24)
        | (((long) memory[index + 4] & 0xff) << 32)
        | (((long) memory[index + 5] & 0xff) << 40)
        | (((long) memory[index + 6] & 0xff) << 48)
        | (((long) memory[index + 7] & 0xff) << 56);
  }

  public static final long getLongBigEndian(byte[] memory, int index) {
    return (((long) memory[index    ] & 0xff) << 56)
        | (((long) memory[index + 1] & 0xff) << 48)
        | (((long) memory[index + 2] & 0xff) << 40)
        | (((long) memory[index + 3] & 0xff) << 32)
        | (((long) memory[index + 4] & 0xff) << 24)
        | (((long) memory[index + 5] & 0xff) << 16)
        | (((long) memory[index + 6] & 0xff) <<  8)
        | (((long) memory[index + 7] & 0xff)      );
  }

  public static final void putLong(byte[] memory, int index, long value) {
    memory[index    ] = (byte) (value >> 56);
    memory[index + 1] = (byte) (value >> 48);
    memory[index + 2] = (byte) (value >> 40);
    memory[index + 3] = (byte) (value >> 32);
    memory[index + 4] = (byte) (value >> 24);
    memory[index + 5] = (byte) (value >> 16);
    memory[index + 6] = (byte) (value >>  8);
    memory[index + 7] = (byte)  value;
  }

  public static final void putLongLittleEndian(byte[] memory, int index, long value) {
    memory[index    ] = (byte)  value;
    memory[index + 1] = (byte) (value >>  8);
    memory[index + 2] = (byte) (value >> 16);
    memory[index + 3] = (byte) (value >> 24);
    memory[index + 4] = (byte) (value >> 32);
    memory[index + 5] = (byte) (value >> 40);
    memory[index + 6] = (byte) (value >> 48);
    memory[index + 7] = (byte) (value >> 56);
  }

  public static final void putLongBigEndian(byte[] memory, int index, long value) {
    memory[index    ] = (byte) (value >> 56);
    memory[index + 1] = (byte) (value >> 48);
    memory[index + 2] = (byte) (value >> 40);
    memory[index + 3] = (byte) (value >> 32);
    memory[index + 4] = (byte) (value >> 24);
    memory[index + 5] = (byte) (value >> 16);
    memory[index + 6] = (byte) (value >>  8);
    memory[index + 7] = (byte)  value;
  }

  /* Implement serialization of float
  * TODO get rid of bytebuffer creation */
  public static float getFloat(byte[] memory, int index)
  {
    ByteBuffer bb = ByteBuffer.wrap(memory, index, 4);
    float v = bb.getFloat();
    return v;
  }

  public static void putFloat(byte[] memory, int index, float value)
  {
    ByteBuffer bb = ByteBuffer.wrap(memory, index, 4);
    bb.putFloat(value);
  }

  /* Getter and setters for double */
  public static double getDouble(byte[] memory, int index)
  {
    ByteBuffer bb = ByteBuffer.wrap(memory, index, 8);
    return bb.getDouble();
  }

  public static void putDouble(byte[] memory, int index, double d)
  {
    ByteBuffer bb = ByteBuffer.wrap(memory, index, 8);
    bb.putDouble(d);
  }
}
