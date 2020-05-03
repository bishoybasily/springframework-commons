package com.github.bishoybasily.springframework.commons.jpa.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.bishoybasily.springframework.commons.core.utils.JsonUtils;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.List;

public class ConverterIntegerList implements AttributeConverter<List<Integer>, String> {

    @Override
    public String convertToDatabaseColumn(List<Integer> value) {
        if (value == null)
            return null;
        return JsonUtils.json(value);
    }

    @Override
    public List<Integer> convertToEntityAttribute(String value) {
        if (value == null)
            return null;
        return JsonUtils.json(value, new TypeReference<ArrayList<Integer>>() {
        });
    }

}
