package kr.co.emfo.kpro_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class EmfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmfoApplication.class, args);
	}

}
