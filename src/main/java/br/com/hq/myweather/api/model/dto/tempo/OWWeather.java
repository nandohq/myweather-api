package br.com.hq.myweather.api.model.dto.tempo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Classe que contém os dados visuais da previsão
 * @author Fernando Souza
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OWWeather {
	
	/**
	 * Característica geral do tempo
	 */
	private String main;
	
	/**
	 * Descrição da característica
	 */
	private String description;
	
	/**
	 * Ícone que representa o tempo
	 */
	private String icon;
}