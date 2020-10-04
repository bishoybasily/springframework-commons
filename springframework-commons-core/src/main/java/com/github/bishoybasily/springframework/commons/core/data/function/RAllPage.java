package com.github.bishoybasily.springframework.commons.core.data.function;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

/**
 * @author bishoybasily
 * @since 3/15/20
 */
@FunctionalInterface
public interface RAllPage<T, R> {

    Flux<T> find(R r, Pageable pageable);

}
