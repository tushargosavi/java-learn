package com.tugo.dt.bao;

import java.util.Collection;


/**
 * Adapter between BAO and Pojo. Currently assume that all fields are public.
 */
public class PojoToBoaAdapter<T> extends AbstractAdapter
{
  Class<T> klass;
  PojoAnalyzer analyzer;

  public PojoToBoaAdapter()
  {
  }

  public PojoToBoaAdapter(Class<T> klass)
  {
    this(klass, null);
  }

  public PojoToBoaAdapter(Class<T> klass, Collection<String> fieldNames)
  {
    this.klass = klass;
    analyzer = new PojoAnalyzer(klass);
    initialize(fieldNames);
  }

  public PojoToBoaAdapter(PojoAnalyzer<T> analyzer, Collection<String> fieldNames)
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
      AdapterField af = new AdapterField();
      af.name = f.name;
      af.type = f.type;
      af.baoGetter = bao.getGetter(f.name);
      af.baoSetter = bao.getSetter(f.name);
      af.pojoGetter = analyzer.getGetter(f.name);
      af.pojoSetter = analyzer.getSetter(f.name);
      nameToAdapterField.put(af.name, af);
      adapterFields.add(af);
    }
  }

  public PojoToBoaAdapter getSubView(Collection<String> fieldNames)
  {
    return new PojoToBoaAdapter(analyzer, fieldNames);
  }
}
