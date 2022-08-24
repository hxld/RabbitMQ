package com.atguigu.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;



/**
 * @author hxld
 * @create 2022-08-23 21:22
 */

/**
 * 此为连接工厂
 */
public class RabbitMQUtils {
    //URL
//    public static final String URL_NAME = "192.168.76.100";
    public static final String URL_NAME = "192.168.119.100";
    //用户名
    public static final String USER_NAME = "admin";
    //密码
    public static final String PASSWORD = "123";
    public static Channel  getChannel() throws Exception {
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
        return channel;
    }
}
