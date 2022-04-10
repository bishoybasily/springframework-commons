package com.github.bishoybasily.springframework.commons.core.data.function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author bishoybasily
 * @since 3/15/20
 */
@FunctionalInterface
public interface SpecificationAllPage<T, S> {

    Page<T> find(S s, Pageable pageable);

}
