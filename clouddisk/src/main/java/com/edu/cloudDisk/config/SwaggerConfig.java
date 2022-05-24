package com.edu.cloudDisk.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@Profile({"dev", "test"})
@EnableSwaggerBootstrapUI
public class SwaggerConfig {
    @Bean
    public Docket apiDocket() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("token").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .genericModelSubstitutes(DeferredResult.class)
                .globalOperationParameters(pars)
                .apiInfo(apiInfo())
                .forCodeGeneration(true).select()
//                .apis(RequestHandlerSelectors.basePackage("cn.controller"))  // 根据包名分组
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/cloudDisk/**"))  // 根据请求路径分组
                .build().groupName("API").pathMapping("/");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("云盘 APIs")
                .description("EDU Cloud Disk apis document")
                .termsOfServiceUrl("http://dev-disk.lingoace.com")
                .version("1.0")
                .build();
    }
}
