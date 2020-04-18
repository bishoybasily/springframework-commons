package com.github.bishoybasily.springframework.commons.jpa.data.request;

import com.github.bishoybasily.springframework.commons.jpa.data.function.All;
import com.github.bishoybasily.springframework.commons.jpa.data.function.AllPage;
import com.github.bishoybasily.springframework.commons.jpa.data.function.AllSort;
import com.github.bishoybasily.springframework.commons.jpa.data.params.Params;
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

    public <R1> RCollectionRequest<T, R1> r(Supplier<R1> r1Supplier) {

        RCollectionRequest<T, R1> rCollectionRequest = new RCollectionRequest<>(params, r1Supplier);

        rCollectionRequest
                .setAll(all)
                .setAllSort(allSort)
                .setAllPage(allPage);

        return rCollectionRequest;
    }

}
