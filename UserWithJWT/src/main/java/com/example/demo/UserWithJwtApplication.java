package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.demo.helper.RefreshableCRUDRepositoryImpl;

@EnableJpaRepositories(repositoryBaseClass = RefreshableCRUDRepositoryImpl.class)
@SpringBootApplication
public class UserWithJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserWithJwtApplication.class, args);
	}

}
