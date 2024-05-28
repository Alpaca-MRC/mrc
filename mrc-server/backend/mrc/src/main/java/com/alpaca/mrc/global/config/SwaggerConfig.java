package com.alpaca.mrc.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@OpenAPIDefinition(
        info = @Info(title = "MRC API 명세서",
                version = "v1"))
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP) // HTTP 타입의 보안 스킴
                .scheme("bearer") // Bearer Token 방식
                .bearerFormat("JWT") // Bearer Token 형식
                .in(SecurityScheme.In.HEADER) // 헤더에 인증 정보를 포함
                .name("Authorization"); // 헤더의 이름은 "Authorization"

        // 보안 요구사항 정의
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        // OpenAPI 객체 생성 및 설정
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme)) // 보안 스킴을 Components에 추가
                .security(Arrays.asList(securityRequirement)); // 보안 요구사항을 OpenAPI에 추가
    }
}
