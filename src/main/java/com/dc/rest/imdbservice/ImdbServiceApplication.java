package com.dc.rest.imdbservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication
@EnableHypermediaSupport(type = { EnableHypermediaSupport.HypermediaType.HAL })
@ComponentScan({ "com.dc.rest.imdbservice" })
public class ImdbServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImdbServiceApplication.class, args);
	}

}
