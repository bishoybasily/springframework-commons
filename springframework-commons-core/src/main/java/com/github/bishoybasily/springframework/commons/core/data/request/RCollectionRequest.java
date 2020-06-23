package com.github.bishoybasily.springframework.commons.core.data.request;

import com.github.bishoybasily.springframework.commons.core.data.function.RAll;
import com.github.bishoybasily.springframework.commons.core.data.function.RAllPage;
import com.github.bishoybasily.springframework.commons.core.data.function.RAllSort;
import com.github.bishoybasily.springframework.commons.core.data.params.Params;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

/**
 *
 * @author bishoybasily
 * @since 3/15/20
 */
@Setter
@Accessors(chain = true)
public class RCollectionRequest<T, R> extends CollectionRequest<T> {

    protected final Supplier<R> rSupplier;

    protected RAll<T, R> rAll = (r) -> {
        throw new UnsupportedOperationException();
    };
    protected RAllSort<T, R> rAllSort = (r, sort) -> {
        throw new UnsupportedOperationException();
    };
    protected RAllPage<T, R> rAllPage = (r, sort) -> {
        throw new UnsupportedOperationException();
    };

    public RCollectionRequest(Params params, Supplier<R> rSupplier) {
        super(params);
        this.rSupplier = rSupplier;
    }

    /**
     * It uses the same approach that {@link CollectionRequest#find()} uses but with a new restriction,
     * The new restriction is a generic object that can be supplied through this {@link #rSupplier},
     * If the supplier doesn't return null then this method will try to determine which function to execute
     * based on the specified params along with the new restriction,
     * If the supplier returns null then the super impl. will be called as a fallback.
     *
     * @return the result of the executed function
     */
    @Override
    public Flux<T> find() {

        R r = rSupplier.get();

        if (ObjectUtils.isEmpty(r))
            return super.find();

        if (params.isPaginationPresented()) {
            return rAllPage.findAll(r, params.pageable());
        } else {
            if (params.isSortPresented()) {
                return rAllSort.findAll(r, params.sort());
            } else {
                return rAll.findAll(r);
            }
        }

    }

}
