package br.com.posjava.leochacarolli.activity_service.messaging;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class AssetCreatedConsumer {

    @RabbitListener(queues = RabbitMqConfig.QUEUE)
    public void consume(Message message) {

        String body = new String(
                message.getBody(),
                StandardCharsets.UTF_8
        );

        System.out.println("--------------------------------");
        System.out.println("Evento ASSET_CREATED recebido");
        System.out.println(body);
        System.out.println("--------------------------------");
    }
}