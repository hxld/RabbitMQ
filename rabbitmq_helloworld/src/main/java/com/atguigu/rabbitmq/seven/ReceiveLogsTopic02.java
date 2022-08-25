package com.atguigu.rabbitmq.seven;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author hxld
 * @create 2022-08-25 17:28
 */
public class ReceiveLogsTopic02 {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //声明队列
        String  queueName = "Q2";
        channel.queueDeclare(queueName,false,false,false,null);
        channel.queueBind(queueName,EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind(queueName,EXCHANGE_NAME,"lazy.#");
        System.out.println("等待接收消息......");

        DeliverCallback deliverCallback = (consumerTag,  message) ->{
            System.out.println(new String(message.getBody(),"UTF-8"));
            System.out.println("C2接收队列："+queueName+"绑定键："+message.getEnvelope().getRoutingKey());

        };


        CancelCallback cancelCallback = consumerTag -> {

        };

        //接收消息
        channel.basicConsume(queueName,true,deliverCallback,cancelCallback);


    }

}
