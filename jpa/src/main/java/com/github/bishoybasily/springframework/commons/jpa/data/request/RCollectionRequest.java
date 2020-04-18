package com.github.bishoybasily.springframework.commons.jpa.data.request;

import com.github.bishoybasily.springframework.commons.jpa.data.function.RAll;
import com.github.bishoybasily.springframework.commons.jpa.data.function.RAllPage;
import com.github.bishoybasily.springframework.commons.jpa.data.function.RAllSort;
import com.github.bishoybasily.springframework.commons.jpa.data.params.Params;
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

	@Override
	public Iterable<T> find() {

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
