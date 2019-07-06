package com.tugo.dt;

import com.datatorrent.common.util.Slice;
import com.tugo.dt.PojoUtils.GetterInt;
import com.tugo.dt.bao.BAObject;
import com.tugo.dt.bao.SliceGetters;
import com.tugo.dt.bao.PojoAdapter;
import com.tugo.dt.bao.PojoAnalyzer;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Test1
{
  public static class SimpleClass
  {
    public int field1;
    private  int field2;

    public int getField2() { return field2; }

    public void setField2(int field2)
    {
      this.field2 = field2;
    }
  }

  @Test
  public void test1()
  {
    System.out.println("this is test");
    DimensionSchemaJsonGenerator.PojoDimensioSchemaJsonGenerator gen = new DimensionSchemaJsonGenerator.PojoDimensioSchemaJsonGenerator(SimpleClass.class);
    System.out.println(gen.fieldList);
    System.out.println(gen.getterList);
    System.out.println(gen.generateExpressions());
  }

  @Test
  public void pojoTest()
  {
    GetterInt<SimpleClass> intGet = PojoUtils.createGetterInt(SimpleClass.class, "field1");
    SimpleClass s = new SimpleClass();
    s.field1 = 10;
    s.field2 = 23;
    int tmp = intGet.get(s);
    System.out.println("value of tmp " + tmp);
  }


  Object createGetterFromSlice(int start)
  {
    /*
    String helper = ByteArrayHelper.class.getName();
    String code = helper + ".getInt({$}.arr, {$}.start +" + start + ")";
    System.out.println("code " + code);
    return PojoUtils.createGetterInt(Slice.class, code);
    */
    return new SliceGetters.IntGetter(start);
  }

  @Test
  public void test3()
  {
    GetterInt<Slice> getterInt = (GetterInt<Slice>)createGetterFromSlice(0);
    ByteBuffer bb = ByteBuffer.allocate(4);
    bb.putInt(1005);
    byte[] arr = bb.array();
    System.out.println("array len is " + arr.length);
    Slice s = new Slice(arr);
    System.out.println("get from slice is " + getterInt.get(s));
  }

  @Test
  public void test4()
  {
    PojoAnalyzer pa = new PojoAnalyzer(SimpleClass.class);
    List<PojoAnalyzer.FieldInfoWithGetterSetter> lst = pa.generateFastGettersAndSetters();
    SimpleClass s = new SimpleClass();
    s.field1 = 12;
    s.field2 = 23;
    for(PojoAnalyzer.FieldInfoWithGetterSetter l : lst) {
      System.out.println("name " + l.name);
    }
  }

  @Test
  public void test5()
  {
    PojoAdapter<SimpleClass> pa = new PojoAdapter<SimpleClass>(SimpleClass.class, null);
    SimpleClass s = new SimpleClass();
    s.field1 = 16;
    s.field2 = 23;
    Slice slice = pa.getNewBaoInstance(s);

    BAObject bao = pa.getBao();
    GetterInt gi = (GetterInt) bao.getGetter("field1");
    System.out.println("get from slice " + gi.get(slice));
    System.out.println("all field size " + slice.length);

    List l = new ArrayList();
    l.add("field2");
    PojoAdapter pa1 = pa.getSubView(l);
    Slice s1 = pa1.getNewBaoInstance(s);
    System.out.println("length of sub view " + s1.length);
  }
}
