package com.atguigu.rabbitmq.springbootrabbitmq.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hxld
 * @create 2022-08-26 18:00
 */
@Configuration
public class TtlQueueConfig {

    //普通交换机
    public static final String X_EXCHANGE = "X";
    //死信交换机
    public static final String Y_DEAD_EXCHANGE = "Y";

    //普通队列
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    //死信队列
    public static final String QUEUE_DEAD_D = "QD";


    //使用注解方式声明交换机与队列
    @Bean("xExchange")
    public DirectExchange xExchange(){
        return  new DirectExchange(X_EXCHANGE);
    }


    @Bean("yExchange")
    public DirectExchange yExchange(){
        return  new DirectExchange(Y_DEAD_EXCHANGE);
    }

    @Bean("queueA")
    public Queue queueA(){
        Map<String, Object> args = new HashMap<>(3);
        //声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange",Y_DEAD_EXCHANGE);
        //声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key","YD");
        //声明队列的 TTL
        args.put("x-message-ttl",10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(args).build();
    }

    //声明队列A绑定X交换机
    @Bean
    public Binding queueBindingX(@Qualifier("queueA") Queue queueA,
                                 @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    };


    @Bean("queueB")
    public Queue queueB(){
        Map<String, Object> args = new HashMap<>(3);
        //声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange",Y_DEAD_EXCHANGE);
        //声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key","YD");
        //声明队列的 TTL
        args.put("x-message-ttl",40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(args).build();
    }

    //声明队列B绑定X交换机
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queue1B,
                                 @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queue1B).to(xExchange).with("XB");
    };


    //声明死信队列QD
    @Bean("queueD")
    public Queue queueD(){
        return  new Queue(QUEUE_DEAD_D);
    }
    //声明死信队列QD绑定关系
    @Bean
    public Binding queuedeadBindingX(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    };



}
