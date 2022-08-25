package com.atguigu.rabbitmq.eight;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hxld
 * @create 2022-08-25 21:43
 */
public class Consumer01 {
    //普通交换机及普通队列
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String NORMAL_QUEUE = "normal_queue";
    //死信交换机及死信队列
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE,BuiltinExchangeType.DIRECT);
        //声明普通队列
        //设置参数，将普通消息超时转为死信消息
        Map<String, Object> arguments = new HashMap<>();
        /*设置过期时间  单位：ms
          ---在普通队列到死信交换机时可以设置过期时间；
         在生产者发送消息的时候可以设置过期时间（常用）*/
//        arguments.put("x-message-ttl",10000);
        //普通队列消息到死信交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死信RountingKEY  死信交换机与死信队列进行关联
        arguments.put("x-dead-letter-routing-key","lisi");
        //设置正常队列的长度的限制
        arguments.put("x-max-length",6);
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments );


        //声明死信队列

        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        //绑定普通交换机与普通队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        //绑定死信交换机与死信队列
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");

        System.out.println("等待接收消息.....");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
//            System.out.println( "c1接收的消息是："+ new String(message.getBody(),"UTF-8"));
            //设置拒绝
            String msg = new String(message.getBody(), "UTF-8");
            if(msg.equals("info5")){
                System.out.println("c1接收的消息是：" +msg+":此消息是被c1拒接的");
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else {
                System.out.println("c1接收的消息是："+msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }




        };

        CancelCallback cancelCallback = consumerTag -> {

        };


        //开启手动应答，否则自动应答不会存在拒绝
       channel.basicConsume(NORMAL_QUEUE,true,deliverCallback,cancelCallback);
    }
}
