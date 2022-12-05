package com.ennova.pubinfowebsite.controller;

import com.ennova.pubinfowebsite.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WarningConsumer {

    @RabbitListener(queues = ConfirmConfig.WARING_QUEUE_NAME)
    public void receiveMsg(Message message) {
        String msg = new String(message.getBody());
        log.error("接受到队列 waring.queue 消息:{}", msg);
    }
}