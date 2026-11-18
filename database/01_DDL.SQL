-- VarreSP | Oracle Database | executar uma vez em esquema vazio.
-- Uma linha por distrito e semana em cada tabela de fatos.
CREATE TABLE distrito (
    id_distrito NUMBER(3)     NOT NULL,
    nome        VARCHAR2(80)  NOT NULL,
    CONSTRAINT pk_distrito PRIMARY KEY (id_distrito),
    CONSTRAINT uq_distrito_nome UNIQUE (nome)
);

CREATE TABLE demanda_semanal (
    id_distrito NUMBER(3) NOT NULL,
    semana      DATE      NOT NULL,
    pedidos     NUMBER(7) NOT NULL,
    CONSTRAINT pk_demanda PRIMARY KEY (id_distrito, semana),
    CONSTRAINT ck_demanda_volume CHECK (pedidos >= 0),
    CONSTRAINT ck_demanda_semana CHECK (semana = TRUNC(semana, 'IW'))
);

CREATE TABLE previsao_semanal (
    id_distrito    NUMBER(3)     NOT NULL,
    semana         DATE          NOT NULL,
    volume_previsto NUMBER(10,3) NOT NULL,
    prob_alta      NUMBER(7,6)   NOT NULL,
    limite_alta    NUMBER(7)     NOT NULL,
    CONSTRAINT pk_previsao PRIMARY KEY (id_distrito, semana),
    CONSTRAINT ck_prev_volume CHECK (volume_previsto >= 0),
    CONSTRAINT ck_prev_prob CHECK (prob_alta BETWEEN 0 AND 1),
    CONSTRAINT ck_prev_limite CHECK (limite_alta > 0),
    CONSTRAINT ck_prev_semana CHECK (semana = TRUNC(semana, 'IW'))
);

ALTER TABLE demanda_semanal ADD CONSTRAINT fk_demanda_distrito
    FOREIGN KEY (id_distrito) REFERENCES distrito (id_distrito);

ALTER TABLE previsao_semanal ADD CONSTRAINT fk_previsao_distrito
    FOREIGN KEY (id_distrito) REFERENCES distrito (id_distrito);
