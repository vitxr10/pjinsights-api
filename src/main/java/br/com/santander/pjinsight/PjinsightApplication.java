package br.com.santander.pjinsight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PjinsightApplication {

	public static void main(String[] args) {
		SpringApplication.run(PjinsightApplication.class, args);
	}

}
