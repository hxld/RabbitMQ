package com.atguigu.rabbitmq.four;

/**
 * @author hxld
 * @create 2022-08-24 16:32
 */

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 发布确认
 * 1.单个确认
 * 2.批量确认
 * 3.异步批量确认
 *
 */
public class ConfirmMessage {


    //批量发消息的个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {

        //单个确认
        ConfirmMessage.single();    // 575ms



    }

    //单个确认
    public static void  single() throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //开启发布确认
        channel.confirmSelect();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);

        //开始时间
        long begin = System.currentTimeMillis();

        //批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + " ";
            channel.basicPublish("",queueName,null,message.getBytes(StandardCharsets.UTF_8));

            //单个消息确认
            boolean flag = channel.waitForConfirms();
            if(flag){
                System.out.println("消息发送成功");
            }

        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个单独确认消息，耗时"+(end - begin)+"ms");
    }


    //批量确认

    //异步批量确认

}
