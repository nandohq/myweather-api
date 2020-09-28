package br.com.hq.myweather.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Component
public class Previsao {

	private String cidade;
	private String pais;
	
	private List<Dia> diasPrevistos = new ArrayList<>();
	
	@Data
	@EqualsAndHashCode(onlyExplicitlyIncluded = true)
	@Component
	public class Dia {
		
		@EqualsAndHashCode.Include
		private LocalDate data;
		
		private BigDecimal minima;
		private BigDecimal maxima;
		private BigDecimal sensacao;
		private BigDecimal umidade;
		
		private int visibilidade;
		
		private String descricao;
		private String icone;
	}
}
