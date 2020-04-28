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
 * The base interface for any jpa based service class,
 * Responsible for handling the basic create, delete and filtered read operations genetically
 * and exposing a reactive interface for these operations,
 * {@link #getJpaRepository()} must be implemented to provide a @{@link JpaRepository} implementation,
 * If there is a "find all" filtration criteria needs to be handled,
 * both {@link #getJpaSpecificationExecutor()} and {@link #getSpecification(P)},
 * must be implemented.
 *
 * @author bishoybasily
 * @since 2/22/20
 */
public interface JpaService<E, I extends Serializable, P extends Params> {

    /**
     * Fetches single entity by id, and wraps it in a reactive @{@link Mono} wrapper,
     * if no entity found, the exception supplied by {@link #supplyNotFoundException(I)} will be thrown
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
        return Mono.fromCallable(() -> {
            return new CollectionRequest<E>(p)
                    .setAll(() -> getJpaRepository().findAll())
                    .setAllSort(sort -> getJpaRepository().findAll(sort))
                    .setAllPage(pageable -> getJpaRepository().findAll(pageable))
                    .r(() -> getSpecification(p))
                    .setRAll(spec -> getJpaSpecificationExecutor().findAll(spec))
                    .setRAllSort((spec, sort) -> getJpaSpecificationExecutor().findAll(spec, sort))
                    .setRAllPage((spec, pageable) -> getJpaSpecificationExecutor().findAll(spec, pageable))
                    .find();
        }).flatMapIterable(it -> it);
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
     * which is ideally the same instance that will be returned from {@link #getJpaRepository()} after extending @{@link JpaSpecificationExecutor}
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
