package com.github.bishoybasily.springframework.commons.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;

public class JsonUtils {

    private final static ObjectMapper MAPPER = new ObjectMapper();

    static {

        MAPPER
                .setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    }

    @SneakyThrows
    public static <T> T json(String json, Class<T> type) {
        return MAPPER.readValue(json, type);
    }

    @SneakyThrows
    public static <T> T json(String json, TypeReference<T> type) {
        return MAPPER.readValue(json, type);
    }

    @SneakyThrows
    public static String json(Object object) {
        return MAPPER.writeValueAsString(object);
    }

}
