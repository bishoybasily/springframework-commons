package com.github.bishoybasily.springframework.commons.jpa.appender;

/**
 * @author bishoybasily
 * @since 3/18/20
 */
@FunctionalInterface
public interface Appender<T> {

	T append(T t1, T t2);

}
