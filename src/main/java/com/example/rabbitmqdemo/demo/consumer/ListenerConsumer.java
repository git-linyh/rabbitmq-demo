package com.example.rabbitmqdemo.demo.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 基于注解的消费方式
 * Created by @author yihui in 15:03 20/3/18.
 */
@Component
public class ListenerConsumer {

//    /**
//     * 当队列已经存在时，直接指定队列名的方式消费
//     *
//     * @param data
//     */
//    @RabbitListener(queues = "topic.a")
//    public void consumerExistsQueue(String data) {
//        System.out.println("consumerExistsQueue: " + data);
//    }

    /**
     * 队列不存在时，需要创建一个队列，并且与exchange绑定
     * value: @Queue 注解，用于声明队列，value 为 queueName, durable 表示队列是否持久化, autoDelete 表示没有消费者之后队列是否自动删除
     * exchange: @Exchange 注解，用于声明 exchange， type 指定消息投递策略，我们这里用的 topic 方式
     * key: 在 topic 方式下，这个就是我们熟知的 routingKey
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "topic.n1", durable = "true", autoDelete = "false"),
            exchange = @Exchange(value = "topic.e", type = ExchangeTypes.TOPIC), key = "r"))
    public void consumerNoQueue(String data) {
        System.out.println("consumerNoQueue: " + data);
    }


    /**
     * 需要手动ack，但是不ack时
     * ackMode=MANUAL，手动 ack确认消费
     * @param data
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "topic.n2", durable = "true", autoDelete = "false"),
            exchange = @Exchange(value = "topic.e", type = ExchangeTypes.TOPIC), key = "r"), ackMode = "MANUAL")
    public void consumerNoAck(String data) {
        // 要求手动ack，这里不ack，会怎样?
        System.out.println("consumerNoAck: " + data);
    }


    /**
     * 手动ack
     *
     * @param data
     * @param deliveryTag
     * @param channel
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "topic.n3", durable = "true", autoDelete = "false",
            arguments = {
            @Argument(name = "x-dead-letter-exchange", value = "topic.dead"),
            @Argument(name = "x-dead-letter-routing-key", value = "topic.n3.routing.key")
            }),
            exchange = @Exchange(value = "topic.e", type = ExchangeTypes.TOPIC), key = "r"), ackMode = "MANUAL")
    public void consumerDoAck(String data, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel)
            throws IOException {
        System.out.println("consumerDoAck: " + data);

        if (data.contains("success")) {
            // RabbitMQ的ack机制中，第二个参数返回true，表示需要将这条消息投递给其他的消费者重新消费
            channel.basicAck(deliveryTag, false);
        } else {
            // 第三个参数true，表示这个消息会重新进入队列
            channel.basicNack(deliveryTag, false, false);
        }
    }

    /**
     * 多节点处理
     *
     * @param data
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "topic.n4", durable = "true", autoDelete = "false"),
            exchange = @Exchange(value = "topic.e", type = ExchangeTypes.TOPIC), key = "r"), concurrency = "4")
    public void multiConsumer(String data) {
        System.out.println("multiConsumer: " + data);
    }
}