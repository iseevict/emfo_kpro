package kr.co.emfo.kpro_test.global.config;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {

        return requestTemplate -> {
            String username = "emfoplus_kpro";
            String password = "emfo!@0717";
            String credentials = username + ":" + password;
            String authHeader = "Basic " + Base64.getMimeEncoder()
                    .encodeToString(credentials.getBytes());

            requestTemplate.header("Authorization", authHeader);
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("Accept", "application/json");
        };
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL; // 모든 요청 및 응답을 상세 출력
    }
}
