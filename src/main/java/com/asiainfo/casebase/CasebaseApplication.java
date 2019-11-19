package com.asiainfo.casebase;

import com.asiainfo.casebase.config.db.JpaDataSourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@Import(JpaDataSourceConfig.class)
@SpringBootApplication
//@EnableCasClient
@EnableSwagger2
public class CasebaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CasebaseApplication.class, args);
	}

}
