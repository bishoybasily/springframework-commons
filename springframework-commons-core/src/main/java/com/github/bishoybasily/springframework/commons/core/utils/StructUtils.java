package com.github.bishoybasily.springframework.commons.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bishoybasily
 * @since 2020-05-29
 */
public class StructUtils {

    public static Map merge(Map m1, Map map2) {
        Map map1 = new HashMap(m1);
        for (Object key : map2.keySet()) {
            Object value2 = map2.get(key);
            if (map1.containsKey(key)) {
                Object value1 = map1.get(key);
                if (value1 instanceof Map && value2 instanceof Map) {
                    merge((Map) value1, (Map) value2);
                } else if (value1 instanceof List && value2 instanceof List) {
                    map1.put(key, merge((List) value1, (List) value2));
                } else {
                    map1.put(key, value2);
                }
            } else {
                map1.put(key, value2);
            }
        }
        return map1;
    }

    public static List merge(List l1, List l2) {
        List list1 = new ArrayList(l1);
        List list2 = new ArrayList(l2);
        list2.removeAll(list1);
        list1.addAll(list2);
        return list1;
    }

}
