package com.tugo.dt.bao;

import java.util.Collection;


/**
 * Adapter between BAO and Pojo. Currently assume that all fields are public.
 */
public class PojoAdapter<T> extends AbstractAdapter
{
  Class<T> klass;
  PojoAnalyzer analyzer;

  public PojoAdapter()
  {
  }

  public PojoAdapter(Class<T> klass)
  {
    this(klass, null);
  }

  public PojoAdapter(Class<T> klass, Collection<String> fieldNames)
  {
    this.klass = klass;
    analyzer = new PojoAnalyzer(klass);
    initialize(fieldNames);
  }

  public PojoAdapter(PojoAnalyzer<T> analyzer, Collection<String> fieldNames)
  {
    this.analyzer = analyzer;
    this.klass = analyzer.getPojoClass();
    initialize(fieldNames);
  }

  private void initialize(Collection<String> fieldNames)
  {
    flist = analyzer.getFieldList();
    /* select only selected fields */
    if (fieldNames != null)
      flist = flist.getSubSet(fieldNames);
    bao = new BAObject(flist);
    createAdapterFields();
  }

  private void createAdapterFields()
  {
    for(DataDescriptor.Field f : flist.fields) {
      AdapterField af = new AdapterField(f.name, f.typeInfo);
      af.baoGetter = bao.getGetter(f.name);
      af.baoSetter = bao.getSetter(f.name);
      af.pojoGetter = analyzer.getGetter(f.name);
      af.pojoSetter = analyzer.getSetter(f.name);
      nameToAdapterField.put(af.name, af);
      adapterFields.add(af);
    }
  }

  public PojoAdapter getSubView(Collection<String> fieldNames)
  {
    return new PojoAdapter(analyzer, fieldNames);
  }
}
