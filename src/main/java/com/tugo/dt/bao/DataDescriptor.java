package com.tugo.dt.bao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataDescriptor
{

  public static class Field
  {
    public String name;
    public String type;

    public Field() { }
    public Field(String name, String type)
    {
      this.name = name;
      this.type = type;
    }
  }

  public static class FieldList {
    List<Field> fields;
    private Map<String, Field> fieldMap;
    private Map<String, String> fieldToTypeMap;

    public FieldList(List<Field> fields)
    {
      this.fields = fields;
    }

    /*
    No duplicate field and type which are supported by bytearray implementations.
     */
    public boolean validate()
    {
      return true;
    }

    public Field getField(String name) {
      return fieldMap.get(name);
    }

    public String getFieldType(String name) {
      return fieldToTypeMap.get(name);
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
  }

}
