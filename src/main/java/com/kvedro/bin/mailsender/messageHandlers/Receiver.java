package com.kvedro.bin.mailsender.messageHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvedro.bin.mailsender.models.Email;
import com.kvedro.bin.mailsender.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Receiver {

    @Value("queue.name")
    private String queue;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JmsHandler jmsHandler;

    private ObjectMapper mapper = new ObjectMapper();

    @JmsListener(destination = "${}", containerFactory = "myFactory")
    public void receiveMessage(String sEmail) {
        try {
            Email email = mapper.readValue(sEmail, Email.class);
            emailService.sendSimpleMessage(email.getTo(), email.getToppic(), email.getBody());
        } catch (IOException e) {
            jmsHandler.sendToJmsErrorQueue(sEmail);
        }
    }


}
