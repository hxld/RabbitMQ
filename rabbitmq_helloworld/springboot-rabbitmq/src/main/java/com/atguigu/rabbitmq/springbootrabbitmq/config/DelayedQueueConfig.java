package com.atguigu.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hxld
 * @create 2022-08-27 10:42
 */
@Configuration
public class DelayedQueueConfig {
    public static final String  DELAYED_QUEUE_NAME = "delayed.queue";
    public static final String  DELAYED_EXCHANGE_NAME = "delayed.exchange";
    public static final String DELAYED_ROUTING_NAME = "delayed.routingkey";

    //声明队列
    @Bean
    public Queue delayedQueue(){
        return new Queue(DELAYED_QUEUE_NAME);

    }

    //声明交换机
    //自定义交换机，我们在这里定义的是一个延迟交换机
    @Bean
    public  CustomExchange delayedExchange(){
        Map<String, Object> args = new HashMap<>();
        //自定义交换机的类型
        args.put("x-delayed-type","direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME,"x-delayed-message",true,false,args);
    }

    //声明绑定关系
    @Bean
    public Binding bindingDelaydeQueue(@Qualifier("delayedQueue")Queue queue,
                                       @Qualifier("delayedExchange") CustomExchange delaydeExchange){
        return BindingBuilder.bind(queue).to(delaydeExchange).with(DELAYED_ROUTING_NAME).noargs();
    }
}
