package br.com.hq.myweather.api.validator;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.hq.myweather.api.model.Pais;
import br.com.hq.myweather.api.repository.PaisRepository;

@Component
public class PaisValidator extends Validator<Pais> {
	
	@Autowired private PaisRepository paises;

	@Override
	public Pais validate(Pais pais) {
		if (isNull(pais))
			throwBadRequest("O país deve ser informado");
		
		if (isBlank(pais.getNome()) && isBlank(pais.getSigla2()))
			throwBadRequest("Informe o nome ou a sigla do país");
		
		Pais existente = paises.obter(pais, "id", "nome", "sigla2", "sigla3");
		
		if (isNull(existente))
			throwNotFound("O país "+pais.getLabel()+" não foi encontrado. Você não está tentando cadastrar uma cidade de Atlantis, está?");
		
		return existente;
	}
}