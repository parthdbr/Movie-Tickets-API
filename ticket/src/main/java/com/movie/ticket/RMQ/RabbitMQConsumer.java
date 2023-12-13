package com.movie.ticket.RMQ;

import com.movie.ticket.DTO.EmailDTO;
import com.movie.ticket.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Service
@Slf4j
public class RabbitMQConsumer {

    @Autowired
    EmailService emailService;

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public <T> void consume(@Payload EmailDTO<T> emailDTO) throws IOException, MessagingException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException {

//        log.info(String.format("Received message -> %s", message));

        emailService.sendEmail(emailDTO);

    }
}
