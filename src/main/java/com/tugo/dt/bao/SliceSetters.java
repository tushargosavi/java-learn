package com.tugo.dt.bao;

import com.datatorrent.common.util.Slice;
import com.tugo.dt.PojoUtils;

public class SliceSetters
{
  public static class BooleanSetter extends SliceGetters.ByteArrayGetterBase implements PojoUtils.SetterBoolean<Slice>
  {
    public BooleanSetter(int start)
    {
      super(start);
    }

    public void set(Slice obj, boolean booleanVal)
    {
      obj.buffer[obj.offset + start] = booleanVal? (byte)1 : (byte)0;
    }
  }

  public static class ByteSetter extends SliceGetters.ByteArrayGetterBase implements PojoUtils.SetterByte<Slice>
  {
    public ByteSetter(int start)
    {
      super(start);
    }

    public void set(Slice obj, byte byteVal)
    {
      obj.buffer[obj.offset + start] = byteVal;
    }
  }

  public static class CharSetter extends SliceGetters.ByteArrayGetterBase implements PojoUtils.SetterChar<Slice>
  {
    public void set(Slice obj, char charVal)
    {
      ByteArrayHelper.putChar(obj.buffer, obj.offset + start, charVal);
    }

    public CharSetter(int start)
    {
      super(start);
    }
  }

  public static class ShortSetter extends SliceGetters.ByteArrayGetterBase implements PojoUtils.SetterShort<Slice>
  {
    public ShortSetter(int start)
    {
      super(start);
    }

    public void set(Slice obj, short shortVal)
    {
      ByteArrayHelper.putShort(obj.buffer, obj.offset + start, shortVal);
    }
  }

  public static class IntSetter extends SliceGetters.ByteArrayGetterBase implements PojoUtils.SetterInt<Slice>
  {
    public IntSetter(int start)
    {
      super(start);
    }

    public void set(Slice obj, int intVal)
    {
      ByteArrayHelper.putInt(obj.buffer, obj.offset + start, intVal);
    }
  }

  public static class LongSetter extends SliceGetters.ByteArrayGetterBase implements PojoUtils.SetterLong<Slice>
  {
    public LongSetter(int start)
    {
      super(start);
    }

    public void set(Slice obj, long longVal)
    {
      ByteArrayHelper.putLong(obj.buffer, obj.offset + start, longVal);
    }
  }

  public static class FloatSetter extends SliceGetters.ByteArrayGetterBase implements PojoUtils.SetterFloat<Slice>
  {
    public void set(Slice obj, float floatVal)
    {
      ByteArrayHelper.putFloat(obj.buffer, obj.offset + start, floatVal);
    }

    public FloatSetter(int start)
    {
      super(start);
    }
  }

  public static class DoubleSetter extends SliceGetters.ByteArrayGetterBase implements PojoUtils.SetterDouble<Slice>
  {
    public void set(Slice obj, double doubleVal)
    {
      ByteArrayHelper.putDouble(obj.buffer, obj.offset + start, doubleVal);
    }

    public DoubleSetter(int start)
    {
      super(start);
    }
  }

  public static class StringSetter extends SliceGetters.ByteArrayGetterBase implements PojoUtils.Setter<Slice, String>
  {
    public void set(Slice obj, String str)
    {
      short idx = (short) StringTable.add(str);
      ByteArrayHelper.putShort(obj.buffer, obj.offset + start, idx);
    }

    public StringSetter(int start)
    {
      super(start);
    }
  }

  /* TODO cache getters as they can be reused */
  public static Object createSetter(DataDescriptor.Field f, int offset)
  {
    switch (f.getTypeInfo()) {
    case BOOLEAN:
      return new BooleanSetter(offset);
    case BYTE:
      return new ByteSetter(offset);
    case CHAR:
      return new CharSetter(offset);
    case SHORT:
      return new ShortSetter(offset);
    case INT:
      return new IntSetter(offset);
    case LONG:
      return new LongSetter(offset);
    case FLOAT:
      return new FloatSetter(offset);
    case DOUBLE:
      return new DoubleSetter(offset);
    case STRING:
      return new StringSetter(offset);
    default:
      return null;
    }
  }
}
