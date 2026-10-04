package br.com.posjava.leochacarolli.it_inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ItInventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItInventoryApplication.class, args);
	}
}