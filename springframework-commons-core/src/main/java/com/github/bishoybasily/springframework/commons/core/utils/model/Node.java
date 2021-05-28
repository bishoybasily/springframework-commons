package com.github.bishoybasily.springframework.commons.core.utils.model;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.ObjectUtils;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class Node {

    private final String key;
    private Object value;
    private Node parent;

    public String getPath() {

        Node p = parent;
        String path = "";

        while (p != null) {
            path = String.format("%s.%s", p.getKey(), ObjectUtils.isEmpty(path) ? key : path);
            p = p.parent;
        }

        return path;

    }

}