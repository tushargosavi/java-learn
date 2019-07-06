package com.tugo.dt.bao;

import com.google.common.collect.Lists;
import com.tugo.dt.PojoUtils;

import java.util.List;
import java.util.Map;

public class MapGetters
{
  static class BaseMapGetter {
    Object key;

    public BaseMapGetter(Object key) {
      this.key = key;
    }
  }

  static class MapGetter extends BaseMapGetter implements PojoUtils.Getter<Map, Object>
  {
    public Object get(Map map)
    {
      return map.get(key);
    }

    public MapGetter(Object key)
    {
      super(key);
    }
  }

  static class MapGetterBoolean extends BaseMapGetter implements PojoUtils.GetterBoolean<Map>
  {
    public boolean get(Map obj)
    {
      return (Boolean)obj.get(key);
    }

    public MapGetterBoolean(Object key)
    {
      super(key);
    }
  }

  static class MapGetterByte extends BaseMapGetter implements PojoUtils.GetterByte<Map>
  {
    public byte get(Map obj)
    {
      return (Byte)obj.get(key);
    }

    public MapGetterByte(Object key)
    {
      super(key);
    }
  }

  static class MapGetterShort extends BaseMapGetter implements PojoUtils.GetterShort<Map>
  {
    public short get(Map obj)
    {
      return (Short)obj.get(key);
    }

    public MapGetterShort(Object key)
    {
      super(key);
    }
  }

  static class MapGetterInt extends BaseMapGetter implements PojoUtils.GetterInt<Map>
  {
    public MapGetterInt(Object key)
    {
      super(key);
    }

    public int get(Map obj)
    {
      return (Integer)obj.get(key);
    }
  }

  static class MapGetterLong extends BaseMapGetter implements PojoUtils.GetterLong<Map>
  {
    public long get(Map obj)
    {
      return (Long)obj.get(key);
    }

    public MapGetterLong(Object key)
    {
      super(key);
    }
  }

  static class MapGetterFloat extends BaseMapGetter implements PojoUtils.GetterFloat<Map> {
    public float get(Map obj)
    {
      return (Float)obj.get(key);
    }

    public MapGetterFloat(Object key)
    {
      super(key);
    }
  }

  static class MapGetterDouble extends BaseMapGetter implements PojoUtils.GetterDouble<Map>
  {
    public double get(Map obj)
    {
      return (Double)obj.get(key);
    }

    public MapGetterDouble(Object key)
    {
      super(key);
    }
  }

  public static DataDescriptor.FieldList createGetterSetters(DataDescriptor.FieldList flist)
  {
    List<DataDescriptor.Field> lst = Lists.newArrayList();

    for(DataDescriptor.Field f : flist.fields) {
      DataDescriptor.FieldWithGetterSetter nf = new DataDescriptor.FieldWithGetterSetter(f);
      nf.setter = createObjectSetter(f);
      nf.getter = createObjectGetter(f);
      lst.add(nf);
    }
    return new DataDescriptor.FieldList(lst);
  }

  /**
   * Generic setters for everything.
   * @param f
   * @return
   */
  public static Object createObjectSetter(DataDescriptor.Field f)
  {
    return new PojoUtils.Setter<Map, Object>() {
      private Object key;
      public void set(Map obj, Object value)
      {
        obj.put(key, value);
      }
      private Object init(Object key) {
        this.key = key;
        return this;
      }
    }.init(f.name);
  }

  /**
   * Generic setters for everything.
   * @param f
   * @return
   */
  public static Object createObjectGetter(DataDescriptor.Field f)
  {
    return new PojoUtils.Getter<Map, Object>()
    {
      public Object get(Map obj)
      {
        return obj.get(key);
      }

      private Object key;

      private Object init(Object key)
      {
        this.key = key;
        return this;
      }
    }.init(f.name);
  }


  public static Object createGetter(DataDescriptor.Field f) {
    switch (f.typeInfo) {
    case BOOLEAN:
      return new MapGetterBoolean(f.name);
    case BYTE:
      return new MapGetterByte(f.name);
    case SHORT:
      return new MapGetterShort(f.name);
    case INT:
      return new MapGetterInt(f.name);
    case LONG:
      return new MapGetterLong(f.name);
    case FLOAT:
      return new MapGetterFloat(f.name);
    case DOUBLE:
      return new MapGetterDouble(f.name);
    case STRING:
      return new MapGetter(f.name);
    default:
      return new MapGetter(f.name);
    }
  }
}
