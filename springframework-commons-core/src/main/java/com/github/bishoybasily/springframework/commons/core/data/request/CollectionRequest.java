package com.github.bishoybasily.springframework.commons.core.data.request;

import com.github.bishoybasily.springframework.commons.core.data.function.All;
import com.github.bishoybasily.springframework.commons.core.data.function.AllPage;
import com.github.bishoybasily.springframework.commons.core.data.function.AllSort;
import com.github.bishoybasily.springframework.commons.core.data.function.Count;
import com.github.bishoybasily.springframework.commons.core.data.params.Params;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;
import org.springframework.util.ObjectUtils;

import java.util.function.Supplier;

/**
 * A generic Collection Request object that can be populated with the common cases for fetching any entity,
 * entities can be fetched with a specific sort or a specific offset and count or all of them,
 * based on the specified params, an appropriate function will be called which should have been set beforehand
 *
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

    protected Count count = () -> {
        throw new UnsupportedOperationException();
    };

    public CollectionRequest(Params params) {
        this.params = params;
    }

    /**
     * Determines the which function to execute based on the specified params,
     * If a pagination is requested then {@link #allPage} will be executed,
     * If no pagination and only sort is passed then {@link #allSort} will be executed,
     * If neither a pagination nor sort is requested then {@link #all} will be executed
     *
     * @return the result of the executed function
     */
    public Iterable<T> find() {

        if (ObjectUtils.isEmpty(params))
            return all.find();

        if (params.isPaginationPresented()) {
            return allPage.find(params.pageable());
        } else {
            if (params.isSortPresented()) {
                return allSort.find(params.sort());
            } else {
                return all.find();
            }
        }

    }

    public Page<T> slice() {

        if (ObjectUtils.isEmpty(params) || !params.isPaginationPresented())
            throw new IllegalArgumentException("Pagination params can't be empty");

        return allPage.find(params.pageable());

    }

    public Long count() {
        return count.count();
    }

    /**
     * Constructs a new instance of {@link SpecificationCollectionRequest} in a fluent style,
     * with all the super type variables populated
     *
     * @param specSupplier
     * @param <R>
     * @return
     */
    public <R> SpecificationCollectionRequest<T, R> spec(Supplier<R> specSupplier) {

        SpecificationCollectionRequest<T, R> specRequest = new SpecificationCollectionRequest<>(params, specSupplier);

        specRequest
                .setAll(all)
                .setAllSort(allSort)
                .setAllPage(allPage);

        return specRequest;
    }

}
