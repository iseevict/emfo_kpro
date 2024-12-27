package kr.co.emfo.kpro_test.global.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmfoClientConfig {

    @Bean
    public Logger.Level feignLoggerLever() { return Logger.Level.FULL; }
}
