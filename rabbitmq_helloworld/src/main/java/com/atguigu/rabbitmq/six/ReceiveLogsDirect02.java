package com.atguigu.rabbitmq.six;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;


/**
 * @author hxld
 * @create 2022-08-25 12:35
 */
public class ReceiveLogsDirect02 {

    //交换机名称
    public static final  String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //声明一个队列
        channel.queueDeclare("disk",false,false,false,null);
        //绑定
        channel.queueBind("disk",EXCHANGE_NAME,"error");



        //接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("02控制台打印接收到的消息："+new String(message.getBody(),"UTF-8"));

        };
        //取消消息
        CancelCallback cancelCallback = consumerTag -> {

        };

        channel.basicConsume("disk",true,deliverCallback,cancelCallback);


    }





}
