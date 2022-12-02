package com.ennova.pubinfowebsite.controller;

import cn.hutool.core.lang.UUID;
import com.ennova.pubinfowebsite.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/confirm")
@Slf4j
public class ProductSendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("sendMessage")
    public void sendMessage(String message) {
        //指定消息 id 为 1
        CorrelationData correlationData1 = new CorrelationData("1");
        String routingKey = "key1";
        Message msg = MessageBuilder.withBody(message.getBytes()).setMessageId(UUID.randomUUID() + "").build();
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME, routingKey, msg + routingKey, correlationData1);

        //指定消息 id 为 2
        CorrelationData correlationData2 = new CorrelationData("2");
        routingKey = "key2";
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME, routingKey + "2", message + routingKey, correlationData2);

        log.info("发送消息内容:{}", message);
    }


}
