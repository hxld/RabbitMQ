package com.atguigu.rabbitmq.springbootrabbitmq.controller;


import com.atguigu.rabbitmq.springbootrabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.applet.resources.MsgAppletViewer;

import java.util.Date;


/**
 * @author hxld
 * @create 2022-08-26 22:10
 */
@Slf4j
@RequestMapping("ttl")   //实现浏览器的请求路径与方法的映射
@RestController    //表示Controller类,同时要求返回值为JSON
public class SendMsgController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("sendMsg/{message}")     // 只能接收GET请求类型
    //@PathVariable   restFul结构,接收参数的注解.
    public void sendMsg(@PathVariable String message){
        log.info("当前时间:{}，发送一条信息给两个TTL队列:{}",new Date(),message);
        rabbitTemplate.convertAndSend("X","XA","消息来自ttl为10s的队列："+message);
        rabbitTemplate.convertAndSend("X","XB","消息来自ttl为40s的队列："+message);
    }


    //开始发消息 消息TTL
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message,@PathVariable String ttlTime){
        log.info("当前时间:{},发送一条时长{}毫秒TTL信息给队列QC:{}",
                new Date().toString(),ttlTime,message);
        rabbitTemplate.convertAndSend("X","XC",message,msg ->{
            //发送消息的时候  延迟时长
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    //开始发消息 基于插件的消息及延迟的时间
    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable String message,@PathVariable Integer delayTime){
        log.info("当前时间:{},发送一条时长{}毫秒TTL信息给队列delayed.queue:{}",
                new Date().toString(),delayTime,message);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,DelayedQueueConfig.DELAYED_ROUTING_NAME,message, msg ->{
            //发送消息的时候  延迟时长
            msg.getMessageProperties().setDelay(delayTime);
            return msg;
        });
    }

}