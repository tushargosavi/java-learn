package com.tugo.dt.bao;

import com.datatorrent.common.util.Slice;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * Adapter between BAO and Pojo. Currently assume that all fields are public.
 */
public class PojoAdapter<T>
{
  class AdapterField extends DataDescriptor.Field {
    Object pojoGetter;
    Object pojoSetter;
    Object baoGetter;
    Object baoSetter;
  }

  Class<T> klass;
  PojoAnalyzer analyzer;
  DataDescriptor.FieldList flist;
  private BAObject bao;

  public PojoAdapter()
  {
  }

  public PojoAdapter(Class<T> klass)
  {
    this.klass = klass;
    analyzer = new PojoAnalyzer(klass);
    initialize();
  }

  private void initialize()
  {
    flist = getFromPojo(klass);
    bao = new BAObject(flist);
    createAdapterFields();
  }

  private void createAdapterFields()
  {
    for(DataDescriptor.Field f : flist.fields) {
      AdapterField af = new AdapterField();
      af.name = f.name;
      af.type = f.type;
      af.baoGetter = bao.getGetter(f.name);
      af.baoSetter = bao.getSetter(f.name);
      af.pojoGetter = analyzer.getGetter(f.name);
      af.pojoSetter = analyzer.getSetter(f.name);
    }
  }

  public DataDescriptor.FieldList getFromPojo(Class<T> klass)
  {
    List<DataDescriptor.Field> flist = Lists.newArrayList();
    for (java.lang.reflect.Field f : klass.getDeclaredFields()) {
      if (TypeInfo.isTypeSupported(f.getType())) {
        DataDescriptor.Field field = new DataDescriptor.Field(f.getName(), TypeInfo.getNameFromType(f.getType()));
        flist.add(field);
      }
    }
    return new DataDescriptor.FieldList(flist);
  }

  public BAObject getBao()
  {
    return bao;
  }

  public Slice getNewBaoInstance()
  {
    Slice s = bao.getNewObject();
    return s;
  }

}
