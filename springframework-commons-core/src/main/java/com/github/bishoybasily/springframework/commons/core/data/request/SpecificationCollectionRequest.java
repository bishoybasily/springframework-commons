package com.github.bishoybasily.springframework.commons.core.data.request;

import com.github.bishoybasily.springframework.commons.core.data.function.SpecificationAll;
import com.github.bishoybasily.springframework.commons.core.data.function.SpecificationAllPage;
import com.github.bishoybasily.springframework.commons.core.data.function.SpecificationAllSort;
import com.github.bishoybasily.springframework.commons.core.data.function.SpecificationCount;
import com.github.bishoybasily.springframework.commons.core.data.params.Params;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.util.ObjectUtils;

import java.util.function.Supplier;

/**
 * @author bishoybasily
 * @since 3/15/20
 */
@Setter
@Accessors(chain = true)
public class SpecificationCollectionRequest<T, S> extends CollectionRequest<T> {

    protected final Supplier<S> specSupplier;

    protected SpecificationAll<T, S> specAll = (s) -> {
        throw new UnsupportedOperationException();
    };
    protected SpecificationAllSort<T, S> specAllSort = (s, sort) -> {
        throw new UnsupportedOperationException();
    };
    protected SpecificationAllPage<T, S> specAllPage = (s, sort) -> {
        throw new UnsupportedOperationException();
    };

    protected SpecificationCount<S> specCount = (s) -> {
        throw new UnsupportedOperationException();
    };

    public SpecificationCollectionRequest(Params params, Supplier<S> specSupplier) {
        super(params);
        this.specSupplier = specSupplier;
    }

    /**
     * It uses the same approach that {@link CollectionRequest#find()} uses but with a new restriction,
     * The new restriction is a generic object that can be supplied through this {@link #specSupplier},
     * If the supplier doesn't return null then this method will try to determine which function to execute
     * based on the specified params along with the new restriction,
     * If the supplier returns null then the super impl. will be called as a fallback.
     *
     * @return the result of the executed function
     */
    @Override
    public Iterable<T> find() {

        S spec = specSupplier.get();

        if (ObjectUtils.isEmpty(spec))
            return super.find();

        if (params.isPaginationPresented()) {
            return specAllPage.find(spec, params.pageable());
        } else {
            if (params.isSortPresented()) {
                return specAllSort.find(spec, params.sort());
            } else {
                return specAll.find(spec);
            }
        }

    }

    @Override
    public Long count() {

        S spec = specSupplier.get();

        if (ObjectUtils.isEmpty(spec))
            return super.count();

        return specCount.count(spec);

    }
}
