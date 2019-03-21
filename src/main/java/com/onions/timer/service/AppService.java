package com.onions.timer.service;

import com.onions.timer.dto.AddAppForm;
import com.onions.timer.model.App;

public interface AppService {
    App addApp(AddAppForm form);

    App findByAppId(String appId);
}
