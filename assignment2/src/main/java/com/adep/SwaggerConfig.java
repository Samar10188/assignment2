package com.adep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	Contact contact = new Contact("ADEP Team ", "http://localhost:8080/adep/assignment2/swagger-ui.html",
			"adep@team.com");

	@SuppressWarnings("rawtypes")
	List<VendorExtension> vendorExtensions = new ArrayList<>();

	ApiInfo apiInfo = new ApiInfo("RESTful ADEP Assignment Web Service documentation",
			"This pages documents RESTful Web Service endpoints", "1.0",
			"http://localhost:8080/adep/assignment2/swagger-ui.html", contact, "Apache 2.0",
			"http://www.apache.org/licenses/LICENSE-2.0", vendorExtensions);

	@Bean
	Docket apiDocket() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2).groupName("public-api").protocols(new HashSet<>(Arrays.asList("HTTP")))
				.apiInfo(apiInfo).select().apis(RequestHandlerSelectors.basePackage("com.adep.rest"))
				.paths(PathSelectors.any()).build();
		return docket;
	}

}
