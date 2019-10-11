/*
 * Copyright (c) 2016
 */

package com.caicai.ottx.manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile({"dev", "test"})
public class SwaggerConfig  {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("api")
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(false)
                .pathMapping("/")
                .select()
                .paths(PathSelectors.any())//过滤的接口
                .build();
//
//        Docket docket = new Docket(DocumentationType.SWAGGER_2).select().apis(
//                RequestHandlerSelectors.basePackage("com.czd.azeroth.web.krz.controller")).paths(PathSelectors.any())
//                .build();
//        docket.genericModelSubstitutes(DeferredResult.class);
//        docket.ignoredParameterTypes(Model.class, ServletRequest.class, ServletResponse.class);
//        return docket;
    }
}
