package com.bk.olympia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;

import javax.persistence.*;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;

@Controller
public abstract class BaseController {
    @Autowired
    private SimpMessagingTemplate template;

    protected EntityManagerFactory factory = Persistence.createEntityManagerFactory("App");
    protected EntityManager entityManager = factory.createEntityManager();
    protected Query query;

    protected ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    protected HashMap<Integer, ScheduledFuture> taskQueue = new HashMap<>();
}
