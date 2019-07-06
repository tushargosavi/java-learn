package com.tugo.learn.io;

import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ListRootDirs
{
  public static void main(String[] args)
  {
    Iterable<Path> dirs = FileSystems.getDefault().getRootDirectories();
    for (Path name : dirs) {
      System.err.println(name);
    }

    // list directoris
    Path dir = Paths.get("/Users/tushar/");
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
      for (Path file : stream) {
        System.out.println(file.getFileName());
      }
    } catch (IOException | DirectoryIteratorException x) {
      // IOException can never be thrown by the iteration.
      // In this snippet, it can only be thrown by newDirectoryStream.
      System.err.println(x);
    }

    DirectoryStream.Filter<Path> filter = file -> (Files.isDirectory(file));
  }

}
