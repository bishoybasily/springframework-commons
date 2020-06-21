package com.github.bishoybasily.springframework.commons.core.data.function;

import reactor.core.publisher.Flux;

/**
 * @author bishoybasily
 * @since 3/15/20
 */
@FunctionalInterface
public interface RAll<T, R> {

    Flux<T> findAll(R r);

}
