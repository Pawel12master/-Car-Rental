package com.example.biznes.service;

import com.example.biznes.controller.ReservationController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Properties;


@Service
public class EmailService {
    private static final Logger logger = LogManager.getLogger(ReservationController.class);
    @Autowired
    private JavaMailSender emailSender;

    @Async
    public void sendSimpleMessage(String to, String subject, String text) throws InterruptedException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("javabiznesmail@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        logger.debug("Wysyłanie wiadomości do użytkownika " + to);
        Thread.sleep(500);
        emailSender.send(message);
        logger.debug("Wysłano wiadomość do " + to);
    }


}
