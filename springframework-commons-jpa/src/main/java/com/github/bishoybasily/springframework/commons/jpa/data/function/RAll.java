package com.github.bishoybasily.springframework.commons.jpa.data.function;

/**
 * @author bishoybasily
 * @since 3/15/20
 */
@FunctionalInterface
public interface RAll<T, R> {

    Iterable<T> findAll(R r);

}
