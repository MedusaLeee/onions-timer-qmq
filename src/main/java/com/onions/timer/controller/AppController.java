package com.onions.timer.controller;

import com.onions.timer.dto.AddAppForm;
import com.onions.timer.exception.ParamsInvalidException;
import com.onions.timer.model.App;
import com.onions.timer.repository.AppRepository;
import com.onions.timer.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/apps")
public class AppController {
    @Autowired
    private AppService appService;

    @PostMapping(path = "")
    public App addApp(@RequestBody AddAppForm form) {
        App app = appService.findByAppId(form.getAppId());
        if (app != null) {
            throw new ParamsInvalidException(400, "AppId已存在");
        }
        app = appService.addApp(form);
        return app;
    }
}
