package com.mines.server.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger 3配置类（生成在线API文档）
 * 文档访问地址：
 * - 原生UI：http://localhost:8080/swagger-ui/index.html
 * - Knife4j增强UI：http://localhost:8080/doc.html
 */
@Configuration
@EnableOpenApi // 启用OpenAPI规范（Swagger 3）
public class SwaggerConfig {

    /**
     * 配置API文档信息
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                // 扫描带有@ApiOperation注解的方法
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 扫描所有控制器路径
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 配置文档基本信息（标题、描述、版本等）
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("地下矿实时数据接入服务端API文档（V6规范）")
                .description("基于《地下矿实时数据数据V6接入指南20250703(2).docx》实现的服务端接口，包含静态数据填报和实时数据处理相关接口")
                .version("1.0.0")
                .contact(new Contact(
                        "开发团队",
                        "http://mines-server.com",
                        "contact@mines-server.com"
                ))
                .build();
    }
}
