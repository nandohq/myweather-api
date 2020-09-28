package br.com.hq.myweather.api;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.util.MimeTypeUtils.parseMimeType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.ExchangeStrategies.Builder;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hq.myweather.api.repository.impl.MWRepositoryImpl;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = MWRepositoryImpl.class)
public class MyWeatherApplication {
	
	@Autowired private OpenWeatherProperties properties;
	
	private Builder exchangeBuilder;
	
	public MyWeatherApplication() {
		var mapper = new ObjectMapper();
		mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		var jacksonDecoder = new Jackson2JsonDecoder(mapper, parseMimeType(TEXT_PLAIN_VALUE));
		this.exchangeBuilder = ExchangeStrategies.builder();
		this.exchangeBuilder.codecs(conf -> conf.customCodecs().register(jacksonDecoder));
	}

	public static void main(String[] args) {
		SpringApplication.run(MyWeatherApplication.class, args);
	}
	
	@Bean
	public WebClient webClientCidades(WebClient.Builder builder) {		
		return builder//
				.baseUrl(properties.getCitiesUri())
				.defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
				.exchangeStrategies(this.exchangeBuilder.build()).build();
	}
	
	@Bean
	public WebClient webClientPrevisao(WebClient.Builder builder) {
		return builder//
				.baseUrl(properties.getWeatherUri())
				.defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
				.exchangeStrategies(this.exchangeBuilder.build()).build();
	}
}