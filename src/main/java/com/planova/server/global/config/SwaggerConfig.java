package com.planova.server.global.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.planova.server.global.constants.Constants;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final Constants constants;

    @Bean
    public OpenAPI planovaOpenAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("Bearer Authentication");

        List<Server> servers = Arrays.asList(
                new Server().url("http://localhost:8000/api").description("로컬 개발용"),
                new Server().url(constants.getServerUrl()).description("배포 서버용"));

        Info info = new Info()
                .title("Planova Project Management API")
                .description("Planova 프로젝트의 백엔드 REST API 문서입니다.<br>" +
                        "효율적인 프로젝트 관리와 협업을 위한 기능을 제공합니다.")
                .version("v1.0.0")
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));

        ExternalDocumentation externalDocs = new ExternalDocumentation()
                .description("Planova GitHub Repository")
                .url("https://github.com/Gyulo94/planova-spring");

        return new OpenAPI()
                .info(info)
                .externalDocs(externalDocs)
                .servers(servers)
                .components(new Components().addSecuritySchemes("Bearer Authentication", securityScheme))
                .addSecurityItem(securityRequirement);
    }
}