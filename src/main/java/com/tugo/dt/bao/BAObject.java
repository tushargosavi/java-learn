package com.tugo.dt.bao;

import com.datatorrent.common.util.Slice;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A Byte Array Object.
 */
public class BAObject
{
  public DataDescriptor.FieldList getFieldList()
  {
    return baoFieldList;
  }

  static class BAOField extends DataDescriptor.Field
  {
    int offset;
    Object getter;
    Object setter;

    public BAOField(String name, TypeInfo typeInfo)
    {
      super(name, typeInfo);
    }
  }

  DataDescriptor.FieldList origFieldList;
  DataDescriptor.FieldList baoFieldList;
  List<BAOField> baoFields;

  int size = 0;
  Object[] getters;
  Object[] setters;

  Map<String, Object> getterMap = Maps.newHashMap();
  Map<String, Object> setterMap = Maps.newHashMap();

  public BAObject(DataDescriptor.FieldList list)
  {
    origFieldList = list;
    initialize();
  }

  private void initialize()
  {
    int offset = 0;
    setters = new Object[origFieldList.size()];
    getters = new Object[origFieldList.size()];
    baoFields = new ArrayList<BAOField>();
    int i = 0;
    for(DataDescriptor.Field f : origFieldList.fields) {
      BAOField bf = new BAOField(f.name, f.getTypeInfo());
      bf.offset = offset;
      bf.getter = SliceGetters.createGetter(f, offset);
      bf.setter = SliceSetters.createSetter(f, offset);
      getters[i] = bf.getter;
      setters[i] = bf.setter;
      getterMap.put(f.name, bf.getter);
      setterMap.put(f.name, bf.setter);
      i++;
      offset += bf.getTypeInfo().getSize();
      baoFields.add(bf);
    }
    size = offset;
    baoFieldList = new DataDescriptor.FieldList(baoFields);
  }

  public Slice getNewObject() {
    byte[] arr = new byte[size];
    return new Slice(arr);
  }

  public Object[] getGetters() {
    return getters;
  }

  public Object getGetter(String name) {
    return getterMap.get(name);
  }

  public Object[] getSetters() {
    return setters;
  }

  public Object getSetter(String name) {
    return setterMap.get(name);
  }


  public BAObject getSubView(Collection<String> fieldNames)
  {
    DataDescriptor.FieldList subFields = baoFieldList.getSubSet(fieldNames);
    return new BAObject(subFields);
  }

}
