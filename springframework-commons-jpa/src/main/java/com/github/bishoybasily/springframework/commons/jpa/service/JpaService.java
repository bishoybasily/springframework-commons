package com.github.bishoybasily.springframework.commons.jpa.service;

import com.github.bishoybasily.springframework.commons.core.data.params.Params;
import com.github.bishoybasily.springframework.commons.core.data.request.CollectionRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author bishoybasily
 * @since 2/22/20
 */
public interface JpaService<E, I extends Serializable, P extends Params> {

    default Mono<E> one(I i) {
        return Mono.fromCallable(() -> {
            return getJpaRepository().findById(i).orElseThrow(supplyNotFoundException(i));
        });
    }

    default Flux<E> all(P p) {
        return Mono.fromCallable(() -> {
            return new CollectionRequest<E>(p)
                    .setAll(getJpaRepository()::findAll)
                    .setAllSort(getJpaRepository()::findAll)
                    .setAllPage(getJpaRepository()::findAll)
                    .r(() -> getSpecification(p))
                    .setRAll(getJpaSpecificationExecutor()::findAll)
                    .setRAllSort(getJpaSpecificationExecutor()::findAll)
                    .setRAllPage(getJpaSpecificationExecutor()::findAll)
                    .find();
        }).flatMapIterable(it -> it);
    }

    default Mono<E> save(E e) {
        return Mono.fromCallable(() -> {
            return getJpaRepository().save(e);
        });
    }

    default Flux<E> save(Iterable<E> es) {
        return Mono.fromCallable(() -> {
            return getJpaRepository().saveAll(es);
        }).flatMapIterable(it -> it);
    }

    default Mono<E> delete(I i) {
        return Mono.fromCallable(() -> {
            E e = getJpaRepository().findById(i).orElseThrow(supplyNotFoundException(i));
            getJpaRepository().delete(e);
            return e;
        }).onErrorMap(mapCanNotDeleteException());
    }

    default Function<Throwable, Throwable> mapCanNotDeleteException() {
        return throwable -> new RuntimeException();
    }

    default Supplier<RuntimeException> supplyNotFoundException(I i) {
        return RuntimeException::new;
    }

    JpaRepository<E, I> getJpaRepository();

    default JpaSpecificationExecutor<E> getJpaSpecificationExecutor() {
        return null;
    }

    default Specification<E> getSpecification(P p) {
        return null;
    }

}
