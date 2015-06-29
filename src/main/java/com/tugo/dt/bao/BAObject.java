package com.tugo.dt.bao;

import com.datatorrent.common.util.Slice;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A Byte Array Object.
 */
public class BAObject
{
  static class BAOField extends DataDescriptor.Field
  {
    int offset;
    Object getter;
    Object setter;
  }

  DataDescriptor.FieldList flist;
  List<BAOField> baoFieldList;

  int size = 0;
  Object[] getters;
  Object[] setters;

  Map<String, Object> getterMap = Maps.newHashMap();
  Map<String, Object> setterMap = Maps.newHashMap();

  public BAObject(DataDescriptor.FieldList list)
  {
    flist = list;
    initialize();
  }

  private void initialize()
  {
    int offset = 0;
    setters = new Object[flist.size()];
    getters = new Object[flist.size()];
    baoFieldList = new ArrayList<BAOField>();
    int i = 0;
    for(DataDescriptor.Field f : flist.fields) {
      BAOField bf = new BAOField();
      bf.offset = offset;
      bf.name = f.name;
      bf.type = f.type;
      bf.getter = ByteArrayGetters.createGetter(f, offset);
      bf.setter = ByteArraySetters.createSetter(f, offset);
      getters[i] = bf.getter;
      setters[i] = bf.setter;
      getterMap.put(f.name, bf.getter);
      setterMap.put(f.name, bf.setter);
      i++;
      offset += TypeInfo.getSize(f.type);
      baoFieldList.add(bf);
    }
    size = offset;
  }

  public Slice getNewObject() {
    byte[] arr = new byte[size];
    return new Slice(arr);
  }

  Object[] getGetters() {
    return getters;
  }

  Object getGetter(String name) {
    return getterMap.get(name);
  }

  Object[] getSetters() {
    return setters;
  }

  Object getSetter(String name) {
    return setterMap.get(name);
  }
}
