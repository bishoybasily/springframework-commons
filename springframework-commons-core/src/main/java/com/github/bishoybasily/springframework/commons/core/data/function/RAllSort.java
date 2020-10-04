package com.github.bishoybasily.springframework.commons.core.data.function;

import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;

/**
 * @author bishoybasily
 * @since 3/15/20
 */
@FunctionalInterface
public interface RAllSort<T, R> {

    Flux<T> find(R r, Sort sort);

}
