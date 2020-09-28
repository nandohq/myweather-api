package br.com.hq.myweather.api.model;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe de modelo que representa a tabela de países
 * 
 * @author Fernando Souza
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicUpdate
@Table(name = "mw_pais")
@AttributeOverride(column = @Column(name = "pai_id"), name = "id")
@AttributeOverride(column = @Column(name = "pai_nome"), name = "nome")
public class Pais extends BaseEntity {

	@NotEmpty
	@Size(min = 2, max = 2)
	@Column(name = "pai_sigla_2", nullable = false, length = 2)
	private String sigla2;

	@Size(min = 3, max = 3)
	@Column(name = "pai_sigla_3", nullable = false, length = 3)
	private String sigla3;
	
	@PostLoad
	private void upper() {
		this.sigla2 = this.sigla2.toUpperCase();
		this.sigla3 = this.sigla3.toUpperCase();
	}

	@Override
	public String getLabel() {
		StringBuilder label = new StringBuilder(super.getLabel());

		if (isNotBlank(this.sigla2))
			label.append(" (").append(this.sigla2).append(")");

		return label.toString();
	}
}