package com.movie.ticket.service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import com.movie.ticket.DTO.EmailDTO;
import com.movie.ticket.model.AdminConfig;
import com.movie.ticket.model.User;
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

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

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

    public <T> void sendEmail(EmailDTO<T> emailDTO) throws MessagingException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IOException {

        String keyVal = emailDTO.getKey();

        String template = emailDescCriteriaRepository.findTemplateBySubject(emailDTO.getSubject(), emailDTO.getKey());

        StringWriter sw = new StringWriter();
        String messageText = "";

        if (template == null ){
            MustacheFactory mf = new DefaultMustacheFactory();
            com.github.mustachejava.Mustache m = mf.compile("templates/"+emailDTO.getKey()+".html");
            m.execute(sw, emailDTO.getSomeDTO()).flush();
        }else {
            Mustache.compiler().compile(template).execute(emailDTO.getSomeDTO(), sw);
        }

        messageText = sw.toString();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("parthdbr@gmail.com");
        helper.setSubject(emailDTO.getSubject());
        helper.setText(messageText,true);
        mailSender.send(message);
    }

}
