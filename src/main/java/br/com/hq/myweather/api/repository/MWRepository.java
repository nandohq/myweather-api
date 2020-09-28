package br.com.hq.myweather.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import br.com.hq.myweather.api.repository.filter.Filter;

@NoRepositoryBean
public interface MWRepository<E,I> extends JpaRepository<E, I> {
	
	/**
	 * Retorna uma entidade da base a partir de qualquer atributo dentre os informados. <br>
	 * A busca ocorrerá na ordem em que os atributos foram passados, até que haja correspondência
	 * @param entity E Objeto que representa a entidade, com os atributos a serem consultados
	 * @param attributes {@literal varargs} Atributos pelos quais a entidade será buscada
	 * @return E Entidade retornada ou <code>null</code> se não houver correspondência na base
	 */
	E obter (E entity, String... attributes);
	
	Page<E> pesquisar(Filter filter, Pageable pageable);
}
