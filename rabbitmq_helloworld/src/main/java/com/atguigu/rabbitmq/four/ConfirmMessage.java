package com.atguigu.rabbitmq.four;

/**
 * @author hxld
 * @create 2022-08-24 16:32
 */

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

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
//        ConfirmMessage.single();    // 575ms

        //批量确认
        ConfirmMessage.batch();

        //异步确认
        ConfirmMessage.async();

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
    public static void batch() throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //开启发布确认
        channel.confirmSelect();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);

        //开始时间
        long begin = System.currentTimeMillis();

        //批量确认消息大小
        int batchSize = 1000;

        //批量发消息 批量发布确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + " ";
            channel.basicPublish(" ",queueName,null,message.getBytes(StandardCharsets.UTF_8));

            //判断达到100条消息的时候，批量确认一次
            if( i % batchSize == 0 ){
                //发布确认
                channel.waitForConfirms();
            }

        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个批量确认消息，耗时"+(end - begin)+"ms");
    }

    //异步批量确认

    public static  void async() throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //开启发布确认
        channel.confirmSelect();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);


        /**
         * 准备一个容器
         * ConcurrentSkipListMap  ---线程安全有序的一个哈希表，适用于高并发的情况下
         * 1.轻松的将序号与消息进行关联
         * 2.轻松批量删除条目 只要给到序号
         * 3.支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long,String> outstandingConfirms = new ConcurrentSkipListMap<>();


        //消息确认成功 回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {

            if(multiple){
                //2.删除到已经确认的消息 剩下的就是未确认的消息
                ConcurrentNavigableMap<Long,String> confimed = outstandingConfirms.headMap(deliveryTag);
                confimed.clear();
            }else {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认的消息"+deliveryTag);

        };
        //消息失败 回调函数
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            System.out.println("未确认的消息"+deliveryTag);
        };

        //监听器 消息哪失败，消息哪成功
        channel.addConfirmListener(ackCallback,nackCallback);   //异步通知

        //开始时间
        long begin = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息"+i;
            channel.basicPublish("",queueName,null,message.getBytes(StandardCharsets.UTF_8));
            //1.此处记录下所有要发送的消息  消息的总和
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);

        }




        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个异步发布确认消息，耗时"+(end - begin)+"ms");
    }

}
