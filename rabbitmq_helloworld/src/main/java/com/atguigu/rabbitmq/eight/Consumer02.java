package com.atguigu.rabbitmq.eight;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hxld
 * @create 2022-08-25 21:43
 */
public class Consumer02 {
    //死信交换机及死信队列
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("等待接收消息.....");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println( "c2接收的消息是："+ new String(message.getBody(),"UTF-8"));
        };
        CancelCallback cancelCallback = consumerTag -> {
        };
       channel.basicConsume(DEAD_QUEUE,false,deliverCallback,cancelCallback);
    }
}
