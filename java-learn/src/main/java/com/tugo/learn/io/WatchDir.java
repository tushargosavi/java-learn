package com.tugo.learn.io;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class WatchDir
{
  @SuppressWarnings("unchecked")
  static <T> WatchEvent<T> cast(WatchEvent<?> event) {
    return (WatchEvent<T>)event;
  }

  public static void main(String[] args) throws IOException
  {

    if (args.length < 1) {
      System.err.format("Usage <dir>\n");
      System.exit(-1);
    }

    WatchService watcher = FileSystems.getDefault().newWatchService();
    HashMap<WatchKey, Path> keys = new HashMap<WatchKey, Path>();
    Path dir = Paths.get(args[0]);
    WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
    keys.put(key, dir);

    while (true) {
      try {
        key = watcher.take();
      } catch (InterruptedException x) {
        return;
      }

      dir = keys.get(key);
      if (dir == null) {
        System.err.println("WatchKey not recognized!!");
        continue;
      }

      for (WatchEvent<?> event : key.pollEvents()) {
        WatchEvent.Kind kind = event.kind();

        if (kind == OVERFLOW) {
          continue;
        }

        WatchEvent<Path> ev = cast(event);
        Path name = ev.context();
        Path child = dir.resolve(name);

        System.out.format("%s: %s\n", event.kind().name(), child);

        boolean valid = key.reset();
        if (!valid) {
          keys.remove(key);

          if (keys.isEmpty()) {
            break;
          }
        }
      }
    }
  }

}
