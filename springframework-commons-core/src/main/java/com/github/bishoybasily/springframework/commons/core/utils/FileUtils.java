package com.github.bishoybasily.springframework.commons.core.utils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

/**
 * @author bishoybasily
 * @since 3/15/20
 */
public class FileUtils {

    public static Flux<String> lines(Callable<InputStream> callable) {
        return Mono.fromCallable(callable).flatMapMany(FileUtils::lines);
    }

    public static Flux<String> lines(InputStream stream) {
        return Flux.create(sink -> {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = reader.readLine()) != null)
                    sink.next(line);
                sink.complete();
            } catch (IOException e) {
                sink.error(e);
            }
        });
    }

}
