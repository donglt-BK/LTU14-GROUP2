package com.bk.olympia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

@Controller
public abstract class BaseController {
    @Autowired
    private SimpMessagingTemplate template;

    protected EntityManagerFactory factory = Persistence.createEntityManagerFactory("App");
    protected EntityManager manager = factory.createEntityManager();
    protected Query query;
}
