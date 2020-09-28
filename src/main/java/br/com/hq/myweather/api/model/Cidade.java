package br.com.hq.myweather.api.model;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe de modelo que representa a tabela de cidades
 * 
 * @author Fernando Souza
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicUpdate
@Table(name = "mw_cidade")
@AttributeOverride(column = @Column(name = "cid_id"), name = "id")
@AttributeOverride(column = @Column(name = "cid_nome"), name = "nome")
public class Cidade extends BaseEntity {

	@Column(name = "cid_descricao", columnDefinition = "text")
	private String descricao;

	@Column(name = "cid_longitude", nullable = false, precision = 2, scale = 10)
	private Double longitude;

	@Column(name = "cid_latitude", nullable = false, precision = 2, scale = 10)
	private Double latitude;

	@OneToOne
	@JoinColumn(name = "cid_pais", nullable = false)
	private Pais pais;

	@Override
	public String getLabel() {
		StringBuilder label = new StringBuilder(super.getLabel());
		
		if (nonNull(this.pais) && isNotBlank(pais.getSigla2()) && label.length() > 0)
			label.append(" (").append(pais.getSigla2()).append(")");
		
		return label.toString();
	}
}