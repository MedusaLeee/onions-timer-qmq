package com.onions.timer.controller;

import com.onions.timer.service.QmqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TimerController {
    @Autowired
    private QmqService qmqService;

    @GetMapping(path = "/")
    public String sendMessage() {
        qmqService.sendMessage("message: " + System.currentTimeMillis());
        return "ok";
    }
}
