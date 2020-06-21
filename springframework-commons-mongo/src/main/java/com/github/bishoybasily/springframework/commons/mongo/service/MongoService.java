package com.github.bishoybasily.springframework.commons.mongo.service;

import com.github.bishoybasily.springframework.commons.core.data.params.Params;
import com.github.bishoybasily.springframework.commons.core.data.request.CollectionRequest;
import com.github.bishoybasily.springframework.commons.mongo.repository.MongoRepository;
import com.google.common.annotations.Beta;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Supplier;

@Beta
public interface MongoService<E, I extends Serializable, P extends Params> {

    default Mono<E> one(I i) {
        return getMongoRepository().findById(i).switchIfEmpty(Mono.error(supplyNotFoundException(i)));
    }

    default Flux<E> all(P p) {
        return new CollectionRequest<E>(p)
                .setAll(() -> getMongoRepository().findAll())
                .setAllSort(sort -> getMongoRepository().findAll(sort))
                .setAllPage(pageable -> getMongoRepository().findAll(pageable))
                .find();
    }

    default Mono<E> save(E e) {
        return getMongoRepository().save(e);
    }

    default Flux<E> save(Iterable<E> es) {
        return getMongoRepository().saveAll(es);
    }

    default Mono<E> delete(I i) {
        return one(i).flatMap(it -> getMongoRepository().delete(it).map(res -> it)).onErrorMap(mapCanNotDeleteException());
    }

    default Function<Throwable, Throwable> mapCanNotDeleteException() {
        return throwable -> new RuntimeException();
    }

    default Supplier<RuntimeException> supplyNotFoundException(I i) {
        return RuntimeException::new;
    }

    MongoRepository<E, I> getMongoRepository();

}
