package com.github.bishoybasily.springframework.commons.core.data.request;

import com.github.bishoybasily.springframework.commons.core.data.function.SpecificationAll;
import com.github.bishoybasily.springframework.commons.core.data.function.SpecificationAllPage;
import com.github.bishoybasily.springframework.commons.core.data.function.SpecificationAllSort;
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

  protected final Supplier<S> specificationSupplier;

  protected SpecificationAll<T, S> specificationAll = (s) -> {
    throw new UnsupportedOperationException();
  };
  protected SpecificationAllSort<T, S> specificationAllSort = (s, sort) -> {
    throw new UnsupportedOperationException();
  };
  protected SpecificationAllPage<T, S> specificationAllPage = (s, sort) -> {
    throw new UnsupportedOperationException();
  };

  public SpecificationCollectionRequest(Params params, Supplier<S> specificationSupplier) {
    super(params);
    this.specificationSupplier = specificationSupplier;
  }

  /**
   * It uses the same approach that {@link CollectionRequest#find()} uses but with a new restriction,
   * The new restriction is a generic object that can be supplied through this {@link #specificationSupplier},
   * If the supplier doesn't return null then this method will try to determine which function to execute
   * based on the specified params along with the new restriction,
   * If the supplier returns null then the super impl. will be called as a fallback.
   *
   * @return the result of the executed function
   */
  @Override
  public Iterable<T> find() {

    S s = specificationSupplier.get();

    if (ObjectUtils.isEmpty(s))
      return super.find();

    if (params.isPaginationPresented()) {
      return specificationAllPage.find(s, params.pageable());
    } else {
      if (params.isSortPresented()) {
        return specificationAllSort.find(s, params.sort());
      } else {
        return specificationAll.find(s);
      }
    }

  }

}
