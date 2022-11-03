package ru.itis.suhd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication/*(exclude={DataSourceAutoConfiguration.class})*/
public class SuhdApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuhdApplication.class, args);
	}
}
