package com.github.bishoybasily.springframework.commons.core.utils;

import com.github.bishoybasily.springframework.commons.core.data.model.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapUtils {

    public static Map<String, Object> flatten(String root, Map<String, ?> data) {
        return visit(data, new Node(root), new ArrayList<>()).stream().collect(Collectors.toMap(Node::getPath, Node::getValue));
    }

    private static List<Node> visit(Map<String, ?> map, Node parent, List<Node> nodes) {
        map.forEach((k, v) -> {
            if (v instanceof Map) visit((Map) v, new Node(k).setParent(parent), nodes);
            else nodes.add(new Node(k).setParent(parent).setValue(v));
        });
        return nodes;
    }

}
