package com.movie.ticket.RMQ;

import com.movie.ticket.DTO.EmailDTO;
import com.movie.ticket.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQConsumer {

    @Autowired
    EmailService emailService;

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consume(@Payload EmailDTO emailDTO){

//        log.info(String.format("Received message -> %s", message));

        emailService.sendEmail(emailDTO);

    }
}
