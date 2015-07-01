package com.tugo.dt.bao;

import java.util.Collection;

public class BaoAdapter extends AbstractAdapter
{
  BAObject left;

  public BaoAdapter(BAObject left, Collection<String> subFields)
  {
    this.left = left;
    initialize(subFields);
  }


  public BaoAdapter(BAObject left)
  {
    this(left, null);
  }

  private void initialize(Collection<String> fieldNames)
  {
    flist = left.getFieldList();
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
      af.pojoGetter = left.getGetter(f.name);
      af.pojoSetter = left.getSetter(f.name);
      nameToAdapterField.put(af.name, af);
      adapterFields.add(af);
    }
  }
}
