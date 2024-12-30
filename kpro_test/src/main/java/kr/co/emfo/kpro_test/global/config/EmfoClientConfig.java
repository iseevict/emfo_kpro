package kr.co.emfo.kpro_test.global.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@Configuration
public class EmfoClientConfig {

    @Bean
    public StringHttpMessageConverter feignStringHttpMessageConverter() {
        return new StringHttpMessageConverter(Charset.forName("EUC-KR"));
    }

    @Bean
    public Logger.Level feignLoggerLever() { return Logger.Level.FULL; }
}
