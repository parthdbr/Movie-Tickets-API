package com.movie.ticket.service;

import com.movie.ticket.DTO.EmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}") private String sender;

    public void sendEmail(EmailDTO emailDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailDTO.getFrom());
        message.setCc(emailDTO.getCc());
        message.setBcc(emailDTO.getBcc());
        message.setReplyTo(emailDTO.getReplyTo());
        message.setTo(emailDTO.getTo());
        message.setSubject(emailDTO.getSubject());
        message.setText(emailDTO.getText());
        mailSender.send(message);
    }

    public EmailDTO setMailData(String[] to,String subject, String msg) {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setFrom(sender);
        emailDTO.setCc("ccmail@yopmail.com");
        emailDTO.setBcc("bccmail@yopmail.com");
        emailDTO.setReplyTo("parthdbr@gmail.com");
        emailDTO.setTo(to);
        emailDTO.setSubject(subject);
        emailDTO.setText(msg);

        return emailDTO;
    }
}
