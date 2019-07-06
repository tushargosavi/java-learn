package com.tugo.learn.avro;

import java.util.List;

import org.json.JSONObject;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

public class JsonToGenericRecordHelper
{
  private final Schema schema;

  public JsonToGenericRecordHelper(Schema schema)
  {
    this.schema = schema;
  }

  public GenericRecord convert(JSONObject o)
  {
    return convertRecord(schema, o);
  }

  public Object convert(Schema s, Object obj)
  {
    switch (s.getType()) {
      case INT:
        return ((Number)obj).intValue();
      case LONG:
        return ((Number)obj).longValue();
      case FLOAT:
        return ((Number)obj).floatValue();
      case DOUBLE:
        return ((Number)obj).floatValue();
      case STRING:
        if (obj instanceof String) return obj;
        return obj.toString();
      case RECORD:
        GenericRecord r = convertRecord(s, obj);
        return r;
      case UNION:
        return convertUnion(s, obj);
      case ARRAY:
        return convertList(s, obj);
      default:
        return null;
    }
  }

  private Object convertUnion(Schema s, Object o)
  {
    assert (s.getType() == Schema.Type.UNION);
    List<Schema> types = s.getTypes();
    if (types.size() > 2) {
      throw new RuntimeException("Current does not support union with multiple types");
    }
    if (!(types.get(0).getType() == Schema.Type.NULL || types.get(1).getType() == Schema.Type.NULL)) {
      throw new RuntimeException("Does not support not null union");
    }
    if (o == null) return null;
    Schema elementSchema = (types.get(0).getType() != Schema.Type.NULL)? types.get(0) : types.get(1);
    // when some consumer return empty object for record, and union has support for null type, then
    // return null for empty object.
    if (elementSchema.getType() == Schema.Type.RECORD && (o instanceof JSONObject) && ((JSONObject)o).length() == 0) {
      return null;
    }
    return convert(elementSchema, o);
  }

  private GenericRecord convertRecord(Schema schema, Object obj)
  {
    // schema should be of record type
    // obj should be JSONObject
    assert (schema.getType() == Schema.Type.RECORD);
    assert (obj instanceof JSONObject);

    // In case of empty object, return null

    JSONObject jobj = (JSONObject)obj;
    //if (jobj.length() == 0) return null;
    GenericRecord record = new GenericData.Record(schema);
    for (Schema.Field f : schema.getFields()) {
      if (jobj.has(f.name())) {
        record.put(f.name(), convert(f.schema(), jobj.get(f.name())));
      }
    }
    return record;
  }

  private List<GenericRecord> convertList(Schema s, Object o)
  {
    return null;
  }
}