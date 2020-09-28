package br.com.hq.myweather.api.service;

import static br.com.hq.myweather.api.utils.Utils.isValidId;
import static java.util.Objects.isNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.hq.myweather.api.exception.MyWeatherBadRequest;
import br.com.hq.myweather.api.exception.MyWeatherNotFound;
import br.com.hq.myweather.api.exception.MyWeatherValidationException;
import br.com.hq.myweather.api.model.Cidade;
import br.com.hq.myweather.api.model.Pais;
import br.com.hq.myweather.api.model.Previsao;
import br.com.hq.myweather.api.repository.CidadeRepository;
import br.com.hq.myweather.api.repository.filter.CidadeFilter;
import br.com.hq.myweather.api.validator.CidadeValidator;
import br.com.hq.myweather.api.validator.PaisValidator;

@Service
public class CidadeService {

	@Autowired private PaisValidator paisValidator;
	@Autowired private CidadeValidator cidadeValidator;
	
	@Autowired private OpenWeatherAPIService owService;

	@Autowired private CidadeRepository cidades;

	/**
	 * Persiste uma cidade na base de dados
	 * 
	 * @param cidade {@link Cidade} Cidade a ser persistida
	 * @return {@link Cidade} Cidade persistida com seu devido identificador
	 * @throws MyWeatherValidationException Exceção personalizada
	 */
	@Transactional
	public Cidade salvar(Cidade cidade) {

		Pais pais = paisValidator.validate(cidade.getPais());
		cidade.setPais(pais);

		Cidade validada = cidadeValidator.validate(cidade);
		validada.setDescricao(cidade.getDescricao());

		return cidades.save(validada);
	}

	/**
	 * Atualiza a descrição de uma cidade. Os demais atributos, ainda que alterados pelo cliente, não serão persistidos
	 * @param cidade {@link Cidade} Cidade que está sendo atualizada
	 * @return {@link Cidade} Cidade atualizada
	 * @throws MyWeatherValidationException Exceção personalizada
	 */
	public Cidade atualizar(Cidade cidade) {
		Cidade retornada = checarIdentificador(cidade.getId());		
		retornada.setDescricao(cidade.getDescricao());

		return cidades.save(retornada);
	}

	/**
	 * Remove uma cidade da bae de dados
	 * 
	 * @param id {@link Long} Identificador da cidade que se deseja remover
	 * @throws MyWeatherValidationException Exceção personalizada
	 */
	@Transactional
	public void remover(Long id) {
		this.cidades.delete(checarIdentificador(id));
	}

	/**
	 * Retorna uma cidade a partir de um identificador informado
	 * 
	 * @param id {@link Long} Identificador da cidade que se deseja obter
	 * @return {@link Cidade} Cidade retornada
	 */
	public Cidade porId(Long id) {
		if (!isValidId(id))
			return null;

		return this.cidades.findById(id).orElse(null);
	}
	
	/**
	 * Retorna uma previsão do tempo para os próximos 5 dias
	 * @param id {@link Long} Identificador da cidade que se deseja obter a previsão do tempo
	 * @return {@link Previsao} Previsao do tempo retornada
	 */
	public Previsao previsaoPorId(Long id) {
		Cidade retornada = checarIdentificador(id);
		return this.owService.previsaoPorCidade(retornada);		
	}

	/**
	 * Retorna uma lista paginada de cidades a partir de alguns filtros
	 * 
	 * @param filter   {@link CidadeFilter} Objeto contendo os filtros da pesquisa
	 * @param pageable {@link Pageable} Objeto contendo os dados de paginação
	 * @return
	 */
	public Page<Cidade> pesquisar(CidadeFilter filter, Pageable pageable) {
		return this.cidades.pesquisar(filter, pageable);
	}
	
	private Cidade checarIdentificador(Long id ) {
		if (!isValidId(id))
			throw new MyWeatherBadRequest("O identificador é obrigatório");

		Cidade retornada = porId(id);

		if (isNull(retornada))
			throw new MyWeatherNotFound("Nenhuma cidade encontrada com o identificador " + id);
		
		return retornada;
	}
}