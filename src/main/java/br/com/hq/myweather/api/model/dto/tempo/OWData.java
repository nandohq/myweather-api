package br.com.hq.myweather.api.model.dto.tempo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Classe que contém os dados numéricos da previsão
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OWData {
	
	/**
	 * Sensação térmica
	 */
	@JsonProperty("feels_like")
	private BigDecimal feelsLike;
	
	/**
	 * Temperatura mínima
	 */
	@JsonProperty("temp_min")
	private BigDecimal tempMin;
	
	/**
	 * Temperatura máxima
	 */
	@JsonProperty("temp_max")
	private BigDecimal tempMax;
	
	/**
	 * Humidade relativa do ar
	 */
	private BigDecimal humidity;
}