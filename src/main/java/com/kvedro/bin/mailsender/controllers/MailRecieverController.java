package com.kvedro.bin.mailsender.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kvedro.bin.mailsender.messageHandlers.JmsHandler;
import com.kvedro.bin.mailsender.models.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@Slf4j
public class MailRecieverController {

    private final JmsHandler jmsHandler;

    @Autowired
    public MailRecieverController(JmsHandler jmsHandler) {
        this.jmsHandler = jmsHandler;
    }

    @PostMapping("/mail")
    public ResponseEntity<Email> queueMail(@RequestBody() Email email){
        jmsHandler.sendToJms(email);
        return ResponseEntity.ok(email);
    }

    @PostMapping("/mailbulk")
    public ResponseEntity<Email[]> queueMailBulk(@RequestBody() Email[] emails){
        Arrays.stream(emails).forEach(jmsHandler::sendToJms);
        return ResponseEntity.ok(emails);
    }
}
