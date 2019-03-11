package com.onions.timer.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import qunar.tc.qmq.Message;
import qunar.tc.qmq.consumer.annotation.EnableQmq;

@Slf4j
@Configuration
@Component
@PropertySource("classpath:application.yml")
@EnableQmq(appCode="${qmq.app-code}", metaServer="${qmq.meta-server-address}/meta/address")
public class QmqConsumer {
    @qunar.tc.qmq.consumer.annotation.QmqConsumer(subject = "timer", consumerGroup = "timer", executor = "qmqExecutor")
    public void onMessage(Message message){
        //process your message
        String value = message.getLargeString("data");
        log.info("消费消息成功：", value);
    }
}
