package com.github.bishoybasily.springframework.commons.core.utils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;

/**
 * @author bishoybasily
 * @since 2020-06-21
 */
public class ReactiveUtils {

    public static <T> Flux<T> toFlux(Callable<Iterable<T>> callable) {
        return Mono.fromCallable(callable).flatMapIterable(it -> it);
    }

}
