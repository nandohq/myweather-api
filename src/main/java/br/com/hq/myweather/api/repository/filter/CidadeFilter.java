package br.com.hq.myweather.api.repository.filter;

import br.com.hq.myweather.api.model.Pais;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CidadeFilter extends Filter {
	
	private String descricao;
	private Pais pais;
}
