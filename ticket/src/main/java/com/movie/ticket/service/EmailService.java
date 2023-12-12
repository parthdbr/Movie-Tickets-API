package com.movie.ticket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import com.movie.ticket.DTO.EmailDTO;
import com.movie.ticket.model.Templates;
import com.movie.ticket.repository.EmailDescCriteriaRepository;
import com.movie.ticket.repository.UserRepository;
import com.samskivert.mustache.Mustache;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.StringWriter;

import static org.springframework.util.ResourceUtils.getFile;

@Service
@Slf4j
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailDescCriteriaRepository emailDescCriteriaRepository;

    @Value("${spring.mail.username}") private String sender;

    public void sendEmail(EmailDTO emailDTO) throws MessagingException, FileNotFoundException {



//        MustacheFactory mf = new DefaultMustacheFactory();
//        com.github.mustachejava.Mustache m = mf.compile("templates/userRegister.html");

        String template = emailDescCriteriaRepository.findTemplateBySubject(emailDTO.getSubject());


        StringWriter sw = new StringWriter();
        String messageText = "";
        Mustache.compiler().compile(template).execute(emailDTO, sw);
        messageText=sw.toString();

//        try{
//            m.execute(sw, emailDTO).flush();
//            messageText=sw.toString();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("parthdbr@gmail.com");
        helper.setSubject(emailDTO.getSubject());
        helper.setText(messageText,true);
        mailSender.send(message);
    }

}
