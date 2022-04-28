package com.example.rabbitmqdemo.config.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 消息发送确认
 * <p>
 * ConfirmCallback  只确认消息是否正确到达 Exchange 中
 * ReturnCallback   消息没有正确到达队列时触发回调，如果正确到达队列不执行
 * <p>
 * 1. 如果消息没有到exchange,则confirm回调,ack=false
 * 2. 如果消息到达exchange,则confirm回调,ack=true
 * 3. exchange到queue成功,则不回调return
 * 4. exchange到queue失败,则回调return
 * @Author jxb
 * @Date 2019-04-04 16:57:04
 */

@Configuration
public class MQProducerAckConfig {

    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);

        //设置消息投递失败的策略，有两种策略：自动删除或返回到客户端。
        //我们既然要做可靠性，当然是设置为返回到客户端(true是返回客户端，false是自动删除)
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            System.out.println();
            System.out.println("相关数据：" + correlationData);
            if (ack) {
                System.out.println("投递成功,确认情况：" + ack);
            } else {
                System.out.println("投递失败,确认情况：" + ack);
                System.out.println("原因：" + cause);
            }
        });

        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            System.out.println();
            System.out.println("ReturnCallback:     " + "消息：" + returnedMessage.getMessage());
            System.out.println("ReturnCallback:     " + "回应码：" + returnedMessage.getReplyCode());
            System.out.println("ReturnCallback:     " + "回应信息：" + returnedMessage.getReplyText());
            System.out.println("ReturnCallback:     " + "交换机：" + returnedMessage.getExchange());
            System.out.println("ReturnCallback:     " + "路由键：" + returnedMessage.getRoutingKey());
            System.out.println();
        });

        return rabbitTemplate;
    }
}