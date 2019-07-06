package com.tugo.dt.bao;

import java.util.Map;

public class MapAdapter extends AbstractAdapter<Map>
{
  public MapAdapter(DataDescriptor.FieldList flist) {
    this.flist = flist;
    DataDescriptor.FieldList lst = MapGetters.createGetterSetters(flist);
    bao = new BAObject(flist);
    createAdapterFields();
  }

  private void createAdapterFields()
  {
    for(DataDescriptor.Field f : flist.fields) {
      AdapterField af = new AdapterField(f.name, f.typeInfo);
      af.baoGetter = bao.getGetter(f.name);
      af.baoSetter = bao.getSetter(f.name);
      af.pojoGetter = MapGetters.createGetter(f);
      af.pojoSetter = MapGetters.createObjectSetter(f);
      nameToAdapterField.put(af.name, af);
      adapterFields.add(af);
    }
  }


}
