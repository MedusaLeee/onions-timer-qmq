package com.onions.timer.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_app")
public class App {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "app_id")
    private String appId;
    @Column(name = "app_secret")
    private String appSecret;
    @Column(name = "app_name")
    private String app_name;
    @Column(name = "consumer_queue")
    private String consumerQueue;

}
