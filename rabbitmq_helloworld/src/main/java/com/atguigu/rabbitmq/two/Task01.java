package com.atguigu.rabbitmq.two;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @author hxld
 * @create 2022-08-23 21:49
 */
public class Task01 {
    public static final String   QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //从控制台接收消息
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String message = scanner.next();

            /**
             * 发送到哪个交换机
             * 路由的key值是哪个，即本次队列的名称
             * 其他参数信息
             * 发送消息的消息体
             */
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("消息发送完成"+message);
        }
    }
}
