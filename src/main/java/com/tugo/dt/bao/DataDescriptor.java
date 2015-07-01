package com.tugo.dt.bao;

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataDescriptor
{

  public static class Field
  {
    public final String name;
    public final TypeInfo typeInfo;

    public Field(String name, String type)
    {
      this.name = name;
      this.typeInfo = TypeInfo.getTypeInfo(type);
    }

    public Field(String name, TypeInfo ti) {
      this.name = name;
      this.typeInfo = ti;
    }

    public TypeInfo getTypeInfo()
    {
      return typeInfo;
    }
  }

  public static class FieldWithGetterSetter extends Field
  {
    Object getter = null;
    Object setter = null;

    public FieldWithGetterSetter(String name, String type, Object getter, Object setter)
    {
      super(name, type);
      this.getter = getter;
      this.setter = setter;
    }

    public Object getGetter()
    {
      return getter;
    }

    public void setGetter(Object getter)
    {
      this.getter = getter;
    }

    public Object getSetter()
    {
      return setter;
    }

    public void setSetter(Object setter)
    {
      this.setter = setter;
    }
  }

  public static class FieldList {
    List<? extends Field> fields;
    private final Map<String, Field> fieldMap = Maps.newHashMap();

    public FieldList(List<? extends Field> fields)
    {
      this.fields = fields;
      generateLookupMaps();
    }

    private void generateLookupMaps()
    {
      for(Field f : fields) {
        fieldMap.put(f.name, f);
      }
    }

    /*
    No duplicate field and type which are supported by byte array implementations.
     */
    public boolean validate()
    {
      return true;
    }

    public String getFieldType(String name)
    {
      return fieldMap.get(name).getTypeInfo().getName();
    }

    public FieldList getSubSet(Collection<String> fieldNames)
    {
      List<Field> subFields = new ArrayList<Field>();
      for(String fieldName : fieldNames) {
        subFields.add(fieldMap.get(fieldName));
      }
      return new FieldList(subFields);
    }

    public int size()
    {
      return fields.size();
    }

    public boolean contains(String name)
    {
      return fieldMap.containsKey(name);
    }

    public Field getField(String name) {
      return fieldMap.get(name);
    }

  }

}
