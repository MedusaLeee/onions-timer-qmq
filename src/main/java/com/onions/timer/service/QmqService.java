package com.onions.timer.service;

import com.onions.timer.dto.MessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import qunar.tc.qmq.Message;

public interface QmqService {

    DeferredResult<ResponseEntity<Object>> sendDelayAsyncMessage(MessageDto messageDto);

    void handleMessage(Message message);
}
