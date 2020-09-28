	package br.com.hq.myweather.api.model.dto.tempo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Classe que representa a resposta da API Open Weather
 * @author Fernando Souza
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherResponse {

	private OWCity city;
	private List<OWForecast> list = new ArrayList<>();	
}