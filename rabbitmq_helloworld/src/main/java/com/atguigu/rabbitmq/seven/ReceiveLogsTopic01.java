package com.atguigu.rabbitmq.seven;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.*;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import javax.xml.ws.BindingType;

/**
 * @author hxld
 * @create 2022-08-25 17:28
 */
public class ReceiveLogsTopic01 {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //声明队列
        String  queueName = "Q1";
        channel.queueDeclare(queueName,false,false,false,null);
        channel.queueBind(queueName,EXCHANGE_NAME,"*.orange.*");
        System.out.println("等待接收消息......");

        DeliverCallback deliverCallback = (consumerTag,  message) ->{
            System.out.println(new String(message.getBody(),"UTF-8"));
            System.out.println("C1接收队列："+queueName+"绑定键："+message.getEnvelope().getRoutingKey());

        };


        CancelCallback cancelCallback = consumerTag -> {

        };

        //接收消息
        channel.basicConsume(queueName,true,deliverCallback,cancelCallback);


    }

}
