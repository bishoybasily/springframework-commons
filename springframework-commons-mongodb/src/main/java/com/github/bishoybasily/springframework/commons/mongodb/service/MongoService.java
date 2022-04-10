package com.github.bishoybasily.springframework.commons.mongodb.service;

import com.github.bishoybasily.springframework.commons.core.data.Updatable;
import com.github.bishoybasily.springframework.commons.core.data.params.Params;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author bishoybasily
 * @since 2022-04-10
 */
public interface MongoService<E extends Updatable<E>, I extends Serializable, P extends Params> {

    /**
     * @return a {@link ReactiveMongoRepository} implementation for the specified entity
     */
    ReactiveMongoRepository<E, I> getMongoRepository();

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
                .map(cleaner());
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
        return getMongoRepository()
                .findById(i)
                .zipWith(preUpdate(e), Updatable::update)
                .flatMap(this::persist)
                .flatMap(this::postUpdate)
                .map(cleaner());
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
        return getMongoRepository()
                .save(e)
                .map(cleaner());
    }

    default Function<E, E> cleaner() {
        return it -> it;
    }

}
