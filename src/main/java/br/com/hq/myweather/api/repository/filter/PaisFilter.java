package br.com.hq.myweather.api.repository.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaisFilter extends Filter {
	
	private String sigla2;
	private String sigla3;
}
