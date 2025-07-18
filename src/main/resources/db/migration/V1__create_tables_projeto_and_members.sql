-- 1. Pessoa
CREATE TABLE IF NOT EXISTS pessoa (
    id            BIGSERIAL PRIMARY KEY,
    nome          VARCHAR(100) NOT NULL,
    datanascimento DATE,
    cpf           VARCHAR(14),
    funcionario   BOOLEAN NOT NULL DEFAULT FALSE,
    gerente       BOOLEAN NOT NULL DEFAULT FALSE
);

-- 2. Projeto
CREATE TABLE IF NOT EXISTS projeto (
    id                BIGSERIAL PRIMARY KEY,
    nome              VARCHAR(200) NOT NULL,
    data_inicio       DATE,
    data_previsao_fim DATE,
    data_fim          DATE,
    descricao         VARCHAR(5000),
    status            VARCHAR(45),
    orcamento         FLOAT,
    risco             VARCHAR(45),
    idgerente         BIGINT NOT NULL,
    CONSTRAINT fk_projeto_gerente
     FOREIGN KEY (idgerente)
         REFERENCES pessoa(id)
         ON UPDATE NO ACTION
         ON DELETE NO ACTION
);

-- 3. Membros (relacionamento N:M entre projeto e pessoa)
CREATE TABLE IF NOT EXISTS membros (
    projeto_id BIGINT NOT NULL,
    pessoa_id  BIGINT NOT NULL,
    PRIMARY KEY (projeto_id, pessoa_id),
    CONSTRAINT fk_membros_projeto
       FOREIGN KEY (projeto_id)
           REFERENCES projeto(id)
           ON DELETE CASCADE,
    CONSTRAINT fk_membros_pessoa
       FOREIGN KEY (pessoa_id)
           REFERENCES pessoa(id)
           ON DELETE CASCADE
);