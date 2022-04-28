package com.example.rabbitmqdemo.controller;

import com.example.rabbitmqdemo.demo.producer.RabbitProducer;
import com.example.rabbitmqdemo.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbitmq")
@Api(tags = "MQ单元测试")
public class RabbitController {

    @Autowired
    private RabbitProducer producer;

    @ApiOperation(value = "配置类实现-Direct模式", notes = "配置类实现-Direct模式")
    @GetMapping("/sendDirect")
    public Result<?> send(@RequestParam String msg) {
        producer.send(msg);
        return Result.success("Direct");
    }

    @ApiOperation(value = "配置类实现-Topic模式", notes = "配置类实现-Topic模式")
    @GetMapping("/sendTopic")
    public Result<?> sendTopic() {
        producer.sendTopic();
        return Result.success("Topic");
    }

    @ApiOperation(value = "配置类实现-Fanout模式", notes = "配置类实现-Fanout模式")
    @GetMapping("/sendFanout")
    public Result<?> sendFanout() {
        producer.sendFanout();
        return Result.success("Fanout");
    }

    @ApiOperation(value = "全注解实现", notes = "全注解实现")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "exchange", value = "交换机", required = true, defaultValue = "1", dataType = "string"),
            @ApiImplicitParam(name = "routing", value = "路由", required = true, defaultValue = "1", dataType = "string"),
            @ApiImplicitParam(name = "data", value = "信息", required = true, defaultValue = "1", dataType = "string")
    })
    @GetMapping(path = "publish")
    public Result<?> publish(String exchange, String routing, String data) {
        producer.publish(exchange, routing, data);
        return Result.success("publish");
    }
}
