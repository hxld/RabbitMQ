package com.atguigu.rabbitmq.springbootrabbitmq.consumer;


import com.atguigu.rabbitmq.springbootrabbitmq.config.DelayedQueueConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class DelayedQueueConsumer {
    //接收消息
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void receiveDelayed(Message message){
        String msg = new String(message.getBody());
        log.info("当前时间:{}，收到延迟队列的消息:{}",new Date().toString(),msg);
    }

}