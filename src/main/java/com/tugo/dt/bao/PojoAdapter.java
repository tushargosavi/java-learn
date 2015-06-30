package com.tugo.dt.bao;

import com.datatorrent.common.util.Slice;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.tugo.dt.PojoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public void copyFromPojoToBao(T from, Slice to)
    {
      TypeInfo ti = TypeInfo.getTypeInfo(type);
      Object getter = pojoGetter;
      Object setter = baoSetter;
      transferField(ti, getter, setter, from, to);
    }

    public void copyFromBaoToPojo(Slice from, T to)
    {
      TypeInfo ti = TypeInfo.getTypeInfo(type);
      Object getter = baoGetter;
      Object setter = pojoSetter;
      transferField(ti, getter, setter, from, to);
    }

    private void transferField(TypeInfo ti, Object getter, Object setter, Object from, Object to)
    {
      Preconditions.checkNotNull(ti);
      Preconditions.checkNotNull(getter);
      Preconditions.checkNotNull(setter);
      Preconditions.checkNotNull(from);
      Preconditions.checkNotNull(to);

      switch (ti) {
      case BOOLEAN:
        boolean b = ((PojoUtils.GetterBoolean)getter).get(from);
        ((PojoUtils.SetterBoolean)setter).set(to, b);
        break;
      case BYTE:
        byte v = ((PojoUtils.GetterByte)getter).get(from);
        ((PojoUtils.SetterByte)setter).set(to, v);
        break;
      case CHAR:
        char c = ((PojoUtils.GetterChar)getter).get(from);
        ((PojoUtils.SetterChar)setter).set(to, c);
        break;
      case SHORT:
        short s = ((PojoUtils.GetterShort)getter).get(from);
        ((PojoUtils.SetterShort)setter).set(to, s);
        break;
      case INT:
        int i = ((PojoUtils.GetterInt)getter).get(from);
        ((PojoUtils.SetterInt)setter).set(to, i);
        break;
      case LONG:
        long l = ((PojoUtils.GetterLong)getter).get(from);
        ((PojoUtils.SetterLong)setter).set(to, l);
        break;
      case FLOAT:
        float f = ((PojoUtils.GetterFloat)getter).get(from);
        ((PojoUtils.SetterFloat)setter).set(to, f);
        break;
      case DOUBLE:
        double d = ((PojoUtils.GetterDouble)getter).get(from);
        ((PojoUtils.SetterDouble)setter).set(to, d);
        break;
      default:
        Object o = ((PojoUtils.Getter)getter).get(from);
        ((PojoUtils.Setter)setter).set(to, o);
        break;
      }
    }
  }

  Class<T> klass;
  PojoAnalyzer analyzer;
  DataDescriptor.FieldList flist;
  private BAObject bao;
  Map<String, AdapterField> nameToAdapterField = Maps.newHashMap();
  List<AdapterField> adapterFields = new ArrayList<AdapterField>();

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
    List<DataDescriptor.Field> list = analyzer.getFieldInfoList();
    flist = new DataDescriptor.FieldList(list);
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

  public void copyFromPojo(DataDescriptor.FieldList flist, T pojo, Slice s)
  {
    for(DataDescriptor.Field f : flist.fields) {
      System.out.println("copying field " + f.name);
      copyField(f.name, pojo, s);
    }
  }

  public void copyFromBao(DataDescriptor.FieldList flist, Slice s, T pojo)
  {
    for(DataDescriptor.Field f : flist.fields) {
      copyField(f.name, s, pojo);
    }
  }

  public Slice getNewBaoInstance(T obj)
  {
    Slice s = bao.getNewObject();
    copyFromPojo(flist, obj, s);
    return s;
  }
}
