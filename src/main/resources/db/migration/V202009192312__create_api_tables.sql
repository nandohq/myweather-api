CREATE TABLE IF NOT EXISTS mw_pais (
  pai_id INT(11) NOT NULL AUTO_INCREMENT,
  pai_nome VARCHAR(100) NOT NULL,
  pai_sigla_2 char(2) NOT NULL,
  pai_sigla_3 char(3) NOT NULL,
  
  PRIMARY KEY (pai_id),
  INDEX idx_pai_nome (pai_nome),
  INDEX idx_pai_sigla_2 (pai_sigla_2)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS mw_cidade (
	cid_id BIGINT (20) NOT NULL AUTO_INCREMENT,
	cid_nome VARCHAR (100) NOT NULL,
	cid_descricao TEXT,
	cid_longitude NUMERIC (10,6) NOT NULL,
	cid_latitude NUMERIC (10,6) NOT NULL,
	cid_pais INT (11) NOT NULL,
	
	PRIMARY KEY (cid_id),
	INDEX idx_cid_nome (cid_nome),
	INDEX idx_cid_pais (cid_pais),
	CONSTRAINT fk_cidade_pais FOREIGN KEY (cid_pais) REFERENCES mw_pais (pai_id)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB;