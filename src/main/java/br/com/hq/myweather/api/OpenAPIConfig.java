package br.com.hq.myweather.api;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class OpenAPIConfig {

	@Bean
	public Docket apiDocket() {
		return new Docket(SWAGGER_2).select()//
				.apis(RequestHandlerSelectors.basePackage("br.com.hq.myweather.api"))
				.build().apiInfo(apiInfo());
	}
	
	public ApiInfo apiInfo() {
		var contato = new Contact("Fernando Souza", "https://www.linkedin.com/in/nandohq/", "nandohq@gmail.com");
		
		return new ApiInfoBuilder()
				.title("MyWeather API")
				.description("API para cadastro de cidade e consulta de previsão do tempo")
				.version("1")
				.contact(contato)
				.build();
	}
}
