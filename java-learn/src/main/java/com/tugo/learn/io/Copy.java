package com.tugo.learn.io;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.EnumSet;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Copy
{
  static boolean okayToOverwrite(Path file) {
    String answer = System.console().readLine("overwrite %s (yes/no)? ", file);
    return (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes"));
  }

  static void copyFile(Path source, Path target, boolean prompt, boolean preserve)
  {
    CopyOption[] options = (preserve)?
      new CopyOption[] { COPY_ATTRIBUTES, REPLACE_EXISTING } :
      new CopyOption[] { REPLACE_EXISTING };

    if (!prompt || Files.notExists(target) || okayToOverwrite(target)) {
      try {
        Files.copy(source, target, options);
      } catch (IOException x) {
        System.err.format("Unable to copy: %s: %s%n", source, x);
      }
    }
  }

  static class TreeCopier implements FileVisitor<Path>
  {
    private final Path source;
    private final Path target;
    private final boolean prompt;
    private final boolean preserve;

    TreeCopier(Path source, Path target, boolean prompt, boolean preserve)
    {
      this.source = source;
      this.target = target;
      this.prompt = prompt;
      this.preserve = preserve;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
    {
      CopyOption[] options = (preserve) ? new CopyOption[] { COPY_ATTRIBUTES } : new CopyOption[0];

      Path newDir = target.resolve(source.relativize(dir));
      try {
        Files.copy(dir, newDir, options);
      } catch (FileAlreadyExistsException x) {
        // ignore
      } catch (IOException ex) {
        System.err.format("Unable to create: %s: %s%n", newDir, ex);
        return FileVisitResult.SKIP_SUBTREE;
      }
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
    {
      copyFile(file, target.resolve(source.relativize(file)), prompt, preserve);
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException
    {
      if (exc instanceof FileSystemLoopException) {
        System.err.println("cycle detected: " + file);
      } else {
        System.err.format("Unable to copy: %s: %s%n", file, exc);
      }
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
    {
      if (exc == null && preserve) {
        Path newDir = target.resolve(source.relativize(dir));
        try {
          FileTime time = Files.getLastModifiedTime(dir);
          Files.setLastModifiedTime(newDir, time);
        } catch (IOException x) {
          System.err.format("Unable to copy all attributes to %s: %s%n", newDir, x);
        }
      }
      return FileVisitResult.CONTINUE;
    }
  }

  static void usage()
  {
    System.err.println("java Copy [-ip] source... target");
    System.err.println("java Copy -r [-ip] source-dir... target");
    System.exit(-1);
  }

  public static void main(String[] args) throws IOException
  {
    boolean recursive = false;
    boolean prompt = false;
    boolean preseve = false;

    int argi = 0;
    while (argi < args.length) {
      String arg = args[argi];
      if (!arg.startsWith("-"))
        break;
      if (arg.length() < 2)
        usage();
      for (int i = 0; i < arg.length(); i++) {
        char c = arg.charAt(i);
        switch (c) {
          case 'r' : recursive = true; break;
          case 'i' : prompt = true; break;
          case 'p' : preseve = true; break;
          default : usage();
        }
      }
      argi++;
    }

    int remaining = args.length - argi;
    if (remaining < 2)
      usage();

    Path[] source = new Path[remaining - 1];
    int i = 0;
    while (remaining > 1) {
      source[i++] = Paths.get(args[argi++]);
      remaining--;
    }
    Path target = Paths.get(args[argi]);

    boolean isDir = Files.isDirectory(target);

    //copy each source file/directory to the target
    for(i = 0; i < source.length; i++) {
     Path dest = (isDir) ? target.resolve(source[i].getFileName()) : target;
     if (recursive) {
       EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
       TreeCopier tc = new TreeCopier(source[i], dest, prompt, preseve);
       Files.walkFileTree(source[i], opts, Integer.MAX_VALUE, tc);
     } else {
       if (Files.isDirectory(source[i])) {
         System.err.format("%s: is a directory%n", source[i]);
         continue;
       }
       copyFile(source[i], dest, prompt, preseve);
     }
    }
  }
}
