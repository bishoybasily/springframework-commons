package com.github.bishoybasily.springframework.commons.core.data;

/**
 * @author bishoybasily
 * @since 2020-06-21
 */
public interface Updatable<T> {

    default T update(T t) {
        return t;
    }

}
