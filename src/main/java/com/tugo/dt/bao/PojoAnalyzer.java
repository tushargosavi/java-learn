package com.tugo.dt.bao;

import com.google.common.base.Preconditions;
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

    /**
     * Create getter and setter based on getterExpression and setterExpression.
     * @param klass
     */
    public void updateGettersAndSetters(Class klass)
    {
      Preconditions.checkNotNull(getterExpr);
      Preconditions.checkNotNull(setterExpr);
      getter = PojoUtils.constructGetter(klass, getterExpr, TypeInfo.getTypeInfo(type).getTypeClass());
      setter = PojoUtils.constructSetter(klass, setterExpr, "{$}", "{#}", TypeInfo.getTypeInfo(type).getTypeClass());
    }
  }

  private List<Field> fieldList;
  private List<Method> getterMethodList;
  private List<Method> setterMethodList;

  private Map<String, String> getterExpressions;
  private Map<String, String> setterExpressions;

  private Map<String, Object> getterMap;
  private Map<String, Object> setterMap;

  public PojoAnalyzer(Class<T> klass)
  {
    this.klass = klass;
    analyzeClass();
  }

  private void analyzeClass()
  {
    fieldList = findFields();
    getterMethodList = findGetterMethods();
    setterMethodList = findSetterMethods();
    getterExpressions  = generateGetterExpressions();
    setterExpressions = generateSetterExpressions();
  }


  /**
   * Return public numeric fields or getters defined in class
   *
   * @return
   */
  public List<Field> findFields()
  {
    List<Field> lst = new ArrayList<Field>();
    for (Field f : klass.getFields()) {
      Class type = f.getType();
      if (TypeInfo.isTypeSupported(type)) {
        System.out.println("fild " + f.getName() + " type " + f.getType());
        lst.add(f);
      }
    }
    return lst;
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
  public List<Method> findGetterMethods()
  {
    List<Method> lst = new ArrayList<Method>();
    for (Method m : klass.getMethods()) {
      String name = m.getName();
      Class<?>[] params = m.getParameterTypes();
      if (name.startsWith("get") && params.length == 0 && TypeInfo.isNumericType(m.getReturnType())) {
        lst.add(m);
      }
    }
    return lst;
  }

  public List<Method> findSetterMethods()
  {
    List<Method> lst = new ArrayList<Method>();
    for (Method m : klass.getMethods()) {
      String name = m.getName();
      Class<?>[] params = m.getParameterTypes();
      if (name.startsWith("set") && params.length == 1 && TypeInfo.isNumericType(params[0])) {
        lst.add(m);
      }
    }
    return lst;
  }

  public Map<String, String> generateGetterExpressions()
  {

    Map<String, String> exprs = Maps.newHashMap();
    for (Field f : fieldList) {
      exprs.put(f.getName(), f.getName());
    }

    for (Method m : getterMethodList) {
      exprs.put(getFieldNameFromGetter(m.getName()), m.getName() + "()");
    }
    return exprs;
  }


  private Map<String, String> generateSetterExpressions()
  {

    Map<String, String> exprs = Maps.newHashMap();
    for (Field f : fieldList) {
      exprs.put(f.getName(), f.getName());
    }

    for (Method m : setterMethodList) {
      exprs.put(getFieldNameFromGetter(m.getName()), m.getName() + "()");
    }
    return exprs;
  }


  public Map<String, String> generateFieldTypeMapping()
  {
    Map<String, String> exprs = Maps.newHashMap();
    for (Field f : fieldList) {
      exprs.put(f.getName(), TypeInfo.getNameFromType(f.getType()));
    }
    return exprs;
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
    for(Field f : fieldList) {
      FieldInfoWithGetterSetter finfo = new FieldInfoWithGetterSetter();
      finfo.name = f.getName();
      finfo.type = TypeInfo.getNameFromType(f.getType());
      finfo.getterExpr = finfo.name;
      finfo.setterExpr = finfo.name;
      finfo.updateGettersAndSetters(klass);
      getterMap.put(finfo.name, finfo.getter);
      setterMap.put(finfo.name, finfo.setter);
      lst.add(finfo);
    }
    return lst;
  }

  public List<Method> getGetterMethodList()
  {
    return getterMethodList;
  }

  public List<Method> getSetterMethodList()
  {
    return setterMethodList;
  }

  public Object getGetter(String name) {
    return getterMap.get(name);
  }

  public Object getSetter(String name) {
    return setterMap.get(name);
  }
}
