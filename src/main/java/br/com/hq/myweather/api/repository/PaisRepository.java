package br.com.hq.myweather.api.repository;

import br.com.hq.myweather.api.model.Pais;

public interface PaisRepository extends MWRepository<Pais, Long> {

	Pais findByNome(String nome);
	Pais findBySigla2(String sigla2);
}
