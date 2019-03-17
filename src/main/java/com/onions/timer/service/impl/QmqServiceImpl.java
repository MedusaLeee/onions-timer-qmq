package com.onions.timer.service.impl;

import com.onions.timer.model.App;
import com.onions.timer.repository.AppRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;
import qunar.tc.qmq.Message;
import qunar.tc.qmq.MessageProducer;
import qunar.tc.qmq.MessageSendStateListener;
import com.onions.timer.service.QmqService;
import qunar.tc.qmq.consumer.annotation.QmqConsumer;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@Slf4j
@Transactional
                        public class QmqServiceImpl implements QmqService {
    @Autowired
    private MessageProducer producer;

    @Autowired
    private AppRepository appRepository;

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
    public void sendDelayTransMessage(String msg, Date date) {
        Message message = producer.generateMessage("timer");
        // QMQ提供的Message是key/value的形式
        message.setProperty("data", msg);
        message.setProperty("delayDate", date);
        //指定发送时间
        message.setDelayTime(date);
        producer.sendMessage(message, new MessageSendStateListener() {
            @Override
            public void onSuccess(Message message) {
                log.debug("发送定时事物消息成功：", message);
            }

            @Override
            public void onFailed(Message message) {
                log.error("发送定时事物消息失败：", message);
            }
        });
        App app = new App();
        app.setApp_name("trans_app2");
        app.setAppId("trans_app_id2");
        app.setAppSecret("trans_app_secret2");
        appRepository.save(app);
        throw new RuntimeException("手动抛异常");
    }

    @Override
    public DeferredResult<String> sendDelayAsyncMessage(String msg, Date date) {
        DeferredResult<String> result = new DeferredResult<>(10 * 1000L);
        Message message = producer.generateMessage("timer");
        // QMQ提供的Message是key/value的形式
        message.setProperty("data", msg);
        message.setProperty("delayDate", date);
        //指定发送时间
        message.setDelayTime(date);
        producer.sendMessage(message, new MessageSendStateListener() {
            @Override
            public void onSuccess(Message message) {
                log.debug("发送定时事物消息成功：", message);
                // 延迟测试
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                result.setResult("success");
            }

            @Override
            public void onFailed(Message message) {
                log.error("发送定时事物消息失败：", message);
                result.setErrorResult("failed");
            }
        });
        return result;
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
