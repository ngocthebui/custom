package com.custom.ngow;

import org.springframework.boot.SpringApplication;

public class TestNgowApplication {

	public static void main(String[] args) {
		SpringApplication.from(NgowApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
