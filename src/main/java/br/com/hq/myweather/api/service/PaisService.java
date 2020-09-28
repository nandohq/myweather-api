package br.com.hq.myweather.api.service;

import static br.com.hq.myweather.api.utils.Utils.isValidId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.hq.myweather.api.model.Pais;
import br.com.hq.myweather.api.repository.PaisRepository;
import br.com.hq.myweather.api.repository.filter.PaisFilter;

@Service
public class PaisService {
	
	@Autowired private PaisRepository paises;
	
	/**
	 * Verifica e retorna um país da base compatível com o identificador, nome ou siglas do objeto informado
	 * @param pais
	 * @return
	 */
	public Pais obter(Pais pais) {		
		return paises.obter(pais, "id", "nome", "sigla2", "sigla3");
	}
	
	/**
	 * Retorna país a partir de um identificador informado
	 * 
	 * @param id {@link Long} Identificador do país que se deseja obter
	 * @return {@link Pais} País retornado
	 */
	public Pais porId(Long id) {
		if (!isValidId(id))
			return null;

		return this.paises.findById(id).orElse(null);
	}
	
	/**
	 * Retorna uma lista paginada de países a partir de alguns filtros
	 * 
	 * @param filter   {@link PaisFilter} Objeto contendo os filtros da pesquisa
	 * @param pageable {@link Pageable} Objeto contendo os dados de paginação
	 * @return
	 */
	public Page<Pais> pesquisar(PaisFilter filter, Pageable pageable) {
		return this.paises.pesquisar(filter, pageable);
	}
}