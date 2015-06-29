package com.tugo.dt;

import com.datatorrent.common.util.Slice;
import com.tugo.dt.PojoUtils.GetterInt;
import com.tugo.dt.bao.ByteArrayGetters;
import com.tugo.dt.bao.PojoAdapter;
import com.tugo.dt.bao.PojoAnalyzer;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.List;

public class Test1
{
  public static class SimpleClass
  {
    public int field1;
    private int field2;

    public int getField2() { return field2; }
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
    return new ByteArrayGetters.IntGetter(start);
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
    s.field1 = 10;
    s.field2 = 23;
    for(PojoAnalyzer.FieldInfoWithGetterSetter l : lst) {
      System.out.println("name " + l.name);
    }
  }

  @Test
  public void test5()
  {
    PojoAdapter<SimpleClass> pa = new PojoAdapter<SimpleClass>(SimpleClass.class);
    Slice s = pa.getNewBaoInstance();
  }
}
