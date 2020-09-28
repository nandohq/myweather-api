package br.com.hq.myweather.api.model.dto.cidade;

import lombok.Data;

@Data
public class OWCoordinates {
	
	/**
	 * Longitude
	 */
	private Double lon;
	
	/**
	 * Latitude
	 */
	private Double lat;
}
