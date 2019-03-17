package com.onions.timer.service;

import org.springframework.web.context.request.async.DeferredResult;
import qunar.tc.qmq.Message;

import java.util.Date;

public interface QmqService {
    void sendMessage(String msg);

    void sendDelayMessage(String msg, Date date);

    void sendDelayTransMessage(String msg, Date date);

    DeferredResult<String> sendDelayAsyncMessage(String msg, Date date);

    void handleMessage(Message message);
}
