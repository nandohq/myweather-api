package br.com.hq.myweather.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("openweather.api")
public class OpenWeatherProperties {
	
	/**
	 * Chave de acesso à API OpenWeather
	 */
	private String key;
	/**
	 * URI da API de consulta de cidades suportadas
	 */
	private String citiesUri;
	/**
	 * URI da API de previsão do tempo Open Weather
	 */
	private String weatherUri;
}
