package com.tugo.dt.bao;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

public enum TypeInfo
{
  BOOLEAN ("bool", 1, boolean.class, Boolean.class),
  BYTE ("byte", 1, byte.class, Byte.class),
  CHAR ("char", 2, char.class, Character.class),
  SHORT ("short", 2, short.class, Short.class),
  INT ("int", 4, int.class, Integer.class),
  LONG ("long", 8, long.class, Long.class),
  FLOAT ("float", 4, float.class, Float.class),
  DOUBLE ("double", 8, double.class, Double.class),
  STRING ("string", 2, String.class, String.class),
  OBJECT ("object", -1, Object.class, Object.class);

  private final String name;
  private final int size;
  private final Class typeClass;
  private final Class wrapperClass;

  TypeInfo(String name, int size, Class klass, Class wrapperClass)
  {
    this.name = name;
    this.size = size;
    this.typeClass = klass;
    this.wrapperClass = wrapperClass;
  }

  public String getName()
  {
    return name;
  }

  public int getSize()
  {
    return size;
  }

  public Class getTypeClass()
  {
    return typeClass;
  }

  public Class getWrapperClass()
  {
    return wrapperClass;
  }

  static final TypeInfo[] supportedTypes = new TypeInfo[]{BOOLEAN, BYTE, CHAR, SHORT, INT, LONG, FLOAT, DOUBLE, STRING};

  static final Map<String, TypeInfo> nameToTypeMap = Maps.newHashMap();
  static final Map<Class, TypeInfo> classToTypeMap = Maps.newHashMap();

  static final Map<String, Class> nameToClassMap = Maps.newHashMap();
  static final Map<String, Class> nameToWrapperClassMap = Maps.newHashMap();
  static final Map<Class, String> classToNameMap = Maps.newHashMap();
  static final Map<String, Integer> nameToSizeMap = Maps.newHashMap();

  /** Numeric classes */
  public static final Set<Class> numericClasses = Sets.newHashSet();
  public static Set<String> numericNames = Sets.newHashSet();

  static {
    for(TypeInfo ti : supportedTypes) {
      nameToTypeMap.put(ti.name, ti);
      classToTypeMap.put(ti.typeClass, ti);
      classToTypeMap.put(ti.wrapperClass, ti);

      nameToSizeMap.put(ti.name, ti.size);
      nameToClassMap.put(ti.name, ti.typeClass);
      nameToWrapperClassMap.put(ti.name, ti.wrapperClass);
      classToNameMap.put(ti.typeClass, ti.name);
      classToNameMap.put(ti.wrapperClass, ti.name);
    }

    numericClasses.add(Short.class);
    numericClasses.add(Short.TYPE);
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

    numericNames.add("short");
    numericNames.add("int");
    numericNames.add("long");
    numericNames.add("float");
    numericNames.add("double");

  }

  public static String getNameFromType(Class type) {
    return classToNameMap.get(type);
  }

  public static TypeInfo getTypeInfo(String name)
  {
    return nameToTypeMap.get(name);
  }

  public static int getSize(String name) {
    return nameToTypeMap.get(name).size;
  }

  public static int getSize(Class klass) {
    return classToTypeMap.get(klass).size;
  }

  public static boolean isTypeSupported(String name) {
    return nameToClassMap.containsKey(name);
  }

  public static boolean isTypeSupported(Class klass) {
    return classToNameMap.containsKey(klass);
  }

  public static boolean isNumericType(String name) {
    return numericNames.contains(name);
  }

  public static boolean isNumericType(Class klass) {
    return numericClasses.contains(klass);
  }

  public static TypeInfo getTypeInfoFromClass(Class<?> type)
  {
    return classToTypeMap.get(type);
  }
}
