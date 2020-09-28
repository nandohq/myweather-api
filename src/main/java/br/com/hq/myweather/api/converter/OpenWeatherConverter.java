package br.com.hq.myweather.api.converter;

import static ir.cafebabe.math.utils.BigDecimalUtils.is;
import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.hq.myweather.api.model.Cidade;
import br.com.hq.myweather.api.model.Previsao;
import br.com.hq.myweather.api.model.Previsao.Dia;
import br.com.hq.myweather.api.model.dto.cidade.OpenWeatherCityResponse;
import br.com.hq.myweather.api.model.dto.tempo.OWForecast;
import br.com.hq.myweather.api.model.dto.tempo.OpenWeatherResponse;
import br.com.hq.myweather.api.repository.PaisRepository;

/**
 * Classe auxiliar responsável por converter os objetos de resposta da API em
 * objetos de domínio
 * 
 * @author Fernando Souza
 */
@Component
public class OpenWeatherConverter {

	@Autowired
	private PaisRepository paises;

	/**
	 * Converte uma cidade consultada na API para uma cidade do domínio
	 * @param cityResponse {@link OpenWeatherCityResponse} Objeto contendo os dados da consulta de cidades da API
	 * @return {@link Cidade} Cidade do domínio convertida
	 */
	public Cidade fromOWCityResponse(OpenWeatherCityResponse cityResponse) {
		if (isNull(cityResponse))
			return null;

		var cidade = new Cidade();

		cidade.setId(cityResponse.getId());
		cidade.setNome(cityResponse.getName());

		if (nonNull(cityResponse.getCoord())) {
			cidade.setLatitude(cityResponse.getCoord().getLat());
			cidade.setLongitude(cityResponse.getCoord().getLon());
		}

		if (isNotBlank(cityResponse.getCountry()))
			cidade.setPais(paises.findBySigla2(cityResponse.getCountry()));

		return cidade;
	}

	/**
	 * Converte uma previsão do tempo consultada na API para uma previsão do domínio
	 * @param weatherResponse {@link OpenWeatherResponse} Objeto contendo os dados da previsão do tempo
	 * @return {@link Previsao} Previsão do domínio convertida
	 */
	public Previsao fromOWWeatherResponse(OpenWeatherResponse weatherResponse) {
		if (isNull(weatherResponse))
			return null;

		var previsao = new Previsao();		

		previsao.setCidade(weatherResponse.getCity().getName());
		previsao.setPais(weatherResponse.getCity().getCountry());

		List<Dia> diasPrevistos = weatherResponse.getList().stream().map(this::fromOWForecast).collect(Collectors.toList());
		
		tratarDiasPrevistos(diasPrevistos);
		previsao.setDiasPrevistos(diasPrevistos);

		return previsao;
	}
	
	/**
	 * Converte um objeto de dados numéricos da previsão da API em um objeto de dados numéricos da previsão de domínio
	 * @param forecast {@link OWForecast} Objeto de dados numéricos da previsão da API
	 * @return {@link Dia} Objeto de dados numéricos da previsão de domínio
	 */
	public Dia fromOWForecast(OWForecast forecast) {
		var dia = new Previsao().new Dia();
		var dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		dia.setData(LocalDate.parse(forecast.getDtTxt(), dateFormatter));
		dia.setMaxima(forecast.getMain().getTempMax());
		dia.setMinima(forecast.getMain().getTempMin());
		dia.setSensacao(forecast.getMain().getFeelsLike());
		dia.setUmidade(forecast.getMain().getHumidity());
		dia.setVisibilidade(forecast.getVisibility());
		dia.setDescricao(forecast.getWeather().get(0).getDescription().toUpperCase());
		dia.setIcone(forecast.getWeather().get(0).getIcon());
		
		return dia;
	}

	/**
	 * Agrupa as previsões que vieram calculadas a cada 3 horas por data e calcula os dados da previsão
	 * @param diasPrevistos {@link List<Dia>} Lista de dias previstos que será agrupada e calculada
	 */
	private void tratarDiasPrevistos(List<Dia> diasPrevistos) {

		/* Agrupa as previsões por data, separando os registros pela menor e maior temperatura */
		Map<LocalDate, List<Dia>> diasAgrupados = diasPrevistos.stream().collect( //
				toMap(Dia::getData, dia -> asList(dia, dia),
					(dia1, dia2) -> asList((is(dia1.get(0).getMaxima()).gt(dia2.get(0).getMaxima()) ? dia2 : dia1).get(0),
					(is(dia1.get(1).getMinima()).lt(dia2.get(1).getMinima()) ? dia2 : dia1).get(1))));

		diasPrevistos.clear();

		/*
		 * Realimenta a lista de dias previstos, configurando temperatura mínima e máxima e
		 * calculando a média da umidade relativa do ar, visibilidade e sensação térmica
		 */
		diasAgrupados.forEach((data, dias) -> {
			
			BigDecimal minima = dias.get(0).getMinima().setScale(2);
			BigDecimal maxima = dias.get(1).getMaxima().setScale(2);
			BigDecimal umidade = obterMedia(dias.get(0).getUmidade(), dias.get(1).getUmidade());
			BigDecimal sensacao = obterMedia(dias.get(0).getSensacao(), dias.get(1).getSensacao());

			int visibilidadeDaMinima = dias.get(0).getVisibilidade();
			int visibilidadeDaMaxima = dias.get(1).getVisibilidade();

			var diaTratado = new Previsao().new Dia();

			diaTratado.setData(data);
			diaTratado.setMinima(minima);
			diaTratado.setMaxima(maxima);
			diaTratado.setIcone(dias.get(1).getIcone());
			diaTratado.setDescricao(dias.get(1).getDescricao());
			diaTratado.setUmidade(umidade);
			diaTratado.setSensacao(sensacao);
			diaTratado.setVisibilidade((visibilidadeDaMinima + visibilidadeDaMaxima) / 2);

			diasPrevistos.add(diaTratado);
		});
		
		/* Ordena as previsões pela data */
		Collections.sort(diasPrevistos, (dia1, dia2) -> dia1.getData().compareTo(dia2.getData()));
	}
	
	/**
	 * Calcula a média entre dois números informados
	 * @param valor1 {@link BigDecimal}
	 * @param valor2 {@link BigDecimal}
	 * @return {@link BigDecimal} Média calculada
	 */
	private BigDecimal obterMedia(BigDecimal valor1, BigDecimal valor2) {
		if (isNull(valor1) || isNull(valor2))
			return BigDecimal.ZERO;
		
		return valor1.add(valor2).divide(new BigDecimal(2)).setScale(2, RoundingMode.HALF_UP);
	}
}