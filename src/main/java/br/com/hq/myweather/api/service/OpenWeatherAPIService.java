package br.com.hq.myweather.api.service;

import static br.com.hq.myweather.api.utils.Utils.isValidEntity;
import static br.com.hq.myweather.api.utils.Utils.isValidId;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.hq.myweather.api.OpenWeatherProperties;
import br.com.hq.myweather.api.converter.OpenWeatherConverter;
import br.com.hq.myweather.api.model.Cidade;
import br.com.hq.myweather.api.model.Previsao;
import br.com.hq.myweather.api.model.dto.cidade.OpenWeatherCityResponse;
import br.com.hq.myweather.api.model.dto.tempo.OpenWeatherResponse;

@Service
public class OpenWeatherAPIService {
	
	@Autowired private WebClient webClientCidades;
	@Autowired private WebClient webClientPrevisao;
	
	@Autowired private OpenWeatherConverter owConverter;
	@Autowired private OpenWeatherProperties properties;
	
	/**
	 * Consulta a cidade informada na API Open Weather. <br>
	 * Se a cidade informada possuir id, a consulta utilizará esse atributo na busca;
	 * caso contrário, o nome da cidade será levado em consideração
	 * @param cidade {@link Cidade} Cidade atual, que está sendo persistida
	 * @return {@link List} Lista com uma cidade, caso a busca por id possua correspondência, ou várias, caso o nome seja compatível com mais de uma
	 */
	public List<Cidade> consultaCidade(Cidade cidade) {		
		if (isNull(cidade))
			return Collections.emptyList();
		
		List<Cidade> cidadesRetornadas = new ArrayList<>();
		
		if (isValidId(cidade.getId())) {
			Cidade retornada = cidadePorId(cidade.getId());
			if (nonNull(retornada))
				cidadesRetornadas.add(retornada);
		} else {
			cidadesRetornadas = cidadePorNome(cidade);
		}
		
		return cidadesRetornadas;				
	}
	
	/**
	 * Consulta cidades que possuam o nome informado na API Weather. O país será levado em consideração, se válido
	 * @param nome {@link String} Nome da cidade a ser consultada
	 * @param pais {@link String} Sigla do país da cidade, com dois caracteres
	 * @return {@link List} Lista de cidades retornada
	 */
	public List<Cidade> cidadePorNome(Cidade cidade) {		
		if (isNull(cidade) || isBlank(cidade.getNome()))
			return Collections.emptyList();
		
		String param = String.join(",", cidade.getNome(), nonNull(cidade.getPais()) ? cidade.getPais().getSigla2() : "");
		
		var cidadesRetornadas = this.webClientCidades.get()
				.uri(b -> b.path("/city").queryParam("query", param).build())
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToFlux(OpenWeatherCityResponse.class);
		
		return cidadesRetornadas.collectList().block().stream().map(this.owConverter::fromOWCityResponse).collect(Collectors.toList());
	}
	
	/**
	 * Consulta uma cidade a partir do seu identificador na API Open Weather
	 * @param id {@link Long} Identificador da cidade a ser consultada
	 * @return {@link Cidade} Cidade correspondente ao identificador ou <code>null</code> caso nenhum tenha sido informado
	 */
	public Cidade cidadePorId(Long id) {
		if (!isValidId(id)) 
			return null;
		
		var cidadeRetornada = this.webClientCidades.get()
				.uri("/city/{id}", id)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(OpenWeatherCityResponse.class);
		
		return this.owConverter.fromOWCityResponse(cidadeRetornada.block());				
	}
	
	/**
	 * Consulta a previsão do tempo para os próximos 5 dias a partir de uma cidade informada
	 * @param cidade {@link Cidade} Cidade para a qual se desja obter a previsão do tempo
	 * @return {@link Previsao} Objeto contendo os dados da previsão do tempo
	 */
	public Previsao previsaoPorCidade(Cidade cidade) {
		if (!isValidEntity(cidade))
			return null;
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		
		params.add("appid", this.properties.getKey());
		params.add("units", "metric");
		params.add("lang", "pt_br");
		params.add("id", cidade.getId().toString());		
		
		var previsaoRetornada = this.webClientPrevisao.get()
				.uri(builder -> builder.queryParams(params).build())
				.retrieve()
				.bodyToMono(OpenWeatherResponse.class);
		
		return this.owConverter.fromOWWeatherResponse(previsaoRetornada.block());
	}
}