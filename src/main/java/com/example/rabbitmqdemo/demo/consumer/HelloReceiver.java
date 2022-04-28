package com.example.rabbitmqdemo.demo.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class HelloReceiver {

    @RabbitListener(queues="topic.message")
    public void process(String str) {
        System.out.println("topicReceiverA  : " + str);
    }

    @RabbitListener(queues="topic.messages")
    public void process2(String str) {
        System.out.println("topicReceiverB  : " + str);
    }

//    @RabbitListener(queues="queue")
//    public void process1(String str) {
//        System.out.println("Receiver  : " + str);
//    }

    @RabbitListener(queues="fanout.A")
    public void process3(String str) {
        System.out.println("fanoutReceiverA  : " + str);
    }

    @RabbitListener(queues="fanout.B")
    public void process4(String str) {
        System.out.println("fanoutReceiverB  : " + str);
    }

    @RabbitListener(queues="fanout.C")
    public void process5(String str) {
        System.out.println("fanoutReceiverC  : " + str);
    }

}
