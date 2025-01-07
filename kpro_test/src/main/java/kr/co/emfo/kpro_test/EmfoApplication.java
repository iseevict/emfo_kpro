package kr.co.emfo.kpro_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.swing.*;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class EmfoApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(EmfoApplication.class);
		springApplication.setWebApplicationType(WebApplicationType.NONE);
		springApplication.run(args);

		/*SpringApplication.run(EmfoApplication.class, args);*/
	}

}
