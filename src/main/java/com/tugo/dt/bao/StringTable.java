package com.tugo.dt.bao;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * A very crude implementation of string table.
 */
public class StringTable
{
  private static Map<String, Integer> map = Maps.newHashMap();
  private static List<String> lst = Lists.newArrayList();
  private static int lastIndex = -1;

  public static int add(String str)
  {
    if (map.containsKey(str))
      return map.get(str).intValue();
    lastIndex++;
    lst.add(str);
    map.put(str, lastIndex);
    return lastIndex;
  }

  public static String get(int index) {
    return lst.get(index);
  }
}
