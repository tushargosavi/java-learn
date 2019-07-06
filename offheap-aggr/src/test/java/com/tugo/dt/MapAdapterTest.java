package com.tugo.dt;

import com.datatorrent.common.util.Slice;
import com.google.common.collect.Maps;
import com.tugo.dt.bao.BAObject;
import com.tugo.dt.bao.DataDescriptor;
import com.tugo.dt.bao.MapAdapter;
import org.junit.Test;

import java.util.Map;

public class MapAdapterTest
{
  @Test
  public void mapAdapterTest()
  {
    Map<String, Object> map = Maps.newHashMap();
    map.put("field1", new Integer(45));
    map.put("field2", new Long(45));
    map.put("strfield", "Tushar Gosavi");
    DataDescriptor.FieldList flist = DataDescriptor.FieldList.Builder().add("field1", "int").add("field2", "long")
    .add("strfield", "string").build();
    MapAdapter ma = new MapAdapter(flist);

    Slice s = ma.getNewBaoInstance(map);
    BAObject bao = ma.getBao();
    PojoUtils.GetterInt getter = (PojoUtils.GetterInt) bao.getGetter("field1");
    System.out.println("size of slice is " + s.length);
    System.out.println("value of int is " + getter.get(s));

    PojoUtils.Getter gt = (PojoUtils.Getter) bao.getGetter("strfield");
    System.out.println("sting getter  " + gt.get(s));
  }
}
