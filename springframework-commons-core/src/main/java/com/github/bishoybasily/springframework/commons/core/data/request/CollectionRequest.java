package com.github.bishoybasily.springframework.commons.core.data.request;

import com.github.bishoybasily.springframework.commons.core.data.function.*;
import com.github.bishoybasily.springframework.commons.core.data.params.Params;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;

import java.util.Optional;

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
public class CollectionRequest<T, P extends Params, S> {

    private Optional<P> optionalParams = Optional.empty();
    private All<T> all = () -> {
        throw new UnsupportedOperationException();
    };
    private AllSort<T> allSort = (sort) -> {
        throw new UnsupportedOperationException();
    };
    private AllPage<T> allPage = (pageable) -> {
        throw new UnsupportedOperationException();
    };
    private Count count = () -> {
        throw new UnsupportedOperationException();
    };
    private Optional<S> optionalSpecification = Optional.empty();
    private SpecificationAll<T, S> specAll = (s) -> {
        throw new UnsupportedOperationException();
    };
    private SpecificationAllSort<T, S> specAllSort = (s, sort) -> {
        throw new UnsupportedOperationException();
    };
    private SpecificationAllPage<T, S> specAllPage = (s, sort) -> {
        throw new UnsupportedOperationException();
    };
    private SpecificationCount<S> specCount = (s) -> {
        throw new UnsupportedOperationException();
    };

    /**
     * Determines the which function to execute based on the specified params,
     * If a pagination is requested then {@link #allPage} will be executed,
     * If no pagination and only sort is passed then {@link #allSort} will be executed,
     * If neither a pagination nor sort is requested then {@link #all} will be executed
     *
     * @return the result of the executed function
     */
    public Iterable<T> find() {
        return optionalParams.map(params -> {
                    return optionalSpecification.map(spec -> {
                                if (params.isPaginationPresented()) {
                                    return specAllPage.find(spec, params.pageable());
                                } else {
                                    if (params.isSortPresented()) {
                                        return specAllSort.find(spec, params.sort());
                                    } else {
                                        return specAll.find(spec);
                                    }
                                }
                            })
                            .orElseGet(() -> {
                                if (params.isPaginationPresented()) {
                                    return allPage.find(params.pageable());
                                } else {
                                    if (params.isSortPresented()) {
                                        return allSort.find(params.sort());
                                    } else {
                                        return all.find();
                                    }
                                }
                            });
                })
                .orElseGet(() -> all.find());
    }

    public Page<T> slice() {

        final var paginationParamsMissing = new IllegalArgumentException("Pagination params can't be empty");

        return optionalParams.map(params -> {
                    return optionalSpecification.map(spec -> {
                                if (params.isPaginationPresented())
                                    return specAllPage.find(spec, params.pageable());
                                throw paginationParamsMissing;
                            })
                            .orElseGet(() -> {
                                if (params.isPaginationPresented())
                                    return allPage.find(params.pageable());
                                throw paginationParamsMissing;
                            });
                })
                .orElseThrow(() -> paginationParamsMissing);

    }

    public Long count() {
        return optionalSpecification.map(spec -> specCount.count(spec))
                .orElseGet(() -> count.count());
    }

}
