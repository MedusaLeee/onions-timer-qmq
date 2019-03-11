package com.onions.timer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;
import qunar.tc.qmq.MessageProducer;
import qunar.tc.qmq.producer.MessageProducerProvider;

@Slf4j
@Configuration
@PropertySource("classpath:application.yml")
public class QmqConfig {

    @Value("${qmq.app-code}")
    String qmqAppCode;

    @Value("${qmq.meta-server-address}")
    String qmqMetaServerAddress;

    @Bean
    public MessageProducer producer(){
        MessageProducerProvider producer = new MessageProducerProvider();
        producer.setAppCode(qmqAppCode);
        producer.setMetaServer(qmqMetaServerAddress + "/meta/address");
        // 默认每次发送时最大批量大小，默认30
        producer.setSendBatch(30);
        // 异步发送队列大小
        producer.setMaxQueueSize(10000);
        // 如果消息发送失败，重试次数，默认10
        producer.setSendTryCount(10);
        return producer;
    }

    @Bean
    public ThreadPoolExecutorFactoryBean qmqExecutor() {
        ThreadPoolExecutorFactoryBean executorFactoryBean = new ThreadPoolExecutorFactoryBean();
        executorFactoryBean.setCorePoolSize(2);
        executorFactoryBean.setMaxPoolSize(2);
        executorFactoryBean.setQueueCapacity(1000);
        executorFactoryBean.setThreadNamePrefix("qmq-process");
        return executorFactoryBean;
    }
}
