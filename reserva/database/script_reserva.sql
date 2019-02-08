CREATE database IF NOT EXISTS reserva;
use reserva;

-- CREATE TABLES

CREATE TABLE estado (
	id INT AUTO_INCREMENT,
	descricao VARCHAR(150) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE cidade (
	id INT AUTO_INCREMENT,
	descricao VARCHAR(150) NOT NULL,
	estado_id INT NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (estado_id) REFERENCES estado (id)
);

CREATE TABLE local (
	id INT AUTO_INCREMENT,
	descricao VARCHAR(150) NOT NULL,
	cidade_id INT NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (cidade_id) REFERENCES cidade (id)
);

CREATE TABLE sala (
	id INT AUTO_INCREMENT,
	descricao VARCHAR(150) NOT NULL,
	local_id INT NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (local_id) REFERENCES local (id)
);

CREATE TABLE reserva (
	id INT AUTO_INCREMENT,
	dataInicio DATE NOT NULL,
	dataFim DATE NOT NULL,
	responsavel VARCHAR(150) NOT NULL,
	descricao VARCHAR(150) NOT NULL,
	incluirCafe boolean NOT NULL,
	quantidadePessoas INT,
	sala_id INT NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (sala_id) REFERENCES sala (id)
);

-- INSERT TABLES

-- INSERT ESTADO
INSERT INTO estado(descricao) VALUES ('Goiás');
INSERT INTO estado(descricao) VALUES ('São Paulo');
INSERT INTO estado(descricao) VALUES ('Rio de Janeiro');
INSERT INTO estado(descricao) VALUES ('Santa Catarina');

-- INSERT CIDADE
INSERT INTO cidade(descricao, estado_id) VALUES ('Goiânia', 1);
INSERT INTO cidade(descricao, estado_id) VALUES ('Anápolis', 1);
INSERT INTO cidade(descricao, estado_id) VALUES ('São Paulo', 2);
INSERT INTO cidade(descricao, estado_id) VALUES ('Rio de Janeiro', 3);
INSERT INTO cidade(descricao, estado_id) VALUES ('Florianópolis', 4);
INSERT INTO cidade(descricao, estado_id) VALUES ('Blumenau', 4);

INSERT INTO local(descricao, cidade_id) VALUES ('Shopping Flamboyant', 1);
INSERT INTO local(descricao, cidade_id) VALUES ('Shopping Passeio das Águas', 1);
INSERT INTO local(descricao, cidade_id) VALUES ('Centro Empresarial Portela', 2);
INSERT INTO local(descricao, cidade_id) VALUES ('Beira Mar Shopping', 5);

INSERT INTO sala(descricao, local_id) VALUES ('Sala 19', 1);
INSERT INTO sala(descricao, local_id) VALUES ('Sala 22', 1);
INSERT INTO sala(descricao, local_id) VALUES ('Sala 14', 1);
INSERT INTO sala(descricao, local_id) VALUES ('Sala 01', 2);
INSERT INTO sala(descricao, local_id) VALUES ('Sala 04', 2);
INSERT INTO sala(descricao, local_id) VALUES ('Sala 18', 4);
INSERT INTO sala(descricao, local_id) VALUES ('Sala 05', 4);
INSERT INTO sala(descricao, local_id) VALUES ('Sala 12', 4);



