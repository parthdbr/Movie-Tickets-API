package com.movie.ticket.service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.movie.ticket.DTO.EmailDTO;
import com.movie.ticket.model.Templates;
import com.movie.ticket.repository.EmailDescCriteriaRepository;
import com.movie.ticket.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailDescCriteriaRepository emailDescCriteriaRepository;

    @Value("${spring.mail.username}") private String sender;

    public void userRegisterSendEmail(EmailDTO emailDTO) throws MessagingException {

        String subject = "New User Registered";

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache m = mf.compile("templates/userRegister.html");
//        Mustache m  = mf.compile(emailDescCriteriaRepository.findTemplateBySubject(subject));
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
        helper.setTo("parthdbr@gmail.com");
        helper.setSubject(subject);
        helper.setText(messageText,true);
        mailSender.send(message);
    }

}
