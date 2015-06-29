package com.tugo.dt.bao;

import com.datatorrent.common.util.Slice;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.tugo.dt.PojoUtils;
import org.omg.PortableServer.POA;

public class ByteArrayGetters
{
  public static class ByteArrayGetterBase
  {
    int start;

    ByteArrayGetterBase(int start) {
      this.start = start;
    }
  }

  public static class BooleanGetter extends ByteArrayGetterBase implements PojoUtils.GetterBoolean<Slice>
  {
    public BooleanGetter(int start)
    {
      super(start);
    }

    public boolean get(Slice obj)
    {
      return (obj.buffer[obj.offset + start] != 0);
    }
  }

  public static class ByteGetter extends ByteArrayGetterBase implements PojoUtils.GetterByte<Slice>
  {
    public ByteGetter(int start)
    {
      super(start);
    }

    public byte get(Slice obj)
    {
      return obj.buffer[obj.offset + start];
    }
  }

  public static class CharGetter extends ByteArrayGetterBase implements PojoUtils.GetterChar<Slice>
  {
    public CharGetter(int start)
    {
      super(start);
    }

    public char get(Slice obj)
    {
      return ByteArrayHelper.getChar(obj.buffer, obj.offset + start);
    }
  }

  public static class ShortGetter extends ByteArrayGetterBase implements PojoUtils.GetterShort<Slice>
  {
    public ShortGetter(int start)
    {
      super(start);
    }

    public short get(Slice obj)
    {
      return ByteArrayHelper.getShort(obj.buffer, obj.offset + start);
    }
  }

  public static class IntGetter extends ByteArrayGetterBase implements PojoUtils.GetterInt<Slice>
  {
    public IntGetter(int start)
    {
      super(start);
    }

    public int get(Slice obj)
    {
      return ByteArrayHelper.getInt(obj.buffer, obj.offset + start);
    }
  }

  public static class LongGeeter extends ByteArrayGetterBase implements PojoUtils.GetterLong<Slice>
  {
    public LongGeeter(int start)
    {
      super(start);
    }

    public long get(Slice obj)
    {
      return ByteArrayHelper.getLong(obj.buffer, obj.offset + start);
    }
  }

  public static class FloatGetter extends ByteArrayGetterBase implements PojoUtils.GetterFloat<Slice>
  {
    public FloatGetter(int start)
    {
      super(start);
    }

    public float get(Slice obj)
    {
      return ByteArrayHelper.getFloat(obj.buffer, obj.offset + start);
    }
  }

  public static class DoubleGetter extends ByteArrayGetterBase implements PojoUtils.GetterDouble<Slice>
  {
    public DoubleGetter(int start)
    {
      super(start);
    }

    public double get(Slice obj)
    {
      return ByteArrayHelper.getDouble(obj.buffer, obj.offset + start);
    }
  }

  /* TODO cache getters as they can be reused */
  public static Object createGetter(DataDescriptor.Field f, int offset) {
    if (f.type.equals("bool"))
      return new BooleanGetter(offset);
    if (f.type.equals("byte"))
      return new ByteGetter(offset);
    if (f.type.equals("char"))
      return new CharGetter(offset);
    if (f.type.equals("short"))
      return new ShortGetter(offset);
    if (f.type.equals("int"))
      return new IntGetter(offset);
    if (f.type.equals("long"))
      return new LongGeeter(offset);
    if (f.type.equals("float"))
      return new FloatGetter(offset);
    if (f.type.equals("double"))
      return new DoubleGetter(offset);
    return null;
  }
}
