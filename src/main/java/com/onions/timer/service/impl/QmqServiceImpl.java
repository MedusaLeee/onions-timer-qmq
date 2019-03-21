package com.onions.timer.service.impl;

import com.alibaba.fastjson.JSON;
import com.onions.timer.dto.MessageDto;
import com.onions.timer.exception.ParamsInvalidException;
import com.onions.timer.model.App;
import com.onions.timer.repository.AppRepository;
import com.onions.timer.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;
import qunar.tc.qmq.Message;
import qunar.tc.qmq.MessageProducer;
import qunar.tc.qmq.MessageSendStateListener;
import com.onions.timer.service.QmqService;
import qunar.tc.qmq.consumer.annotation.QmqConsumer;

import javax.transaction.Transactional;
import java.text.ParseException;
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
    public DeferredResult<ResponseEntity<Object>> sendDelayAsyncMessage(MessageDto messageDto) {
        DeferredResult<ResponseEntity<Object>> result = new DeferredResult<>(10 * 1000L);
        Message message = producer.generateMessage("timer");
        messageDto.setTime(System.currentTimeMillis());
        String jsonStr = JSON.toJSONString(messageDto);
        // QMQ提供的Message是key/value的形式
        message.setProperty("data", jsonStr);
        //指定发送时间
        Date delayDate;
        try {
            delayDate = DateUtils.parseDate(messageDto.getJobAt(), "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            throw new ParamsInvalidException(400, "jobAt格式错误");
        }
        message.setDelayTime(delayDate);
        result.onTimeout(() -> result.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)));
        producer.sendMessage(message, new MessageSendStateListener() {
            @Override
            public void onSuccess(Message message) {
                log.debug("发送定时事物消息成功：", message);
                result.setResult(new ResponseEntity<>(HttpStatus.OK));
            }

            @Override
            public void onFailed(Message message) {
                log.error("发送定时事物消息失败：", message);
                ErrorResponse errorResponse = new ErrorResponse(500, "发送消息失败");
                result.setErrorResult(new ResponseEntity<Object>(errorResponse, HttpStatus.OK));
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
