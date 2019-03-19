package com.onions.timer.service.impl;

import com.onions.timer.service.AppService;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class AppServiceImpl implements AppService {
    @Autowired
    private ConnectionFactory connectionFactory;
    @Override
    public void addApp() {
        try {
            DeclareOk declareOk = connectionFactory.createConnection().createChannel(false).queueDeclare("qmq-test", true, false, false, null);
            log.debug("declareOk: " + declareOk.getQueue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
