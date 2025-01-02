package kr.co.emfo.kpro_test.global.config;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.Charset;
import java.util.Base64;

@Configuration
public class FeignClientConfig {

    @Value("${server.domain}")
    private String myDomain;
    @Value("${server.username}")
    private String usernameP;
    @Value("${server.password}")
    private String passwordP;

    @Bean
    public RequestInterceptor requestInterceptor() {

        return requestTemplate -> {
            if (!requestTemplate.url().contains("/send/send.emfo")) { // Kpro 용
                String username = usernameP;
                String password = passwordP;
                String credentials = username + ":" + password;
                String authHeader = "Basic " + Base64.getMimeEncoder()
                        .encodeToString(credentials.getBytes());

                requestTemplate.header("Authorization", authHeader);
                requestTemplate.header("Content-Type", "application/json");
                requestTemplate.header("Accept", "application/json");
            }
            else { // Npro2 용
                requestTemplate.header("Content-Type", "multipart/form-data; charset=EUC-KR");
                requestTemplate.header("Accept-Charset", "EUC-KR");
                requestTemplate.header("Host", myDomain);
                requestTemplate.header("Referer", myDomain);
                requestTemplate.header("Origin", myDomain);
            }
        };
    }

    @Bean
    public StringHttpMessageConverter feignStringHttpMessageConverter() {
        return new StringHttpMessageConverter(Charset.forName("EUC-KR"));
    }

    /*@Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL; // 모든 요청 및 응답을 상세 출력
    }*/
}
