package com.kvedro.bin.mailsender.messageHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvedro.bin.mailsender.models.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JmsHandler {
    @Value(value = "${queue.name}")
    private String queue;

    @Value(value = "${queue.errorName}")
    private String errorQueue;

    private final ApplicationContext context;
    private final ObjectMapper mapper = new ObjectMapper();



    @Autowired
    public JmsHandler(ApplicationContext context) {
        this.context = context;
    }

    public void sendToJms(Email email) {
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        jmsTemplate.convertAndSend(queue ,asString(email));
    }

    private String asString(Email email){
        try{
            return mapper.writeValueAsString(email);
        }catch(Exception e){
            log.error("Hello email conversion failed: {}", e);
            return "";
        }
    }

    public void sendToJmsErrorQueue(String email) {
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        jmsTemplate.convertAndSend(errorQueue, email);
    }
}
