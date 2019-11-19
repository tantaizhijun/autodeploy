package com.asiainfo.casebase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@Import(DynamicDataSourceRegister.class)
@SpringBootApplication
//@EnableCasClient
@EnableSwagger2
public class CasebaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CasebaseApplication.class, args);
	}

}
