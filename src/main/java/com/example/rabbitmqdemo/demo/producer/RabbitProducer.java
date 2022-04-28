package com.example.rabbitmqdemo.demo.producer;

import com.example.rabbitmqdemo.config.rabbitmq.DirectConfig;
import com.example.rabbitmqdemo.vo.Result;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RabbitProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 配置类实现-Direct模式
     *
     * @param msg
     */
    public void send(String msg) {
        //direct类型
        //交换机  路由key 信息 mq回调对象
        //rabbitTemplate.convertAndSend(DirectConfig.BUSINESS_EXCHANGE_NAME, DirectConfig.ROUTING_KEY, msg, new CorrelationData(UUID.randomUUID().toString()));
        rabbitTemplate.convertAndSend(DirectConfig.BUSINESS_EXCHANGE_NAME, DirectConfig.ROUTING_KEY, msg, message -> {
            //设置消息延迟
            message.getMessageProperties().setHeader("x-delay", 60000);
            return message;
        }, new CorrelationData(UUID.randomUUID().toString()));
    }

    /**
     * 配置类实现-Topic模式
     */
    public void sendTopic() {
//        rabbitTemplate.convertAndSend("queue", "Hello,Rabbit!");
        //交换机  路由key 信息 mq回调对象
        rabbitTemplate.convertAndSend("exchange", "topic.message", "Hello,Rabbit!"); //topic类型
    }

    /**
     * 配置类实现-Fanout模式
     */
    public void sendFanout() {
        rabbitTemplate.convertAndSend("fanoutExchange", "", "Hello,Rabbit!"); //fanout类型
    }

    /**
     * 全注解实现
     *
     * @param exchange
     * @param routing
     * @param data
     * @return
     */
    public Result<?> publish(String exchange, String routing, String data) {
        rabbitTemplate.convertAndSend(exchange, routing, data);
        return Result.success("publish");
    }

}
