package br.com.hq.myweather.api.utils;

import static java.util.Objects.nonNull;

import java.util.Collection;
import java.util.Objects;

import br.com.hq.myweather.api.model.BaseEntity;

public class Utils {
	
	private Utils() {}
	
	/**
	 * Valida se um identificador é válido. Um identificador será válido se for maior que zero
	 * @param id {@link Long} IDentificador a ser validado
	 * @return {@literal boolean} <code>true</code> se for um identificador válido, <code>false</code> caso contrário
	 */
	public static boolean isValidId(Long id) {
		return nonNull(id) && id > 0L;
	}
	
	/**
	 * Valida se uma entidade é válida. Uma entidade é válida se não for nula e se possuir um identificador válido
	 * @param entity {@link BaseEntity} Entidade a ser validada
	 * @return {@literal boolean} <code>true</code> se for uma entidade válida, <code>false</code> caso contrário
	 */
	public static boolean isValidEntity(BaseEntity entity) {
		return nonNull(entity) && isValidId(entity.getId());
	}
	
	/**
	 * Verifica se uma coleção é válida. Uma coleção será válida se:
	 * <ol>
	 * <li>não for nula</li>
	 * <li>não estiver vazia</li>
	 * <li>não contiver valores nulos</li>
	 * </ol>
	 * @param list {@link Collection} Lista a ser verificada
	 * @return {@literal boolean} <code>true</code> se for uma coleção válida, <code>false</code> caso contrário
	 */
	public static boolean isValidList(Collection<?> list) {
		return nonNull(list) && !list.isEmpty() && list.stream().noneMatch(Objects::isNull);
	}
}
