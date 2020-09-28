package br.com.hq.myweather.api.model.dto.tempo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OWCity {
	
	private Long id;
	private String name;
	private String country;
}
