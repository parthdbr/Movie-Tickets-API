package com.movie.ticket.service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.movie.ticket.DTO.EmailDTO;
import com.movie.ticket.model.User;
import com.movie.ticket.repository.UserRepository;
import jakarta.annotation.Resource;
import jakarta.annotation.Resources;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.*;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    UserRepository userRepository;

    @Value("${spring.mail.username}") private String sender;

    public void sendEmail(EmailDTO emailDTO) throws MessagingException {

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache m = mf.compile("templates/mailTemplate.html");
        StringWriter sw = new StringWriter();
        String messageText = "";

        try{
            m.execute(sw, emailDTO).flush();
            messageText=sw.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

//        message.setFrom(emailDTO.getFrom());
//        message.setCc(emailDTO.getCc());
//        message.setBcc(emailDTO.getBcc());
//        message.setReplyTo(emailDTO.getReplyTo());

        helper.setTo(emailDTO.getEmail());
        helper.setSubject(emailDTO.getSubject());
        helper.setText(messageText,true);
        mailSender.send(message);
    }

    public EmailDTO setMailData(String to,String subject, String msg) {

        User user = userRepository.findByEmailContainingAndSoftDeleteIsFalse(to);

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setFullName(user.getFirst_name()+" "+user.getLast_name());
        emailDTO.setFrom(sender);
        emailDTO.setCc("ccmail@yopmail.com");
        emailDTO.setBcc("bccmail@yopmail.com");
        emailDTO.setReplyTo("parthdbr@gmail.com");
        emailDTO.setEmail(to);
        emailDTO.setSubject(subject);
        emailDTO.setText(msg);

        return emailDTO;
    }
}
