package com.tugo.dt.bao;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tugo.dt.PojoUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Create a field list form Pojo object.
 * currently supports only basic data types see @TypeInfo for more information.
 */
public class PojoAnalyzer<T>
{
  Class<T> klass;

  public Class<T> getPojoClass()
  {
    return klass;
  }

  /**
   * A information about the field in Pojo with getter and setter interfaces.
   */
  public static class FieldInfoWithGetterSetter  extends DataDescriptor.Field {
    /**
     * A getter Interface for object
     */
    public Object getter;
    /**
     * expression used while creation of getter.
     */
    String getterExpr;
    /**
     * A setter interface @see(Setter)
     */
    public Object setter;
    /**
     * expression used while creation of setter.
     */
    String setterExpr;

    public FieldInfoWithGetterSetter(String name, TypeInfo ti)
    {
      super(name, ti);
    }

    public FieldInfoWithGetterSetter(DataDescriptor.Field f)
    {
      super(f.name, f.getTypeInfo());
    }

    /**
     * Create getter and setter based on getterExpression and setterExpression.
     * @param klass
     */
    public void updateGettersAndSetters(Class klass)
    {
      Preconditions.checkNotNull(getterExpr);
      Preconditions.checkNotNull(setterExpr);
      getter = PojoUtils.constructGetter(klass, getterExpr, typeInfo.getTypeClass());
      setter = PojoUtils.constructSetter(klass, setterExpr, "{$}", "{#}", typeInfo.getTypeClass());
    }
  }

  private final Map<String, Object> getterMap = Maps.newHashMap();
  private final Map<String, Object> setterMap = Maps.newHashMap();

  public DataDescriptor.FieldList fieldList;
  private List<FieldInfoWithGetterSetter> fieldInfoList = new ArrayList<FieldInfoWithGetterSetter>();
  private Map<String, FieldInfoWithGetterSetter> fieldsMap = Maps.newHashMap();

  public PojoAnalyzer(Class<T> klass)
  {
    this.klass = klass;
    analyzeClass();
  }

  private void analyzeClass()
  {
    findPublicFields();
    findGetterMethods();
    findSetterMethods();
    fieldInfoList = generateFastGettersAndSetters();
    fieldList = new DataDescriptor.FieldList(fieldInfoList);
  }


  /**
   * Return public numeric fields or getters defined in class
   *
   * @return
   */
  public void findPublicFields()
  {
    List<Field> lst = new ArrayList<Field>();
    for (Field f : klass.getFields()) {
      Class type = f.getType();
      if (TypeInfo.isTypeSupported(type)) {
        System.out.println("fild " + f.getName() + " type " + f.getType());
        lst.add(f);
        FieldInfoWithGetterSetter fi = new FieldInfoWithGetterSetter(f.getName(), TypeInfo.getTypeInfoFromClass(f.getType()));
        fi.getterExpr = fi.name;
        fi.setterExpr = fi.name;
        fieldsMap.put(fi.name, fi);
        fieldInfoList.add(fi);
      }
    }
  }

  /**
   * Consider the method only if
   * - name starts with get.
   * - does not take any argument.
   * - is defined as public.
   * - returns numeric data.
   *
   * @return List of methods found.
   */
  public void findGetterMethods()
  {
    List<Method> lst = new ArrayList<Method>();
    for (Method m : klass.getMethods()) {
      String mName = m.getName();
      Class<?>[] params = m.getParameterTypes();
      if (mName.startsWith("get") && params.length == 0 && TypeInfo.isNumericType(m.getReturnType())) {
        String name = getFieldNameFromGetter(m.getName());
        FieldInfoWithGetterSetter fi = fieldsMap.get(name);
        if (fi == null) {
          fi = new FieldInfoWithGetterSetter(m.getName(), TypeInfo.getTypeInfoFromClass(m.getReturnType()));
          fieldsMap.put(fi.name, fi);
          fieldInfoList.add(fi);
        }
        fi.getterExpr = "{$}." + m.getName() + "()";
      }
    }
  }

  public void findSetterMethods()
  {
    for (Method m : klass.getMethods()) {
      String Mname = m.getName();
      Class<?>[] params = m.getParameterTypes();
      if (Mname.startsWith("set") && params.length == 1 && TypeInfo.isNumericType(params[0])) {
        String name = getFieldNameFromGetter(m.getName());
        FieldInfoWithGetterSetter fi = fieldsMap.get(name);
        if (fi != null) {
          fi.setterExpr = "{$}." + m.getName() + "({#})" ;
        }
      }
    }
  }

  private String getFieldNameFromGetter(String name)
  {
    StringBuilder str = new StringBuilder(name.substring(3));
    Character c = Character.toLowerCase(str.charAt(0));
    str.setCharAt(0, c);
    return str.toString();
  }

  public List<FieldInfoWithGetterSetter> generateFastGettersAndSetters()
  {
    List<FieldInfoWithGetterSetter> lst = new ArrayList<FieldInfoWithGetterSetter>();
    for(FieldInfoWithGetterSetter fi : fieldInfoList) {
      if (fi.getterExpr == null || fi.setterExpr == null) {
        System.out.println("getter or setter set numm for " + fi.name);
        continue;
      }
      fi.updateGettersAndSetters(klass);
      getterMap.put(fi.name, fi.getter);
      setterMap.put(fi.name, fi.setter);
      lst.add(fi);
    }
    return lst;
  }

  public Object getGetter(String name) {
    return getterMap.get(name);
  }

  public Object getSetter(String name) {
    return setterMap.get(name);
  }

  public DataDescriptor.FieldList getFieldList()
  {
    return fieldList;
  }
}
