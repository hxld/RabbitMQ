package com.atguigu.rabbitmq.three;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.atguigu.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author hxld
 * @create 2022-08-23 22:44
 */
public class Wroker03 {

    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        System.out.println("c1接收消息较短");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            //  模拟场景 业务处理多，睡眠1秒
            SleepUtils.sleep(1);
            System.out.println("接收到的消息："+new String(message.getBody(),"utf-8"));
            //手动应答
            /**
             * 参数1 ：消息标记，每一个消息头上都带着一个消息标记
             * 参数2：是否批量应答 true 表示批量
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag+"消费者取消消息回调");
        };
        //设置不公平分发  1   轮训分发 0 （默认）
//        int prefetchCount = 1;
        //预取值
        int prefetchCount = 2;
        channel.basicQos(prefetchCount);
        //手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME,autoAck,deliverCallback,cancelCallback);
    }
}
