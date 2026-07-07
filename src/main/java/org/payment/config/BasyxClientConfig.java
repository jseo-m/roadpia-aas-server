package org.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BasyxClientConfig {

    @Bean
    public WebClient basyxWebClient(
            WebClient.Builder builder,
            @Value("${basyx.base-url}") String basyxBaseUrl
    ) {
        return builder
                .baseUrl(basyxBaseUrl)
                .build();
    }
}
