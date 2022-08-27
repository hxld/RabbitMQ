package com.atguigu.rabbitmq.springbootrabbitmq.consumer;

import com.atguigu.rabbitmq.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author hxld
 * @create 2022-08-27 17:32
 */
@Slf4j
@Component
public class WarningConsumer {
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveMessageWarning(Message message){
        String msg = new String(message.getBody());
        log.error("报警发现不可路由消息：{}",msg);
    }
}
