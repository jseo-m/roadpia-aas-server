package org.roadpia.aas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI roadpiaAasOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Roadpia AAS 외부 연동 API")
                        .version("v1")
                        .description("Eclipse BaSyx AAS Environment를 내부 서버로 사용하고, 외부 호출자에게 자산 중심 API를 제공하는 Spring Boot Adapter API입니다."));
    }
}
