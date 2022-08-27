package com.atguigu.rabbitmq.springbootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author hxld
 * @create 2022-08-27 16:11
 */


@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback {
    /**
     * 交换机确认回调方法
     * 1.发消息 交换机接收到了 回调
     *  1.1 correlationData  保存回调消息的ID及相关信息
     *  1.2 交换机收到消息  ack = true
     *  1.3 cause nullify
     *
     *  2.发消息  交换机接收失败了 回调
     *  2.1 correlationData  保存回调消息的ID及相关信息
     *  2.2 交换机未收到消息 ack = false
     *  2.3 cause  失败的原因
     * @param correlationData
     * @param ack
     * @param cause
     */


    @Autowired
    private  RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        //注入
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    //回调接口
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";   //三元表达式
        if(ack){
            log.info("交换机已经收到id为:{}的消息",id);
        }else {
            log.info("交换机还未收到id为:{}消息,由于原因:{}",id,cause);
        }
    }





    //回退接口
    @Override
    public void returnedMessage(ReturnedMessage returned) {
//        log.info("消息{},被交换机{}退回，退回原因:{},路由key:{}：",returned.getMessage()
//                ,returned.getExchange(),returned.getReplyText(),returned.getRoutingKey());
        log.info(returned.toString());
    }



}
