package com.github.bishoybasily.springframework.commons.core.data.function;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

/**
 * @author bishoybasily
 * @since 3/15/20
 */
@FunctionalInterface
public interface AllPage<T> {

    Flux<T> findAll(Pageable pageable);

}
