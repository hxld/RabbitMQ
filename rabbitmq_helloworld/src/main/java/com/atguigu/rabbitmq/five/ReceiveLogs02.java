package com.atguigu.rabbitmq.five;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;


/**
 * @author hxld
 * @create 2022-08-25 12:35
 */
public class ReceiveLogs02 {

    //交换机名称
    public static final  String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        /**
         * 生成一个临时队列，队列的名称是随机的
         * 当消费者断开与队列的连接的时候，队列就自动删除
         */
        String queueName = channel.queueDeclare().getQueue();
        /**
         * 绑定交换机与队列
         */
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        System.out.println("等待接收消息，把接收到消息打印在屏幕上。。。。。");

        //接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("02控制台打印接收到的消息："+new String(message.getBody(),"UTF-8"));

        };


        //取消消息
        CancelCallback cancelCallback = consumerTag -> {

        };

        channel.basicConsume(queueName,true,deliverCallback,cancelCallback);

    }

}
