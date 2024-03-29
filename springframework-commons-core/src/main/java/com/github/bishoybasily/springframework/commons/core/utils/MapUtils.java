package com.github.bishoybasily.springframework.commons.core.utils;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapUtils {

    public static Map<String, Object> flatten(String root, Map<String, ?> data) {
        return visit(data, new Node(root), new ArrayList<>()).stream().collect(Collectors.toMap(Node::getPath, Node::getValue));
    }

    private static List<Node> visit(Map<String, ?> map, Node parent, List<Node> nodes) {
        if (!ObjectUtils.isEmpty(map))
            map.forEach((k, v) -> {
                if (v instanceof Map) visit((Map) v, new Node(k).setParent(parent), nodes);
                else nodes.add(new Node(k).setParent(parent).setValue(v));
            });
        return nodes;
    }

    @Data
    @Accessors(chain = true)
    @RequiredArgsConstructor
    public static class Node {

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
}
