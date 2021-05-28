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
public class CsvUtils {

    public static Flux<String[]> values(Callable<InputStream> callable, String splitter) {
        return FileUtils.lines(callable).map(splitter(splitter));
    }

    public static Flux<String[]> values(InputStream stream, String splitter) {
        return FileUtils.lines(stream).map(splitter(splitter));
    }

    private static Function<String, String[]> splitter(String splitter) {
        return s -> {
            return Arrays.asList(s.split(splitter))
                    .stream()
                    .map(it -> it.replaceAll("\"", ""))
                    .toArray(String[]::new);
        };
    }

}
