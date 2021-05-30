package com.github.bishoybasily.springframework.commons.core.utils;

import java.util.StringJoiner;
import java.util.stream.Stream;

public class StringUtils {

    public static String prefixedJoiner(String prefix, StringJoiner joiner) {
        if (joiner.length() > 0)
            return prefix + joiner;
        return "";
    }

    public static String join(String delimiter, String[] args) {
        StringJoiner joiner = new StringJoiner(delimiter);
        Stream.of(args).forEach(joiner::add);
        return joiner.toString();
    }

}
