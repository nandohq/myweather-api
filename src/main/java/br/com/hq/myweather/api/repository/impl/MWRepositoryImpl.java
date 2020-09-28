package br.com.hq.myweather.api.repository.impl;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import br.com.hq.myweather.api.exception.MyWeatherInternal;
import br.com.hq.myweather.api.repository.MWRepository;
import br.com.hq.myweather.api.repository.filter.Filter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MWRepositoryImpl<E, I> extends SimpleJpaRepository<E, I> implements MWRepository<E, I> {

	private EntityManager em;

	public MWRepositoryImpl(JpaEntityInformation<E, ?> ei, EntityManager em) {
		super(ei, em);
		this.em = em;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E obter(E entity, String... attributes) {
		if (isNull(entity) || isNull(attributes))
			return null;

		E returned = null;
		StringBuilder jpql = new StringBuilder("SELECT x FROM ").append(getDomainClass().getSimpleName()).append(" x ");

		try {
			for (String attribute : attributes) {
				Object value = PropertyUtils.getProperty(entity, attribute);

				if (nonNull(value)) {
					if (value instanceof String)
						value = "'" + value + "'";

					jpql.append("WHERE ").append("x.").append(attribute).append(" = ").append(value);
					returned = em.createQuery(jpql.toString(), getDomainClass()).getSingleResult();

					if (nonNull(returned))
						break;

					jpql.delete(jpql.indexOf("WHERE"), jpql.length() - 1);
				}
			}

			/* Não há nenhum atributo no objeto original apto a constar na cláusula */
			if (jpql.indexOf("WHERE") == -1)
				return null;

		} catch (NoResultException noResult) {
			/* Nada encontrado */
		} catch (Exception error) {
			log.error("Erro ao obter a entidade {}: {}", getDomainClass().getSimpleName(), error.getMessage());
		}

		return returned;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<E> pesquisar(Filter filter, Pageable pageable) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<E> criteria = builder.createQuery(getDomainClass());
		Root<E> root = criteria.from(getDomainClass());

		Predicate[] predicates = montarPredicados(filter, builder, root);
		
		criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, builder));
		criteria.where(predicates);

		TypedQuery<E> query = em.createQuery(criteria);

		configurarPaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(filter, pageable));
	}

	private Predicate[] montarPredicados(Filter filter, CriteriaBuilder builder, Root<E> root) {
		if (isNull(filter))
			return new Predicate[0];
		
		List<Predicate> predicates = new ArrayList<>();
		
		List<Field> fields = new ArrayList<>(Arrays.asList(filter.getClass().getSuperclass().getDeclaredFields()));
		fields.addAll(Arrays.asList(filter.getClass().getDeclaredFields()));

		try {
			for (Field field : fields) {
				field.setAccessible(true);
				if (field.getType().equals(String.class)) {
					String value = (String) field.get(filter);

					if (isNotBlank(value))
						predicates.add(builder.like(root.get(field.getName()), "%"+value+"%"));
				} else {
					Object value = field.get(filter);

					if (Objects.nonNull(value))
						predicates.add(builder.equal(root.get(field.getName()), value));
				}
			}
		} catch (IllegalAccessException error) {
			log.error("Erro ao montar a critéria de pesquisa: {}", error.getMessage());
			throw new MyWeatherInternal("Erro ao pesquisar registros");
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private void configurarPaginacao(TypedQuery<E> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistros = pageable.getPageSize();
		int primeiroRegistro = paginaAtual * totalRegistros;

		query.setFirstResult(primeiroRegistro);
		query.setMaxResults(totalRegistros);
	}

	private Long total(Filter filter, Pageable pageable) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<E> root = criteria.from(getDomainClass());

		Predicate[] predicates = montarPredicados(filter, builder, root);
		criteria.orderBy(QueryUtils.toOrders(pageable.getSort(), root, builder));
		criteria.where(predicates);

		criteria.select(builder.count(root));
		return em.createQuery(criteria).getSingleResult();
	}
}