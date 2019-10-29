package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringEventService {
    @Autowired
    private static ApplicationEventPublisher applicationEventPublisher;

    public static void publishEvent(ApplicationEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
