package com.atguigu.rabbitmq.six;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author hxld
 * @create 2022-08-25 12:54
 */
public class DirectLogs {


    //交换机名称
    public static final  String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();


        Scanner scanner = new Scanner(System.in);

        while(scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"warning",null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发送消息："+ message);
        }
    }
}
