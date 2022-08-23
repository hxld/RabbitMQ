package com.atguigu.rabbitmq.two;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author hxld
 * @create 2022-08-23 21:33
 */
public class Wroker01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收到的消息："+message.getBody());
        };

        CancelCallback cancelCallback = consumerTag  -> {
            System.out.println(consumerTag +"消息失败回调");
        };


        System.out.println("c2等待接收消息");

        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);


    }
}
