package com.onions.timer.dto;

import lombok.Data;

@Data
public class MessageDto {
    private String jobAt;
    private String appId;
    private String data;
    private Long time;
}
