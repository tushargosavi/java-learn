package com.tugo.learn.avro;

import java.util.List;

import org.apache.avro.Schema;

public class AvroUtils
{
  public static Schema getNonNullSchema(Schema schema, String fieldName)
  {
    Schema.Field field = schema.getField(fieldName);
    if (field == null) return null;
    if (field.schema().getType() == Schema.Type.UNION) {
      List<Schema> types = field.schema().getTypes();
      if (types.size() > 2) {
        throw new RuntimeException("Current does not support union with multiple types");
      }
      if (!(types.get(0).getType() == Schema.Type.NULL || types.get(1).getType() == Schema.Type.NULL)) {
        throw new RuntimeException("Does not support not null union");
      }
      Schema elementSchema = (types.get(0).getType() != Schema.Type.NULL)? types.get(0) : types.get(1);
      return elementSchema;
    } else {
      return field.schema();
    }
  }

  public static Schema getSchema(Schema input, String chain)
  {
    if (chain.contains(".")) {
      String fieldName = chain.split("\\.")[0];
      return getSchema(getNonNullSchema(input, fieldName), chain.substring(chain.indexOf(".") + 1));
    } else {
      return getNonNullSchema(input, chain);
    }
  }

}
