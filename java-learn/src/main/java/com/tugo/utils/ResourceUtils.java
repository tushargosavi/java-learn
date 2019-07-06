package com.tugo.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.utils.IOUtils;

public class ResourceUtils
{
  public static byte[] getContent(String str) throws IOException
  {
    byte[] ret = null;
    try(
      InputStream is = ResourceUtils.class.getResourceAsStream(str);
      ByteArrayOutputStream bao = new ByteArrayOutputStream()) {
      IOUtils.copy(is, bao);
      return bao.toByteArray();
    }
  }

  public static String getContentAsString(String str) throws IOException
  {
    return new String(getContent(str));
  }
}
