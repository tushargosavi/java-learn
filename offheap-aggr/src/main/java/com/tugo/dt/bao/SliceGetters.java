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

  public static class StringGetter extends ByteArrayGetterBase implements PojoUtils.Getter<Slice, String>
  {
    public String get(Slice obj)
    {
      int idx = ByteArrayHelper.getShort(obj.buffer, obj.offset + start);
      String str = StringTable.get(idx);
      return str;
    }

    public StringGetter(int start)
    {
      super(start);
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
    case STRING:
      return new StringGetter(offset);
    default:
      throw new UnsupportedOperationException("Can't get request operation");
    }
  }

  /**
   * Create a generic getter, which usage objects as return value instead of primitives.
   * @param f
   * @param offset
   * @return
   */
  public static PojoUtils.Getter<Slice, Object> createObjectGetter(DataDescriptor.Field f, int offset) {
    return new PojoUtils.Getter<Slice, Object>()
    {
      private int offset;
      private DataDescriptor.Field f;
      public Object get(Slice s)
      {
        Object obj = null;
        switch(f.getTypeInfo()) {
        case BOOLEAN:
          obj = ByteArrayHelper.getBoolean(s.buffer, s.offset + offset);
          break;
        case BYTE:
          obj = s.buffer[s.offset + offset];
          break;
        case CHAR:
          obj = ByteArrayHelper.getChar(s.buffer, s.offset + offset);
          break;
        case SHORT:
          obj = ByteArrayHelper.getShort(s.buffer, s.offset + offset);
          break;
        case INT:
          obj = ByteArrayHelper.getInt(s.buffer, s.offset + offset);
          break;
        case LONG:
          obj = ByteArrayHelper.getLong(s.buffer, s.offset + offset);
          break;
        case FLOAT:
          obj = ByteArrayHelper.getFloat(s.buffer, s.offset + offset);
          break;
        case DOUBLE:
          obj = ByteArrayHelper.getDouble(s.buffer, s.offset + offset);
          break;
        default:
          obj = null;
        }
        return obj;
      }

      private PojoUtils.Getter<Slice, Object> init(DataDescriptor.Field f, int offset) {
        this.f = f;
        this.offset = offset;
        return this;
      }
    }.init(f, offset);
  }
}
