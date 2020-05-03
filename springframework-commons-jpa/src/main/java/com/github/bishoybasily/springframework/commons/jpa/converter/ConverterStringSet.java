package com.github.bishoybasily.springframework.commons.jpa.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.bishoybasily.springframework.commons.core.utils.JsonUtils;

import javax.persistence.AttributeConverter;
import java.util.Set;

public class ConverterStringSet implements AttributeConverter<Set<String>, String> {

    @Override
    public String convertToDatabaseColumn(Set<String> value) {
        if (value == null)
            return null;
        return JsonUtils.json(value);
    }

    @Override
    public Set<String> convertToEntityAttribute(String value) {
        if (value == null)
            return null;
        return JsonUtils.json(value, new TypeReference<Set<String>>() {
        });
    }

}
