package com.tugo.learn.avro;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.specific.SpecificRecordBase;

public class GenericToSpecificRecordConverter
{
  static void copy(String name, GenericRecord gr, SpecificRecordBase sr)
  {
    sr.put(name, gr.get(name));
  }

  // A null Union is a type with possible null value.
  static boolean isNullUnionSchema(Schema s)
  {
    return s.getType() == Schema.Type.UNION &&
      s.getTypes().size() == 2 &&
      s.getTypes().get(0).getType() == Schema.Type.NULL;
  }

  // get non null type in a union
  static Schema getNonNullUnionType(Schema s)
  {
    return s.getTypes().get(0).getType() != Schema.Type.NULL ?
      s.getTypes().get(0) : s.getTypes().get(1);
  }

  public static void convertRecord(GenericRecord gr, SpecificRecordBase sr)
  {
    if (gr == null || sr == null) return;
    for(Schema.Field f : gr.getSchema().getFields()) {
      String name = f.name();
      System.out.println("copying field " + name);
      switch (f.schema().getType()) {
        case INT:
        case LONG:
        case FLOAT:
        case DOUBLE:
        case STRING:
          copy(name, gr, sr);
          break;
        case RECORD:
          convertRecord((GenericRecord)gr.get(name), (SpecificRecordBase)sr.get(name));
          break;
        case UNION:
          if (isNullUnionSchema(f.schema())) {
            if (gr.get(name) == null)
              sr.put(name, null);
            else {
              Schema s = getNonNullUnionType(f.schema());
              if (s.getType() == Schema.Type.RECORD) {
                convertRecord((GenericRecord)gr.get(name), (SpecificRecordBase)sr.get(name));
              } else {
                copy(name, gr, sr);
              }
            }
          }
          default:
      }
    }
  }
}
