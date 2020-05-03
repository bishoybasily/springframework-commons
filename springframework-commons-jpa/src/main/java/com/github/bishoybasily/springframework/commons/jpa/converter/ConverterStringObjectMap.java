package com.github.bishoybasily.springframework.commons.jpa.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.bishoybasily.springframework.commons.core.utils.JsonUtils;

import javax.persistence.AttributeConverter;
import java.util.Map;

public class ConverterStringObjectMap implements AttributeConverter<Map<String, Object>, String> {

    @Override
    public String convertToDatabaseColumn(Map<String, Object> value) {
        if (value == null)
            return null;
        return JsonUtils.json(value);
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String value) {
        if (value == null)
            return null;
        return JsonUtils.json(value, new TypeReference<Map<String, Object>>() {
        });
    }

}
