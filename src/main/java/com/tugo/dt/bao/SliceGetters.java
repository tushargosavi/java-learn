package com.tugo.dt.bao;

import com.datatorrent.common.util.Slice;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.tugo.dt.PojoUtils;
import org.omg.PortableServer.POA;

public class SliceGetters
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
    switch(f.typeInfo) {
    case BOOLEAN:
      return new BooleanGetter(offset);
    case BYTE:
      return new ByteGetter(offset);
    case CHAR:
      return new CharGetter(offset);
    case SHORT:
       return new ShortGetter(offset);
    case INT:
        return new IntGetter(offset);
    case LONG:
        return new LongGeeter(offset);
    case FLOAT:
        return new FloatGetter(offset);
    case DOUBLE:
        return new DoubleGetter(offset);
    default:
      return null;
    }
  }
}
