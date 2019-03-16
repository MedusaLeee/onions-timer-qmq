package com.onions.timer.service;

import qunar.tc.qmq.Message;

import java.util.Date;

public interface QmqService {
    void sendMessage(String msg);

    void sendDelayMessage(String msg, Date date);

    void sendDelayTransMessage(String msg, Date dat);

    void handleMessage(Message message);
}
