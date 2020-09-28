package br.com.hq.myweather.api.model;

import static br.com.hq.myweather.api.utils.Utils.isValidId;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@MappedSuperclass
public abstract class BaseEntity implements Persistable<Long> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	
	@NotEmpty
	@Size(min = 3, max=100)
	@Column(nullable = false, length = 100)
	@EqualsAndHashCode.Include
	private String nome;
	
	@JsonIgnore
	@Override
	public boolean isNew() {
		return !isValidId(this.id);
	}
	
	public String getLabel() {
		StringBuilder label = new StringBuilder("");
		
		if (isValidId(this.id))
			label.append(this.id);
		
		if (isNotBlank(this.nome)) {
			label.append(label.length() > 0 ? " - " : "");
			label.append(this.nome);
		}
		
		return label.toString();
	}
}