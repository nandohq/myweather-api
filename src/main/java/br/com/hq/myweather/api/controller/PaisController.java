package br.com.hq.myweather.api.controller;

import static java.util.Objects.isNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.hq.myweather.api.model.Pais;
import br.com.hq.myweather.api.repository.filter.PaisFilter;
import br.com.hq.myweather.api.service.PaisService;

@RestController
@RequestMapping("/paises")
public class PaisController {
	
	@Autowired private PaisService paises;
	
	@GetMapping
	public Page<Pais> pesquisar(PaisFilter filter, Pageable pageable) {
		return paises.pesquisar(filter, pageable);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Pais> obterUm(@PathVariable Long id) {
		Pais retornado = paises.porId(id);
		return isNull(retornado) ? ResponseEntity.notFound().build() : ResponseEntity.ok(retornado);
	}
}