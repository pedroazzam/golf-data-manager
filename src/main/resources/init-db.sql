DROP TABLE IF EXISTS pessoal;
CREATE TABLE pessoal (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nome VARCHAR(50) UNIQUE, email VARCHAR(100) UNIQUE, cpf VARCHAR(15) UNIQUE, codigo_assessor VARCHAR(10) UNIQUE, codigo_banco INTEGER, agencia VARCHAR(50), conta VARCHAR(50));
DROP TRIGGER IF EXISTS pessoal_insert_cod_assessor;
CREATE TRIGGER IF NOT EXISTS pessoal_insert_cod_assessor AFTER INSERT ON pessoal WHEN NEW.codigo_assessor ISNULL OR NEW.codigo_assessor = '' OR upper(substr(NEW.codigo_assessor,1,1)) <> 'A' BEGIN UPDATE pessoal SET codigo_assessor = ('P' || (SELECT ifnull((SELECT seq FROM sqlite_sequence WHERE name = 'pessoal'),0) +1)) WHERE id = NEW.id; END;
DROP TRIGGER IF EXISTS pessoal_update_cod_assessor;
CREATE TRIGGER IF NOT EXISTS pessoal_update_cod_assessor AFTER UPDATE ON pessoal WHEN NEW.codigo_assessor ISNULL OR NEW.codigo_assessor = '' OR upper(substr(NEW.codigo_assessor,1,1)) <> 'A' BEGIN UPDATE pessoal SET codigo_assessor = ('P' || NEW.id) WHERE id = NEW.id; END;
DROP TRIGGER IF EXISTS pessoal_insert_validations;
CREATE TRIGGER IF NOT EXISTS pessoal_insert_validations BEFORE INSERT ON pessoal BEGIN	SELECT CASE	WHEN NEW.email NOT LIKE '%_@__%.__%' THEN RAISE (ABORT, 'Endereço de e-mail inválido')	END; SELECT CASE WHEN NEW.cpf NOT LIKE '___.___.___-__' THEN RAISE (ABORT, 'Formato de CPF inválido') END; END;
DROP TRIGGER IF EXISTS pessoal_update_validations;
CREATE TRIGGER IF NOT EXISTS pessoal_update_validations BEFORE UPDATE ON pessoal BEGIN	SELECT CASE	WHEN NEW.email NOT LIKE '%_@__%.__%' THEN RAISE (ABORT, 'Endereço de e-mail inválido')	END; SELECT CASE WHEN NEW.cpf NOT LIKE '___.___.___-__' THEN RAISE (ABORT, 'Formato de CPF inválido') END; END;
DROP TABLE IF EXISTS produto;
CREATE TABLE produto (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nome VARCHAR(100) NOT NULL UNIQUE, tag_col_produto VARCHAR(100), cond_tn1 TEXT CHECK( cond_tn1 IN ('OU','E') ) NOT NULL DEFAULT 'OU', tag_col_nivel01 VARCHAR(100), cond_tn2 TEXT CHECK( cond_tn2 IN ('OU','E') ) NOT NULL DEFAULT 'OU', tag_col_nivel02 VARCHAR(100), cond_tn3 TEXT CHECK( cond_tn3 IN ('OU','E') ) NOT NULL DEFAULT 'OU', tag_col_nivel03 VARCHAR(100));
DROP TABLE IF EXISTS produto_capitao;
CREATE TABLE produto_capitao (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, id_produto INTEGER, id_pessoal INTEGER, comissao NUMBER, CONSTRAINT UQ_columns UNIQUE (id_produto, id_pessoal), FOREIGN KEY (id_pessoal) REFERENCES pessoal(id) ON DELETE CASCADE, FOREIGN KEY (id_produto) REFERENCES produto(id) ON DELETE CASCADE);
DROP TABLE IF EXISTS assessor_xerife;
CREATE TABLE assessor_xerife (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, id_assessor INTEGER NOT NULL UNIQUE, id_pessoal INTEGER NOT NULL, comissao NUMBER DESCRIPTION 'Percentual do que é recebido pela Golf. Exemplo: A Golf recebe 70% do total e quero gerar esta comissão em 21% do total, logo tenho que inserir 0,3 pois 21% é 30% de 70%.');
DROP TABLE IF EXISTS produto_regra_comissionamento;
CREATE TABLE produto_regra_comissionamento (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,	id_produto INTEGER NOT NULL UNIQUE,	regra TEXT CHECK( regra IN ('PROPORCIONAL','ASSESSOR','GOLF') )   NOT NULL DEFAULT 'PROPORCIONAL');
DROP TABLE IF EXISTS xerife_regra_comissionamento;
CREATE TABLE xerife_regra_comissionamento (	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,	id_xerife INTEGER NOT NULL UNIQUE,	regra TEXT CHECK( regra IN ('PROPORCIONAL','ASSESSOR','GOLF') )   NOT NULL DEFAULT 'PROPORCIONAL');