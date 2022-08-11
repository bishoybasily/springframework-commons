package com.github.bishoybasily.springframework.commons.jpa.service;

import com.github.bishoybasily.springframework.commons.core.data.Updatable;
import com.github.bishoybasily.springframework.commons.core.data.params.Params;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bishoybasily
 * @since 2022-05-27
 */
class JpaServiceTest {

    @Test
    void all() {

        @AllArgsConstructor
        @EqualsAndHashCode(of = "value")
        class Entity implements Updatable<Entity> {
            private final String value;
        }

        JpaRepository<Entity, String> jpaRepository = mock(JpaRepository.class);
        when(jpaRepository.findAll()).thenReturn(List.of(new Entity("all")));
        when(jpaRepository.findAll(any(Sort.class))).thenReturn(List.of(new Entity("all sort")));
        when(jpaRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(new Entity("all page"))));

        JpaSpecificationExecutor<Entity> jpaSpecificationExecutor = mock(JpaSpecificationExecutor.class);
        when(jpaSpecificationExecutor.findAll(any(Specification.class))).thenReturn(List.of(new Entity("spec all")));
        when(jpaSpecificationExecutor.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of(new Entity("spec all sort")));
        when(jpaSpecificationExecutor.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(new Entity("spec all page"))));

        final var jpaService = new JpaService<Entity, String, Params>() {
            @Override
            public JpaRepository<Entity, String> getJpaRepository() {
                return jpaRepository;
            }

            @Override
            public JpaSpecificationExecutor<Entity> getJpaSpecificationExecutor() {
                return jpaSpecificationExecutor;
            }
        };

        // null params - should return all
        StepVerifier.create(jpaService.all(null))
                .expectNextMatches(it -> it.equals(new Entity("all")))
                .verifyComplete();

        // empty params - should return all
        StepVerifier.create(jpaService.all(new Params()))
                .expectNextMatches(it -> it.equals(new Entity("all")))
                .verifyComplete();

        // non-empty, incomplete sort params - should return all
        StepVerifier.create(jpaService.all(new Params().setSort(List.of("abc"))))
                .expectNextMatches(it -> it.equals(new Entity("all")))
                .verifyComplete();

        // non-empty, incomplete pagination params - should return all
        StepVerifier.create(jpaService.all(new Params().setPage(0)))
                .expectNextMatches(it -> it.equals(new Entity("all")))
                .verifyComplete();

        // non-empty, complete sort params - should return all sort
        StepVerifier.create(jpaService.all(new Params().setSort(List.of("abc")).setDirection(List.of("asc"))))
                .expectNextMatches(it -> it.equals(new Entity("all sort")))
                .verifyComplete();

        // non-empty, complete page params - should return all page
        StepVerifier.create(jpaService.all(new Params().setPage(0).setCount(5)))
                .expectNextMatches(it -> it.equals(new Entity("all page")))
                .verifyComplete();

        final var jpaServiceWithSpecification = new JpaService<Entity, String, Params>() {
            @Override
            public JpaRepository<Entity, String> getJpaRepository() {
                return jpaRepository;
            }

            @Override
            public JpaSpecificationExecutor<Entity> getJpaSpecificationExecutor() {
                return jpaSpecificationExecutor;
            }

            @Override
            public Optional<Specification<Entity>> getSpecification(Optional<Params> params) {
                // empty specification from empty params
                return params.flatMap(p -> new ArrayList<Specification<Entity>>().stream().reduce(Specification::and));
            }
        };

        // null params - should return all
        StepVerifier.create(jpaServiceWithSpecification.all(null))
                .expectNextMatches(it -> it.equals(new Entity("all")))
                .verifyComplete();

        // empty params - should return all spec
        StepVerifier.create(jpaServiceWithSpecification.all(new Params()))
                .expectNextMatches(it -> it.equals(new Entity("spec all")))
                .verifyComplete();

        // non-empty, incomplete pagination params - should return all spec
        StepVerifier.create(jpaServiceWithSpecification.all(new Params().setPage(0)))
                .expectNextMatches(it -> it.equals(new Entity("spec all")))
                .verifyComplete();

        // non-empty, complete sort params - should return all sort spec
        StepVerifier.create(jpaServiceWithSpecification.all(new Params().setSort(List.of("abc")).setDirection(List.of("asc"))))
                .expectNextMatches(it -> it.equals(new Entity("spec all sort")))
                .verifyComplete();

        // non-empty, complete page params - should return all page spec
        StepVerifier.create(jpaServiceWithSpecification.all(new Params().setPage(0).setCount(5)))
                .expectNextMatches(it -> it.equals(new Entity("spec all page")))
                .verifyComplete();

    }

}