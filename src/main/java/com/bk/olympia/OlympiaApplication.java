package com.bk.olympia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class OlympiaApplication {

	public static void main(String[] args) {
	    SpringApplication.run(OlympiaApplication.class, args);
    }
}
