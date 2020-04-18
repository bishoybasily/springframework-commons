package com.github.bishoybasily.springframework.commons.core.data.params;

import com.google.common.collect.Streams;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bishoybasily
 * @since 3/15/20
 */
@Getter
@Setter
public class Params {

    private Integer page;
    private Integer count;
    private List<String> sort = new ArrayList<>();
    private List<String> direction = new ArrayList<>();

    public static Params empty() {
        return new Params();
    }

    public boolean isPaginationPresented() {
        return !ObjectUtils.isEmpty(page) && !ObjectUtils.isEmpty(count);
    }

    public boolean isSortPresented() {
        return !ObjectUtils.isEmpty(sort) && !ObjectUtils.isEmpty(direction);
    }

    public Pageable pageable() {
        if (isSortPresented())
            return PageRequest.of(page, count, sort());
        return PageRequest.of(page, count);
    }

    public Sort sort() {
        return Sort.by(
                Streams.zip(
                        sort.stream(),
                        direction.stream(),
                        (prop, dir) -> new Sort.Order(Sort.Direction.fromString(dir), prop)
                ).collect(Collectors.toList())
        );
    }

}
