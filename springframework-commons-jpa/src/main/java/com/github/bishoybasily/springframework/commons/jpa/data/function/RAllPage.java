package com.github.bishoybasily.springframework.commons.jpa.data.function;

import org.springframework.data.domain.Pageable;

/**
 * @author bishoybasily
 * @since 3/15/20
 */
@FunctionalInterface
public interface RAllPage<T, R> {

    Iterable<T> findAll(R r, Pageable pageable);

}
