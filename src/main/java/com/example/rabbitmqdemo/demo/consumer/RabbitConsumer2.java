package com.example.rabbitmqdemo.demo.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RabbitListener(queues = "dead-letter-queue")
public class RabbitConsumer2 {
    /**
     * 指定消费的队列
     */
    @RabbitHandler
    public void consume(String msg, Message message, Channel channel){
        boolean success = false;
        int retryCount = 3;
        while (!success && retryCount-- > 0){
            try {
                // 处理消息
                log.info("收到消息: {}, deliveryTag = {}", msg, message.getMessageProperties().getDeliveryTag());
                if(msg.equals("dead-letter2")){
                    throw new RuntimeException("收到死信");
                }
                // 正常处理完毕，手动确认
                success = true;
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }catch (Exception e){
                log.error("程序异常：{}", e.getMessage());
            }
        }
        // 达到最大重试次数后仍然消费失败
        if(!success){
            // 手动删除，移至死信队列
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}