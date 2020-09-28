package br.com.hq.myweather.api.exception;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyWeatherError {
	
	/**
	 * Código do erro
	 */
	private int codigo;
	/**
	 * Tipo do erro
	 */
	private String tipo;
	/**
	 * Erros ocorridos
	 */
	private List<String> erros;
	
	public void addError(String error) {
		if (isNull(this.erros))
			this.erros = new ArrayList<>();
		
		this.erros.add(error);
	}
}