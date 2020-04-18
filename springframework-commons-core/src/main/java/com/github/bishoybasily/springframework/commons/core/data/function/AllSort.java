package com.github.bishoybasily.springframework.commons.core.data.function;

import org.springframework.data.domain.Sort;

/**
 * @author bishoybasily
 * @since 3/15/20
 */
@FunctionalInterface
public interface AllSort<T> {

    Iterable<T> findAll(Sort sort);

}
