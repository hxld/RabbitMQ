package com.atguigu.rabbitmq.one;

import com.rabbitmq.client.*;



/**
 * @author hxld
 * @create 2022-08-22 22:43
 */
public class Consumer {
    //队列名称
    public static final String QUEUE_NAME = "mirrior_hello";

    //URL
    public static final  String URL_NAME = "192.168.119.130";

    //用户名
    public static final  String USER_NAME = "admin";

    //密码
    public static final String PASSWORD = "123";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(URL_NAME);
        factory.setUsername(USER_NAME);
        factory.setPassword(PASSWORD);
        factory.setHandshakeTimeout(60000);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        /**
         * 参数
         * 1.消费哪个队列
         * 2.消费成功后是否自动应答 true:自动yingda
         * 3.消费者成功消费的回调  - -- 接口 使用匿名内部类或者lambda
         * 4.消费者取消消费的回调    - -- 接口 使用匿名内部类或者lambda
         */

        //成功消费
        //匿名内部类实现方式
//        DeliverCallback deliverCallback = new DeliverCallback() {
//            @Override
//            public void handle(consumerTag, message) throws IOException {
//                System.out.println(message);
//            }
//        };
        //lambda表达式
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };

        //未成功消费
        //匿名内部类
//        CancelCallback cancelCallback = new CancelCallback() {
//            @Override
//            public void handle(String consumerTag ) throws IOException {
//                System.out.println(consumerTag );
//            }
//        };
        //lambda表达式 当只有一个参数的时候可以取消小括号
        CancelCallback cancelCallback =consumerTag   -> {
            System.out.println("消息消费被中断");
        };


        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

    }
}
