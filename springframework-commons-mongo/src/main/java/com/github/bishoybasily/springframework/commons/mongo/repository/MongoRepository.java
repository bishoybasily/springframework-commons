package com.github.bishoybasily.springframework.commons.mongo.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;


/**
 * @author bishoybasily
 * @since 2020-06-21
 */
public interface MongoRepository<T, ID> extends ReactiveMongoRepository<T, ID> {

    Flux<T> findAll(final Pageable page);

}
