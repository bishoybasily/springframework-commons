package com.github.bishoybasily.springframework.commons.core.data.request;

import com.github.bishoybasily.springframework.commons.core.data.function.All;
import com.github.bishoybasily.springframework.commons.core.data.function.AllPage;
import com.github.bishoybasily.springframework.commons.core.data.function.AllSort;
import com.github.bishoybasily.springframework.commons.core.data.params.Params;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.function.Supplier;

/**
 * @author bishoybasily
 * @since 3/15/20
 */
@Setter
@Accessors(chain = true)
public class CollectionRequest<T> {

    protected final Params params;

    protected All<T> all = () -> {
        throw new UnsupportedOperationException();
    };
    protected AllSort<T> allSort = (sort) -> {
        throw new UnsupportedOperationException();
    };
    protected AllPage<T> allPage = (pageable) -> {
        throw new UnsupportedOperationException();
    };

    public CollectionRequest(Params params) {
        this.params = params;
    }

    public Iterable<T> find() {

        if (params.isPaginationPresented()) {
            return allPage.findAll(params.pageable());
        } else {
            if (params.isSortPresented()) {
                return allSort.findAll(params.sort());
            } else {
                return all.findAll();
            }
        }
    }

    public <R> RCollectionRequest<T, R> r(Supplier<R> r1Supplier) {

        RCollectionRequest<T, R> rRequest = new RCollectionRequest<>(params, r1Supplier);

        rRequest
                .setAll(all)
                .setAllSort(allSort)
                .setAllPage(allPage);

        return rRequest;
    }

}
