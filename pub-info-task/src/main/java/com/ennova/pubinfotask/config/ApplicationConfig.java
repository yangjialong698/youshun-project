package com.ennova.pubinfotask.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationConfig implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        webSocketServer.init();
    }
}

