package com.onions.timer.controller;

import com.onions.timer.model.App;
import com.onions.timer.repository.AppRepository;
import com.onions.timer.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apps")
public class AppController {

    @Autowired
    private AppRepository appRepository;
    @Autowired
    private AppService appService;

    @GetMapping(path = "")
    public List<App> getAll() {
        return appRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public App getAppById(@PathVariable Long id) {
        return appRepository.findById(id).orElse(new App());
    }

    @GetMapping(path = "/add")
    public String addApp() {
        appService.addApp();
        return "success";
    }
}
