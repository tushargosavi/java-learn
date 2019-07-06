package com.tugo.dt.bao;

import com.datatorrent.common.util.Slice;
import com.google.common.base.Preconditions;
import com.tugo.dt.PojoUtils;

public class AdapterField<T> extends DataDescriptor.Field {
  Object pojoGetter;
  Object pojoSetter;
  Object baoGetter;
  Object baoSetter;

  public AdapterField(String name, TypeInfo typeInfo)
  {
    super(name, typeInfo);
  }

  public void copyFromPojoToBao(T from, Slice to)
  {
    Object getter = pojoGetter;
    Object setter = baoSetter;
    transferField(typeInfo, getter, setter, from, to);
  }

  public void copyFromBaoToPojo(Slice from, T to)
  {
    Object getter = baoGetter;
    Object setter = pojoSetter;
    transferField(typeInfo, getter, setter, from, to);
  }

  private void transferField(TypeInfo ti, Object getter, Object setter, Object from, Object to)
  {
    Preconditions.checkNotNull(ti);
    Preconditions.checkNotNull(getter);
    Preconditions.checkNotNull(setter);
    Preconditions.checkNotNull(from);
    Preconditions.checkNotNull(to);

    switch (ti) {
    case BOOLEAN:
      boolean b = ((PojoUtils.GetterBoolean)getter).get(from);
      ((PojoUtils.SetterBoolean)setter).set(to, b);
      break;
    case BYTE:
      byte v = ((PojoUtils.GetterByte)getter).get(from);
      ((PojoUtils.SetterByte)setter).set(to, v);
      break;
    case CHAR:
      char c = ((PojoUtils.GetterChar)getter).get(from);
      ((PojoUtils.SetterChar)setter).set(to, c);
      break;
    case SHORT:
      short s = ((PojoUtils.GetterShort)getter).get(from);
      ((PojoUtils.SetterShort)setter).set(to, s);
      break;
    case INT:
      int i = ((PojoUtils.GetterInt)getter).get(from);
      ((PojoUtils.SetterInt)setter).set(to, i);
      break;
    case LONG:
      long l = ((PojoUtils.GetterLong)getter).get(from);
      ((PojoUtils.SetterLong)setter).set(to, l);
      break;
    case FLOAT:
      float f = ((PojoUtils.GetterFloat)getter).get(from);
      ((PojoUtils.SetterFloat)setter).set(to, f);
      break;
    case DOUBLE:
      double d = ((PojoUtils.GetterDouble)getter).get(from);
      ((PojoUtils.SetterDouble)setter).set(to, d);
      break;
    default:
      Object o = ((PojoUtils.Getter)getter).get(from);
      ((PojoUtils.Setter)setter).set(to, o);
      break;
    }
  }
}
