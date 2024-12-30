package kr.co.emfo.kpro_test.global.config;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

@Configuration
public class FeignClientConfig {

    @Value("${server.domain}")
    private String myDomain;

    @Bean
    public RequestInterceptor requestInterceptor() {

        return requestTemplate -> {
            if (!requestTemplate.url().contains("/send/send.emfo")) {
                String username = "emfoplus_kpro";
                String password = "emfo!@0717";
                String credentials = username + ":" + password;
                String authHeader = "Basic " + Base64.getMimeEncoder()
                        .encodeToString(credentials.getBytes());

                requestTemplate.header("Authorization", authHeader);
                requestTemplate.header("Content-Type", "application/json");
                requestTemplate.header("Accept", "application/json");
            }
            else {
                requestTemplate.header("Content-Type", "multipart/form-data; charset=EUC-KR");
                requestTemplate.header("Accept-Charset", "EUC-KR");
                requestTemplate.header("Host", myDomain); // 상대 서버가 내 도메인을 기대할 때
                requestTemplate.header("Referer", myDomain); // 상대 서버가 Referer 확인할 때
                requestTemplate.header("Origin", myDomain);
            }
        };
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL; // 모든 요청 및 응답을 상세 출력
    }
}
