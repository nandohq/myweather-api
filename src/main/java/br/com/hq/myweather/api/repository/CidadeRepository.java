package br.com.hq.myweather.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import br.com.hq.myweather.api.model.Cidade;

public interface CidadeRepository extends MWRepository<Cidade, Long> {
	
	@Query("SELECT cid FROM Cidade cid JOIN cid.pais pais WHERE cid.nome LIKE ?1 AND pais.sigla2 LIKE ?2")
	List<Cidade> porNome(String nome, String siglaPais);
}
