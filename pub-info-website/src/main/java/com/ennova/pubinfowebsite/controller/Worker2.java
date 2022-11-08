package com.ennova.pubinfowebsite.controller;

import com.ennova.pubinfowebsite.utils.RabbitMqUtils;
import com.ennova.pubinfowebsite.utils.SleepUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

public class Worker2 {
    private static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("Worker2等待接收消息处理");

        boolean autoAck = false;

        channel.basicConsume(TASK_QUEUE_NAME, autoAck, ((consumerTag, message) -> {
            SleepUtils.sleep(30);
            System.out.println("接收到的消息为：" + new String(message.getBody(), "UTF-8").getBytes(StandardCharsets.UTF_8));
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }), consumerTag -> {
            System.out.println("消费者取消消费接口回调逻辑");
        });

    }
}
