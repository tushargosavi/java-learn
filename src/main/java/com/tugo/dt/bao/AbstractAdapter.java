package com.tugo.dt.bao;

import com.datatorrent.common.util.Slice;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AbstractAdapter<T>
{
  protected DataDescriptor.FieldList flist;
  protected BAObject bao;
  protected Map<String, AdapterField> nameToAdapterField = Maps.newHashMap();
  protected List<AdapterField> adapterFields = new ArrayList<AdapterField>();

  public BAObject getBao()
  {
    return bao;
  }

  public Slice getNewBaoInstance()
  {
    Slice s = bao.getNewObject();
    return s;
  }

  /**
   * Copy field from pojo from slice
   * @param name
   * @param from
   * @param to
   */
  public void copyField(String name, T from, Slice to)
  {
    AdapterField af = nameToAdapterField.get(name);
    af.copyFromPojoToBao(from, to);
  }

  /** copy field from binary object to pojo
   *
   * @param name
   * @param from
   * @param to
   */
  public void copyField(String name, Slice from, T to)
  {
    AdapterField af = nameToAdapterField.get(name);
    af.copyFromBaoToPojo(from, to);
  }

  /**
   * Copy field values from object to BAO.
   * @param flist
   * @param pojo
   * @param s
   */
  public void copyInto(DataDescriptor.FieldList flist, T pojo, Slice s)
  {
    for(DataDescriptor.Field f : flist.fields) {
      System.out.println("copying field " + f.name);
      copyField(f.name, pojo, s);
    }
  }

  /**
   * Populate data to Object from BAO.
   * @param flist
   * @param s
   * @param pojo
   */
  public void copyFrom(DataDescriptor.FieldList flist, Slice s, T pojo)
  {
    for(DataDescriptor.Field f : flist.fields) {
      copyField(f.name, s, pojo);
    }
  }

  public Slice getNewBaoInstance(T obj)
  {
    Slice s = bao.getNewObject();
    copyInto(flist, obj, s);
    return s;
  }

}
