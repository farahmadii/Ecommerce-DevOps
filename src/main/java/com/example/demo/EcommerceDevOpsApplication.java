package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EnableJpaRepositories("com.example.demo.model.persistence.repositories")
@EntityScan("com.example.demo.model.persistence")
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class EcommerceDevOpsApplication {

	private static final Logger logger = LoggerFactory.getLogger(EcommerceDevOpsApplication.class);

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){

		logger.debug("attempting to load Bean " + BCryptPasswordEncoder.class.getName() + "into the application context");
		return new BCryptPasswordEncoder();
	}
	public static void main(String[] args) {
		SpringApplication.run(EcommerceDevOpsApplication.class, args);
	}

}
