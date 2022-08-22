package com.atguigu.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author hxld
 * @create 2022-08-22 22:16
 */
public class Producer {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //URL
    public static final  String URL_NAME = "192.168.76.100";

    //用户名
    public static final  String USER_NAME = "admin";

    //密码
    public static final String PASSWORD = "123";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建工厂
        ConnectionFactory factory = new ConnectionFactory();
        //  工厂ip 连接RabbitMQ的队列
        factory.setHost(URL_NAME);
        //用户名
        factory.setUsername(USER_NAME);
        //密码
        factory.setPassword(PASSWORD);

        //创建连接
        Connection connection = factory.newConnection();

        //获取信道
        Channel channel = connection.createChannel();
        /**
         * 生成一个队列
         * 参数有五个
         * 1.队列名称
         * 2.队列里面的消息是否持久化（磁盘） 默认非持久化即存在内存中
         * 3.该队列是否只能供一个消费者消费，即进行消息共享，true:共享，false:不共享
         * 4.是否自动删除 当最后一个消费者断开连接后，该队列是否自动删除，true:自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //发消息
        String message = "hello world";


        /**
         * 发送一个消息
         * 1.发送到哪个交换机
         * 2.路由的Key值是哪个，本次是队列的名称
         * 3.其他参数信息
         * 4.发送消息的消息体
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送完毕");

    }

}
