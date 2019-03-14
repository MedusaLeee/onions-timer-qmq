package com.onions.timer.controller;

import com.onions.timer.service.QmqService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;

@RestController
public class TimerController {
    @Autowired
    private QmqService qmqService;

    @GetMapping(path = "/default")
    public String sendMessage() {
        qmqService.sendMessage("message: " + System.currentTimeMillis());
        return "ok";
    }

    @GetMapping(path = "/delay")
    public ResponseEntity<String> sendDelayMessage(String time) {
        Date date;
        try {
            date = DateUtils.parseDate(time, "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            return new ResponseEntity<>("日期格式错误", HttpStatus.BAD_REQUEST);
        }
        qmqService.sendDelayMessage("message time: " + time, date);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
