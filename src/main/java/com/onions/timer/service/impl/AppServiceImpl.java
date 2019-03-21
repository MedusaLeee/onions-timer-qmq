package com.onions.timer.service.impl;

import com.onions.timer.dto.AddAppForm;
import com.onions.timer.model.App;
import com.onions.timer.rabbit.TimerProducer;
import com.onions.timer.repository.AppRepository;
import com.onions.timer.service.AppService;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;

@Slf4j
@Service
@Transactional
public class AppServiceImpl implements AppService {
    //    @Autowired
//    private ConnectionFactory connectionFactory;
//    @Autowired
//    private TimerProducer timerProducer;
    @Autowired
    @Qualifier("adminChannel")
    private Channel adminChannel;
    @Autowired
    private AppRepository appRepository;

    @Override
    public App addApp(AddAppForm form) {
        // 创建 app后创建队列
        App app = new App();
        app.setAppId(form.getAppId());
        app.setAppName(form.getAppName());
        app.setConsumerQueue("timer-" + form.getAppId());
        app = appRepository.save(app);
        try {
            DeclareOk declareOk = adminChannel.queueDeclare(app.getConsumerQueue(), true, false, false, null);
            log.debug("declareOk: " + declareOk.getQueue());
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
        return app;
    }

    @Override
    public App findByAppId(String appId) {
        return appRepository.findByAppId(appId);
    }
}
