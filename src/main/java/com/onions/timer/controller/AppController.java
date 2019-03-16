package com.onions.timer.controller;

import com.onions.timer.model.App;
import com.onions.timer.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apps")
public class AppController {

    @Autowired
    private AppRepository appRepository;

    @GetMapping(path = "")
    public List<App> getAll() {
        return appRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public App getAppById(@PathVariable Long id) {
        return appRepository.findById(id).orElse(new App());
    }

}
