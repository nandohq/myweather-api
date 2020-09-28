package br.com.hq.myweather.api.model.dto.cidade;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OpenWeatherCityResponse {
	
	/**
	 * Identificador da cidade
	 */
	@EqualsAndHashCode.Include
	private Long id;
	
	/**
	 * Nome da cidade
	 */
	private String name;
	
	/**
	 * Sigla do país
	 */
	private String country;
	
	/**
	 * Dados de geolocalização
	 */
	private OWCoordinates coord;
}