package com.github.bishoybasily.springframework.commons.core.data.function;

/**
 * @author bishoybasily
 * @since 3/15/20
 */
@FunctionalInterface
public interface SpecificationAll<T, S> {

    Iterable<T> find(S s);

}
