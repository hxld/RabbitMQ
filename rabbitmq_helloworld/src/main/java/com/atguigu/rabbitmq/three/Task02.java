package com.atguigu.rabbitmq.three;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author hxld
 * @create 2022-08-23 22:28
 */
public class Task02 {

    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        //开启发布确认，只要有信道之后就可以
        channel.confirmSelect();

        //持久化
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME,durable,false,false,null);

        //从控制台接收消息
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()){
            String message = scanner.next();

            //设置生产者发送消息为持久化，及保存在磁盘中
            channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes(StandardCharsets.UTF_8));

            System.out.println("生产者生产消息："+message);
        }
    }
}
