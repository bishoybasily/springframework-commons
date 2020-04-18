package com.github.bishoybasily.springframework.commons.core.appender;

import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bishoybasily
 * @since 3/18/20
 */
@RequiredArgsConstructor
public class ObjectAppender<T> {

    private final Appender<T> appender;

    private final List<T> list = new ArrayList<>();

    public ObjectAppender<T> append(T t) {
        list.add(t);
        return this;
    }

    public T build() {

        T result = null;
        for (T next : list) {
            if (ObjectUtils.isEmpty(result)) {
                result = next;
            } else {
                result = appender.append(result, next);
            }
        }

        return result;
    }

}
