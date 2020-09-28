package br.com.hq.myweather.api.model.dto.tempo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Classe que contém todos os dados da previsão
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OWForecast {
	
	/**
	 * Data
	 */
	@JsonProperty("dt_txt")
	private String dtTxt;
	
	/**
	 * Visibilidade
	 */
	private int visibility;
	
	/**
	 * Dados numéricos
	 */
	private OWData main;
	
	/**
	 * Dados resumidos
	 */
	private List<OWWeather> weather;
}