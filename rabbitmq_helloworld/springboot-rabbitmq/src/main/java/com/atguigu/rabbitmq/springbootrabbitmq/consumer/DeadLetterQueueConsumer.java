package com.atguigu.rabbitmq.springbootrabbitmq.consumer;




import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author hxld
 * @create 2022-08-27 8:22
 */

import java.util.Date;
@Slf4j
@Component
public class DeadLetterQueueConsumer {
    //接收消息
    @RabbitListener(queues = "QD")
    public void receiveD(Message message){
        String msg = new String(message.getBody());
        log.info("当前时间:{}，收到死信队列的消息:{}",new Date().toString(),msg);
    }

}