package com.onions.timer.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qunar.tc.qmq.Message;
import qunar.tc.qmq.MessageProducer;
import qunar.tc.qmq.MessageSendStateListener;
import com.onions.timer.service.QmqService;
import qunar.tc.qmq.consumer.annotation.QmqConsumer;

import java.util.Date;

@Service
@Slf4j
public class QmqServiceImpl implements QmqService {
    @Autowired
    MessageProducer producer;

    @Override
    public void sendMessage(String msg) {
        Message message = producer.generateMessage("timer");
        //QMQ提供的Message是key/value的形式
        message.setProperty("data", msg);
        producer.sendMessage(message, new MessageSendStateListener() {
            @Override
            public void onSuccess(Message message) {
                log.debug("发送消息成功：", message);
            }

            @Override
            public void onFailed(Message message) {
                log.error("发送消息失败：", message);
            }
        });
    }

    @Override
    public void sendDelayMessage(String msg, Date date) {
        Message message = producer.generateMessage("timer");
        // QMQ提供的Message是key/value的形式
        message.setProperty("data", msg);
        message.setProperty("delayDate", date);
        //指定发送时间
        message.setDelayTime(date);
        producer.sendMessage(message, new MessageSendStateListener() {
            @Override
            public void onSuccess(Message message) {
                log.debug("发送定时消息成功：", message);
            }

            @Override
            public void onFailed(Message message) {
                log.error("发送定时消息失败：", message);
            }
        });
    }

    @Override
    @QmqConsumer(subject = "timer", consumerGroup = "timer", executor = "qmqExecutor")
    public void handleMessage(Message message) {
        String value = message.getLargeString("data");
        Date delayDate = message.getDateProperty("delayDate");
        if (delayDate == null) {
            log.info("消费成功: " + value + ",diff：0 ms");
            return;
        }
        Date nowDate = new Date();
        Long diff = nowDate.getTime() - delayDate.getTime();
        log.info("消费成功: " + value + ",diff：" + Integer.toString(diff.intValue()) + " ms");
    }
}
