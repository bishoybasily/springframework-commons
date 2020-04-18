package com.github.bishoybasily.springframework.commons.core.data.function;

import org.springframework.data.domain.Pageable;

/**
 * @author bishoybasily
 * @since 3/15/20
 */
@FunctionalInterface
public interface AllPage<T> {

    Iterable<T> findAll(Pageable pageable);

}
