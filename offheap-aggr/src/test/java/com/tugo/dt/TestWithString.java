package com.tugo.dt;

import com.datatorrent.common.util.Slice;
import com.tugo.dt.bao.PojoAdapter;
import org.junit.Test;

public class TestWithString
{
  public static class TestClass
  {
    public int a;
    public String b;
  }

  @Test
  public void test1()
  {
    TestClass a = new TestClass();
    a.a = 10;
    a.b = "Tushar";

    PojoAdapter ad = new PojoAdapter(TestClass.class);
    Slice s = ad.getNewBaoInstance(a);
    System.out.println("len of slice is " + s.length);

    PojoUtils.Getter getter = (PojoUtils.Getter) ad.getBao().getGetter("b");
    System.out.println("str " + getter.get(s));
  }
}
