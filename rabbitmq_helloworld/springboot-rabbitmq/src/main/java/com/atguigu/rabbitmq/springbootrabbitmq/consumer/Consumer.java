package com.atguigu.rabbitmq.springbootrabbitmq.consumer;

import com.atguigu.rabbitmq.springbootrabbitmq.config.ConfirmConfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author hxld
 * @create 2022-08-27 15:24
 */
@Slf4j
@Component
public class Consumer {
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveConfigMessage(Message message){
        String s = new String(message.getBody());
        log.info("接受到的消息是：{}" + s);
    }
}
