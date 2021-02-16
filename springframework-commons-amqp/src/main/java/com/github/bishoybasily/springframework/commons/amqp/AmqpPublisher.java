package com.github.bishoybasily.springframework.commons.amqp;

import com.github.bishoybasily.springframework.commons.core.utils.ReactiveUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;

/**
 * @author bishoybasily
 * @since 2020-05-03
 */
public interface AmqpPublisher {

    RabbitTemplate getRabbitTemplate();

    default <T> Mono<T> publish(String exchange, String routingKey, T t) {
      return ReactiveUtils.toMono(() -> {
        getRabbitTemplate().convertAndSend(exchange, routingKey, t);
        return t;
      });
    }

    default <T, S> Mono<S> publishAndReceive(String exchange, String routingKey, T t, ParameterizedTypeReference<S> reference) {
      return ReactiveUtils.toMono(() -> {
        return getRabbitTemplate().convertSendAndReceiveAsType(exchange, routingKey, t, reference);
      });
    }

}
