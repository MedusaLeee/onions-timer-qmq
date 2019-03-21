package com.onions.timer.controller;

import com.onions.timer.dto.MessageDto;
import com.onions.timer.service.QmqService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@RestController
public class TimerController {
    @Autowired
    private QmqService qmqService;

    //    // 压力测试用的
//    @GetMapping(path = "/messages")
//    public DeferredResult<String> sendDelayAsyncMessage(String time, int index) {
//        Date date;
//        try {
//            date = DateUtils.parseDate(time, "yyyy-MM-dd HH:mm:ss");
//        } catch (ParseException e) {
//            throw new Error("日期格式错误");
//        }
//        return qmqService.sendDelayAsyncMessage("message index: " + index + ", time: " + time, date);
//    }
    @PostMapping(path = "/messages")
    public DeferredResult<ResponseEntity<Object>> addDelayMessage(@RequestBody MessageDto messageDto) {
        return qmqService.sendDelayAsyncMessage(messageDto);
    }
}
