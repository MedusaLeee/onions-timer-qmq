package com.onions.timer.controller;

import com.onions.timer.service.QmqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TimerController {
    @Autowired
    private QmqService qmqService;

    @GetMapping(path = "/")
    @ResponseBody
    public String sendMessage() {
        qmqService.sendMessage("message: " + System.currentTimeMillis());
        return "ok";
    }
}
