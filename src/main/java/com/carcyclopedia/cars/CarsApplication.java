package com.carcyclopedia.cars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
public class CarsApplication {
	public static void main(String[] args) {
		SpringApplication.run(CarsApplication.class, args);
	}
}