package com.onions.timer.service;

import qunar.tc.qmq.Message;

public interface QmqService {
    public void sendMessage(String message);
    public void handleMessage(Message message);
}
