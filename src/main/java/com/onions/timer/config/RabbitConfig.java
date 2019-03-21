package com.onions.timer.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public Channel adminChannel(@Autowired ConnectionFactory connectionFactory) {
        return connectionFactory.createConnection().createChannel(false);
    }
}
