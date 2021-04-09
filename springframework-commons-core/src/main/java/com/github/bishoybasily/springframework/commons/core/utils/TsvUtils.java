package com.github.bishoybasily.springframework.commons.core.utils;

import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * @author bishoybasily
 * @since 3/15/20
 */
public class TsvUtils {

  public static Flux<String[]> values(Callable<InputStream> callable) {
    return FileUtils.lines(callable).map(splitter());
  }

  public static Flux<String[]> values(InputStream stream) {
    return FileUtils.lines(stream).map(splitter());
  }

  private static Function<String, String[]> splitter() {
    return s -> {
      return Arrays.asList(s.split("\t"))
        .stream()
        .map(it -> it.replaceAll("\"", ""))
        .toArray(String[]::new);
    };
  }

}
