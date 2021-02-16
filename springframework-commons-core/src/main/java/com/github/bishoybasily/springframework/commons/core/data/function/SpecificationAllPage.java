package com.github.bishoybasily.springframework.commons.core.data.function;

import org.springframework.data.domain.Pageable;

/**
 * @author bishoybasily
 * @since 3/15/20
 */
@FunctionalInterface
public interface SpecificationAllPage<T, S> {

  Iterable<T> find(S s, Pageable pageable);

}
