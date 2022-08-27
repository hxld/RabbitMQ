package com.atguigu.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hxld
 * @create 2022-08-27 15:02
 */
@Configuration
public class ConfirmConfig {

    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";

    public static final String BACKUP_QUEUE_NAME = "backup.queue";
    public static final String WARNING_QUEUE_NAME = "warning.queue";
    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";


    //声明交换机
    @Bean("confirmExchange")
    public DirectExchange confirmExchange(){
//        return new DirectExchange(CONFIRM_EXCHANGE_NAME);
        //将直接交换机消息转发到备份交换机
        return  ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).durable(true)
                .withArgument("alternate-exchange",BACKUP_EXCHANGE_NAME).build();
    }

    @Bean("backupExchange")
    public FanoutExchange backupExchange(){
        return  new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    //声明队列
    @Bean("confirmQueue")
    public Queue confirmQueue(){
      return   QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    @Bean("backQueue")
    public Queue backQueue(){
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();

    }
    @Bean("warningQueue")
    public Queue warningQueue(){
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }
    //声明绑定关系
    @Bean
    public Binding queueBindingConfirm(@Qualifier("confirmExchange") DirectExchange exchange,
                                       @Qualifier("confirmQueue")Queue queue){

      return   BindingBuilder.bind(queue).to(exchange).with("key1");
    }

    @Bean
    public Binding queueBindingBackup(@Qualifier("backupExchange")FanoutExchange exchange,
                                       @Qualifier("backQueue")Queue queue){

        return   BindingBuilder.bind(queue).to(exchange);
    }
    @Bean
    public Binding queueBindingBackupWarning(@Qualifier("backupExchange")FanoutExchange exchange,
                                      @Qualifier("warningQueue")Queue queue){
        return   BindingBuilder.bind(queue).to(exchange);
    }
}
