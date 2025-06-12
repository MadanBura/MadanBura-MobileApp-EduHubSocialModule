package com.connect.eduHub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = "com.connect.eduHub")
@EnableDiscoveryClient
public class EduHubConnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduHubConnectApplication.class, args);
	}

}
