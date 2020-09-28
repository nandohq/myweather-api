package br.com.hq.myweather.api.validator;

import static br.com.hq.myweather.api.utils.Utils.isValidList;
import static java.util.Objects.isNull;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.hq.myweather.api.model.Cidade;
import br.com.hq.myweather.api.repository.CidadeRepository;
import br.com.hq.myweather.api.service.OpenWeatherAPIService;

@Component
public class CidadeValidator extends Validator<Cidade> {

	@Autowired
	private CidadeRepository cidades;
	
	@Autowired private OpenWeatherAPIService owAPI;
	
	@Override
	public Cidade validate(Cidade cidade) {		
		validateAlreadyCreated(cidade);
		return validateExists(cidade);
	}
	
	public void validateAlreadyCreated(Cidade cidade) {
		if (isNull(cidade) || isNull(cidade.getPais()))
			return;
		
		List<Cidade> cidadesDaBase = cidades.porNome(cidade.getNome(), cidade.getPais().getSigla2());

		if (isValidList(cidadesDaBase)) {
			StringBuilder erro = new StringBuilder("Parece que a cidade " + cidade.getNome() + " já foi cadastrada. ");
			erro.append("Correspondência(s) encontrada(s): ");
			erro.append(cidadesDaBase.stream().map(Cidade::getLabel).collect(Collectors.joining(" | ")));

			throwConflict(erro.toString());
		}
	}
	
	public Cidade validateExists(Cidade cidade) {
		List<Cidade> cidadesDoMundo = owAPI.consultaCidade(cidade);

		if (!isValidList(cidadesDoMundo)) {
			StringBuilder erro = new StringBuilder("A cidade informada não é suportada");
			erro.append(" - você não está tentando cadastrar uma cidade de Nárnia ou Oz, está?");
			
			throwBadRequest(erro.toString());
		}

		if (cidadesDoMundo.size() > 1) {
			StringBuilder erro = new StringBuilder("Encontramos mais de uma correspondência para a cidade ");
			erro.append(cidade.getNome()).append(" no mundo: ");
			erro.append(cidadesDoMundo.stream().map(Cidade::getLabel).collect(Collectors.joining(" | ")));

			throwConflict(erro.toString());
		}
		
		return cidadesDoMundo.get(0);
	}
}