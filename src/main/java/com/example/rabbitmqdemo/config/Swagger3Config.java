package com.example.rabbitmqdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@EnableOpenApi
@Configuration
public class Swagger3Config implements WebMvcConfigurer {

    @Bean
    public Docket createRestApi(Environment environment) {
        //设置要显示的swagger的环境
        Profiles profiles = Profiles.of("dev");
        //通过environment.acceptsProfiles判断是否处在自己设定的环境中
        boolean flag = environment.acceptsProfiles(profiles);
        //目前不用 保留写法 直接true
        flag = true;
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .enable(flag)
                .select()
                //RequestHandlerSelectors配置要扫播接口的方式
                //basePackage指定要扫描的包
                .apis(RequestHandlerSelectors.basePackage("com.example.rabbitmqdemo.controller"))
                //paths()过滤什么路径
//                .paths(PathSelectors.ant("/zhao/**"))
                .build();
    }

    //生成接口信息，包括标题、联系人等
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger3接口文档")
                .description("如有疑问，请联系开发工程师。")
                .contact(new Contact("Swagger3", "http://localhost:8090/swagger-ui/index.html", "1@qq.com"))
                .version("1.0")
                .build();
    }

}