package com.atguigu.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author hxld
 * @create 2022-08-22 22:16
 */
public class Producer {
    //队列名称
    public static final String QUEUE_NAME = "node2_hello";

    //URL
    public static final  String URL_NAME = "192.168.119.130";

    //用户名
    public static final  String USER_NAME = "admin";

    //密码
    public static final String PASSWORD = "123";

    public static final String FED_EXCHANGE_NAME = "fed_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建工厂
        ConnectionFactory factory = new ConnectionFactory();
        //  工厂ip 连接RabbitMQ的队列
        factory.setHost(URL_NAME);
        //用户名
        factory.setUsername(USER_NAME);
        //密码
        factory.setPassword(PASSWORD);
        //测试消息优先级的时候启动报错，原因是连接超时，百度查了下是要设置超时时间。
        factory.setHandshakeTimeout(60000);

        //创建连接
        Connection connection = factory.newConnection();

        //获取信道
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(FED_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        /**
         * 生成一个队列
         * 参数有五个
         * 1.队列名称
         * 2.队列里面的消息是否持久化（磁盘） 默认非持久化即存在内存中
         * 3.该队列是否只能供一个消费者消费，即进行消息共享，true:共享，false:不共享
         * 4.是否自动删除 当最后一个消费者断开连接后，该队列是否自动删除，true:自动删除
         * 5.其他参数
         */

        //声明队列的时候设置优先级
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-max-priority",10);  //设置消息的优先级，0-225区间，实际开发中取到10就行，否则浪费资源。
        channel.queueDeclare(QUEUE_NAME,false,false,false,arguments);
        channel.queueBind(QUEUE_NAME,FED_EXCHANGE_NAME,"royteKey");
        //发消息 的时候设置某条消息的优先级
//        String message = "hello world";
        for (int i = 1; i < 11 ; i++) {
            String message = "info"+i;
            if(i == 5){
                AMQP.BasicProperties properties =
                        new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("",QUEUE_NAME,properties,message.getBytes());
            }else{
                channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            }
        }


        /**
         * 发送一个消息
         * 1.发送到哪个交换机
         * 2.路由的Key值是哪个，本次是队列的名称
         * 3.其他参数信息
         * 4.发送消息的消息体
         */
//        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送完毕");

    }
    }

