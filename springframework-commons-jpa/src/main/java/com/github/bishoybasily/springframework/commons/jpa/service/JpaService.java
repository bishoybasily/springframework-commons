package com.github.bishoybasily.springframework.commons.jpa.service;

import com.github.bishoybasily.springframework.commons.core.data.Updatable;
import com.github.bishoybasily.springframework.commons.core.data.params.Params;
import com.github.bishoybasily.springframework.commons.core.data.request.CollectionRequest;
import com.github.bishoybasily.springframework.commons.core.utils.ReactiveUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The base interface for any jpa based service class,
 * Responsible for handling the basic create, delete and filtered read operations genetically
 * and exposing a reactive interface for these operations,
 * {@link #getJpaRepository()} must be implemented to provide a {@link JpaRepository} implementation,
 * If there is a "find all" filtration criteria needs to be handled,
 * both {@link #getJpaSpecificationExecutor()} and {@link #getSpecification(Optional)},
 * must be implemented.
 *
 * @author bishoybasily
 * @since 2/22/20
 */
public interface JpaService<E extends Updatable<E>, I extends Serializable, P extends Params> {

    /**
     * Fetches single entity by id, and wraps it in a reactive {@link Mono} wrapper,
     * if no entity found, the exception supplied by {@link #supplyNotFoundException(Serializable)} )} will be thrown
     *
     * @param i the id to find the entity
     * @return the found entity
     */
    default Mono<E> one(I i) {
        return Mono.fromCallable(() -> {
            return getJpaRepository().findById(i).orElseThrow(supplyNotFoundException(i));
        }).map(retrieveCleaner());
    }

    /**
     * Retrieves all the records as reactive {@link Flux},
     * filtered based on the passed params
     *
     * @param p an object that extends {@link Params} to use as filter
     * @return the matched records
     */
    default Flux<E> all(P p) {
        return ReactiveUtils.toFlux(() -> createCollectionRequest(p).find()).map(retrieveCleaner());
    }

    /**
     * Retrieves Page/Slice of the data as {@link Mono} of {@link Page} according to the passed pagination params,
     * throws {@link IllegalArgumentException} if pagination params are not presented
     *
     * @param p
     * @return
     */
    default Mono<Page<E>> slice(P p) {
        return ReactiveUtils.toMono(() -> createCollectionRequest(p).slice().map(retrieveCleaner()));
    }

    default Mono<Long> count(P p) {
        return ReactiveUtils.toMono(() -> createCollectionRequest(p).count());
    }

    default CollectionRequest<E, P, Specification<E>> createCollectionRequest(P p) {

        final var optionalParams = Optional.ofNullable(p);

        return new CollectionRequest<E, P, Specification<E>>()

                .setOptionalParams(optionalParams)

                .setAll(getJpaRepository()::findAll)
                .setAllSort(getJpaRepository()::findAll)
                .setAllPage(getJpaRepository()::findAll)
                .setCount(getJpaRepository()::count)

                .setOptionalSpecification(getSpecification(optionalParams))

                .setSpecAll(getJpaSpecificationExecutor()::findAll)
                .setSpecAllSort(getJpaSpecificationExecutor()::findAll)
                .setSpecAllPage(getJpaSpecificationExecutor()::findAll)
                .setSpecCount(getJpaSpecificationExecutor()::count);

    }

    default Mono<E> preCreate(E e) {
        return Mono.just(e);
    }

    /**
     * Persists single entity
     *
     * @param e the entity to be persisted
     * @return the persisted entity wrapped in a reactive {@link Mono}
     */
    default Mono<E> create(E e) {
        return preCreate(e)
                .flatMap(this::persist)
                .flatMap(this::postCreate)
                .map(persistCleaner());
    }

    default Mono<E> postCreate(E e) {
        return Mono.just(e);
    }

    /**
     * Prepares the update version of the entity,
     * This could be useful in scenarios where some initialization logic has to be executed to prepare the update payload like populating some properties in the payload
     *
     * @param e the update payload
     * @return the update payload after preparation wrapped in a reactive {@link Mono}
     */
    default Mono<E> preUpdate(E e) {
        return Mono.just(e);
    }

    /**
     * Updates single entity by id
     *
     * @param i the id of the targeted entity
     * @param e the new update object
     * @return the updated entity after overriding the presented properties, the update implementation is dependent on the entity business use cases which means that some attrs may not be overrideable
     */
    default Mono<E> update(I i, E e) {
        return one(i)
                .zipWith(preUpdate(e), Updatable::update)
                .flatMap(this::persist)
                .flatMap(this::postUpdate)
                .map(persistCleaner());
    }

    /**
     * Can be used to execute some logic after updating this entity, can be used for auditing purposes
     *
     * @param e the updated entity after persisting it
     * @return the same passed entity
     */
    default Mono<E> postUpdate(E e) {
        return Mono.just(e);
    }

    default Mono<E> persist(E e) {
        return Mono.fromCallable(() -> {
                    return getJpaRepository().save(e);
                })
                .map(persistCleaner());
    }

    /**
     * Deletes entity by id
     *
     * @param i the id to be used for deletion
     * @return the deleted entity wrapped in a reactive {@link Mono} in case of some undo logic needs to be implemented
     */
    default Mono<E> delete(I i) {
        return Mono.fromCallable(() -> {
                    E e = getJpaRepository().findById(i).orElseThrow(supplyNotFoundException(i));
                    getJpaRepository().delete(e);
                    return e;
                })
                .flatMap(this::postDelete)
                .map(retrieveCleaner())
                .onErrorMap(mapCanNotDeleteException());
    }

    default Mono<E> delete(E e) {
        return Mono.fromCallable(() -> {
                    getJpaRepository().delete(e);
                    return e;
                })
                .flatMap(this::postDelete)
                .map(retrieveCleaner())
                .onErrorMap(mapCanNotDeleteException());
    }

    default Flux<E> delete(I[] is) {
        return Flux.fromArray(is)
                .flatMap(this::delete)
                .map(retrieveCleaner());
    }

    /**
     * Can be used to execute some logic after deleting this entity, can be used for auditing purposes
     *
     * @param e the deleted entity after removing it
     * @return the same passed entity
     */
    default Mono<E> postDelete(E e) {
        return Mono.just(e);
    }

    /**
     * Maps the exception that will be thrown in case of failing to delete this entity
     *
     * @return an {@link Function} implementation for mapping the throwable
     */
    default Function<Throwable, Throwable> mapCanNotDeleteException() {
        return throwable -> new RuntimeException();
    }

    /**
     * Supplies an exception to be thrown in case of entity wasn't found
     *
     * @param i the id that originally passed to find or delete this entity
     * @return an {@link Supplier} implementation for customizing the exception
     */
    default Supplier<RuntimeException> supplyNotFoundException(I i) {
        return RuntimeException::new;
    }

    /**
     * @return a {@link JpaRepository} implementation for the specified entity
     */
    JpaRepository<E, I> getJpaRepository();

    /**
     * @return a {@link JpaSpecificationExecutor} implementation for the specified entity
     * which is ideally the same instance that will be returned from {@link #getJpaRepository()} after extending {@link JpaSpecificationExecutor}
     */
    JpaSpecificationExecutor<E> getJpaSpecificationExecutor();

    /**
     * @param p the specified filtration object
     * @return a {@link Specification} implementation to use while retrieving all records
     */
    default Optional<Specification<E>> getSpecification(Optional<P> p) {
        return Optional.empty();
    }

    default Function<E, E> retrieveCleaner() {
        return it -> it;
    }

    default Function<E, E> persistCleaner() {
        return it -> it;
    }

}
