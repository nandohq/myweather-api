package br.com.hq.myweather.api.controller;

import static java.util.Objects.isNull;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.hq.myweather.api.event.CreatedResourceEvent;
import br.com.hq.myweather.api.model.Cidade;
import br.com.hq.myweather.api.model.Previsao;
import br.com.hq.myweather.api.repository.filter.CidadeFilter;
import br.com.hq.myweather.api.service.CidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {
	
	@Autowired private CidadeService cidades;
	@Autowired private ApplicationEventPublisher publisher;
	
	@PostMapping
	public ResponseEntity<Cidade> salvar(@Valid @RequestBody Cidade cidade, HttpServletResponse response) {
		
		Cidade cadastrada = cidades.salvar(cidade);
		publisher.publishEvent(new CreatedResourceEvent(this, response, cadastrada.getId()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(cadastrada);		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Cidade> atualizar (@PathVariable Long id, @Valid @RequestBody Cidade cidade) {
		cidade.setId(id);
		Cidade atualizada = cidades.atualizar(cidade);
		return ResponseEntity.ok(atualizada);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		cidades.remover(id);
	}
	
	@GetMapping
	public Page<Cidade> pesquisar(CidadeFilter filter, Pageable pageable) {
		return cidades.pesquisar(filter, pageable);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Cidade> obterUma(@PathVariable Long id) {
		Cidade retornada = cidades.porId(id);
		return isNull(retornada) ? ResponseEntity.notFound().build() : ResponseEntity.ok(retornada);
	}
	
	@GetMapping("/{id}/previsao")
	public ResponseEntity<Previsao> obterUmaPrevisao(@PathVariable Long id) {
		Previsao retornada = cidades.previsaoPorId(id);
		return isNull(retornada) ? ResponseEntity.notFound().build() : ResponseEntity.ok(retornada);
	}
}
