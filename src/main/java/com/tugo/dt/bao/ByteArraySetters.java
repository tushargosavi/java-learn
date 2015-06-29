package com.tugo.dt.bao;

import com.datatorrent.common.util.Slice;
import com.tugo.dt.PojoUtils;

public class ByteArraySetters
{
  public static class BooleanSetter extends ByteArrayGetters.ByteArrayGetterBase implements PojoUtils.SetterBoolean<Slice>
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

  public static class ByteSetter extends ByteArrayGetters.ByteArrayGetterBase implements PojoUtils.SetterByte<Slice>
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

  public static class CharSetter extends ByteArrayGetters.ByteArrayGetterBase implements PojoUtils.SetterChar<Slice>
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

  public static class ShortSetter extends ByteArrayGetters.ByteArrayGetterBase implements PojoUtils.SetterShort<Slice>
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

  public static class IntSetter extends ByteArrayGetters.ByteArrayGetterBase implements PojoUtils.SetterInt<Slice>
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

  public static class LongSetter extends ByteArrayGetters.ByteArrayGetterBase implements PojoUtils.SetterLong<Slice>
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

  public static class FloatSetter extends ByteArrayGetters.ByteArrayGetterBase implements PojoUtils.SetterFloat<Slice>
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

  public static class DoubleSetter extends ByteArrayGetters.ByteArrayGetterBase implements PojoUtils.SetterDouble<Slice>
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

  /* TODO cache getters as they can be reused */
  public static Object createSetter(DataDescriptor.Field f, int offset) {
    if (f.type.equals("bool"))
      return new BooleanSetter(offset);
    if (f.type.equals("byte"))
      return new ByteSetter(offset);
    if (f.type.equals("char"))
      return new CharSetter(offset);
    if (f.type.equals("short"))
      return new ShortSetter(offset);
    if (f.type.equals("int"))
      return new IntSetter(offset);
    if (f.type.equals("long"))
      return new LongSetter(offset);
    if (f.type.equals("float"))
      return new FloatSetter(offset);
    if (f.type.equals("double"))
      return new DoubleSetter(offset);
    return null;
  }
}
