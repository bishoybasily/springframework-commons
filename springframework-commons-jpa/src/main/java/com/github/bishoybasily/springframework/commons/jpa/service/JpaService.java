package com.github.bishoybasily.springframework.commons.jpa.service;

import com.github.bishoybasily.springframework.commons.core.data.Updatable;
import com.github.bishoybasily.springframework.commons.core.data.params.Params;
import com.github.bishoybasily.springframework.commons.core.data.request.CollectionRequest;
import com.github.bishoybasily.springframework.commons.core.data.request.RCollectionRequest;
import com.github.bishoybasily.springframework.commons.core.utils.ReactiveUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The base interface for any jpa based service class,
 * Responsible for handling the basic create, delete and filtered read operations genetically
 * and exposing a reactive interface for these operations,
 * {@link JpaService#getJpaRepository()} must be implemented to provide a {@link JpaRepository} implementation,
 * If there is a "find all" filtration criteria needs to be handled,
 * both {@link JpaService#getJpaSpecificationExecutor()} and {@link JpaService#getSpecification(P)},
 * must be implemented.
 *
 * @author bishoybasily
 * @since 2/22/20
 */
public interface JpaService<E extends Updatable<E>, I extends Serializable, P extends Params> {

    /**
     * Fetches single entity by id, and wraps it in a reactive @{@link Mono} wrapper,
     * if no entity found, the exception supplied by {@link JpaService#supplyNotFoundException(I)} will be thrown
     *
     * @param i the id to find the entity
     * @return the found entity
     */
    default Mono<E> one(I i) {
        return Mono.fromCallable(() -> {
            return getJpaRepository().findById(i).orElseThrow(supplyNotFoundException(i));
        });
    }

    /**
     * Retrieves all of the records as reactive @{@link Flux},
     * filtered based on the passed params
     *
     * @param p an object that extends @{@link Params} to use as filter
     * @return the matched records
     */
    default Flux<E> all(P p) {
        return createCollectionRequest(p).find();
    }

    default RCollectionRequest<E, Specification<E>> createCollectionRequest(P p) {
        return new CollectionRequest<E>(p)
                .setAll(() -> ReactiveUtils.toFlux(() -> getJpaRepository().findAll()))
                .setAllSort(sort -> ReactiveUtils.toFlux(() -> getJpaRepository().findAll(sort)))
                .setAllPage(pageable -> ReactiveUtils.toFlux(() -> getJpaRepository().findAll(pageable)))
                .r(() -> getSpecification(p))
                .setRAll(spec -> ReactiveUtils.toFlux(() -> getJpaSpecificationExecutor().findAll(spec)))
                .setRAllSort((spec, sort) -> ReactiveUtils.toFlux(() -> getJpaSpecificationExecutor().findAll(spec, sort)))
                .setRAllPage((spec, pageable) -> ReactiveUtils.toFlux(() -> getJpaSpecificationExecutor().findAll(spec, pageable)));
    }

    /**
     * Persists single entity
     *
     * @param e the entity to be persisted
     * @return the persisted entity wrapped in a reactive @{@link Mono}
     */
    default Mono<E> save(E e) {
        return Mono.fromCallable(() -> {
            return getJpaRepository().save(e);
        });
    }

    /**
     * Persists multiple entities
     *
     * @param es the entities to be persisted
     * @return the persisted entities wrapped in a reactive @{@link Flux}
     */
    default Flux<E> save(Iterable<E> es) {
        return Mono.fromCallable(() -> {
            return getJpaRepository().saveAll(es);
        }).flatMapIterable(it -> it);
    }

    /**
     * Updates single entity by id
     *
     * @param i the id of the targeted entity
     * @param e the new update object
     * @return the updated entity after overriding the presented properties, the update implementation is dependent on the entity business use cases which means that some attrs may not be overrideable
     */
    default Mono<E> update(I i, E e) {
        return one(i).map(it -> it.update(e)).flatMap(this::save);
    }

    /**
     * Deletes entity by id
     *
     * @param i the id to be used for deletion
     * @return the deleted entity wrapped in a reactive @{@link Mono} in case of some undo logic needs to be implemented
     */
    default Mono<E> delete(I i) {
        return Mono.fromCallable(() -> {
            E e = getJpaRepository().findById(i).orElseThrow(supplyNotFoundException(i));
            getJpaRepository().delete(e);
            return e;
        }).onErrorMap(mapCanNotDeleteException());
    }

    /**
     * Maps the exception that will be thrown in case of failing to delete this entity
     *
     * @return an @{@link Function} implementation for mapping the throwable
     */
    default Function<Throwable, Throwable> mapCanNotDeleteException() {
        return throwable -> new RuntimeException();
    }

    /**
     * Supplies an exception to be thrown in case of entity wasn't found
     *
     * @param i the id that originally passed to find or delete this entity
     * @return an @{@link Supplier} implementation for customizing the exception
     */
    default Supplier<RuntimeException> supplyNotFoundException(I i) {
        return RuntimeException::new;
    }

    /**
     * @return a {@link JpaRepository} implementation for the specified entity
     */
    JpaRepository<E, I> getJpaRepository();

    /**
     * @return a @{@link JpaSpecificationExecutor} implementation for the specified entity
     * which is ideally the same instance that will be returned from {@link JpaService#getJpaRepository()} after extending @{@link JpaSpecificationExecutor}
     */
    default JpaSpecificationExecutor<E> getJpaSpecificationExecutor() {
        return null;
    }

    /**
     * @param p the specified filtration object
     * @return a @{@link Specification} implementation to use while retrieving all records
     */
    default Specification<E> getSpecification(P p) {
        return null;
    }

}
