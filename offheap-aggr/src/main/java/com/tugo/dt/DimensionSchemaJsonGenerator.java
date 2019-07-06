package com.tugo.dt;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Helper class to generate JSON schema used by DimensionsComputation and DimensionStore.
 */
public interface DimensionSchemaJsonGenerator
{
  String generateSchema();

  /**
   * Generate dimension schema from POJO.
   * @param <T>
   */
  class PojoDimensioSchemaJsonGenerator<T> implements DimensionSchemaJsonGenerator
  {
    Class<T> klass;

    List<Field> fieldList;
    List<Method> getterList;

    Map<String, String> fieldExpressions;

    public static final Set<Class> numericClasses = Sets.newHashSet();

    static {
      numericClasses.add(Integer.class);
      numericClasses.add(Integer.TYPE);
      numericClasses.add(Long.class);
      numericClasses.add(Long.TYPE);
      numericClasses.add(Float.class);
      numericClasses.add(Float.TYPE);
      numericClasses.add(Double.class);
      numericClasses.add(Double.TYPE);
      numericClasses.add(Short.class);
      numericClasses.add(Short.TYPE);
    }

    static boolean isNumericClass(Class c) {
      return numericClasses.contains(c);
    }

    PojoDimensioSchemaJsonGenerator(Class<T> klass)
    {
      this.klass = klass;
      analyzeClass();
    }

    private void analyzeClass()
    {
      fieldList = findNumericFields();
      getterList = findGetterMethods();
      fieldExpressions = generateExpressions();
    }

    /**
     * Return public numeric fields or getters defined in class
     * @return
     */
    public List<Field> findNumericFields()
    {
      List<Field> lst = new ArrayList<Field>();
      for(Field f : klass.getFields())
      {
        Class type = f.getType();
        if (isNumericClass(type)) {
          System.out.println("fild " + f.getName() + " type " + f.getType());
          lst.add(f);
        }
      }
      return lst;
    }

    /**
     * Consider the method only if
     *  - name starts with get.
     *  - does not take any argument.
     *  - is defined as public.
     *  - returns numeric data.
     * @return List of methods found.
     */
    public List<Method> findGetterMethods()
    {
      List<Method> lst = new ArrayList<Method>();
      for(Method m : klass.getMethods())
      {
        String name = m.getName();
        Class<?>[] params = m.getParameterTypes();
        if (name.startsWith("get") && params.length == 0 && isNumericClass(m.getReturnType()))
        {
          lst.add(m);
        }
      }
      return lst;
    }

    public Map<String, String> generateExpressions() {

      Map<String, String> exprs = Maps.newHashMap();
      for(Field f : fieldList) {
        exprs.put(f.getName(), f.getName());
      }

      for(Method m : getterList) {
        exprs.put(getFieldNameFromGetter(m.getName()), m.getName() + "()");
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


    public String generateSchema()
    {
      return null;
    }

  }

  /**
   * Generate dimension schema from a input schema in json format.
   * @param <T>
   */
  class JsonDimensioSchemaJsonGenerator<T> implements DimensionSchemaJsonGenerator
  {
    public String generateSchema()
    {
      return null;
    }
  }

}
