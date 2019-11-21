package com.asiainfo.casebase;

import net.unicon.cas.client.configuration.EnableCasClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCasClient
public class CasebaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CasebaseApplication.class, args);
	}

}
