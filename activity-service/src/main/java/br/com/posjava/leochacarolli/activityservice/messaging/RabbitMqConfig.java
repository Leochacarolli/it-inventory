package br.com.posjava.leochacarolli.activity_service.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE = "asset.exchange";
    public static final String QUEUE = "asset.activity.queue";
    public static final String ROUTING_KEY = "asset.created";

    @Bean
    public DirectExchange assetExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue assetActivityQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Binding assetActivityBinding(
            Queue assetActivityQueue,
            DirectExchange assetExchange) {

        return BindingBuilder
                .bind(assetActivityQueue)
                .to(assetExchange)
                .with(ROUTING_KEY);
    }
}