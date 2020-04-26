/*Construir tabelas*/

DROP TABLE IF EXISTS pessoal;
CREATE TABLE pessoal (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nome VARCHAR(50) UNIQUE, email VARCHAR(100) UNIQUE, cpf VARCHAR(15) UNIQUE, codigo_assessor VARCHAR(10), codigo_banco INTEGER, agencia VARCHAR(50), conta VARCHAR(50));

-- pessoal, contas_bancarias e assessor estão unificadas em uma tabela agora
/* DROP TABLE IF EXISTS pessoal;
CREATE TABLE pessoal (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nome VARCHAR(50) UNIQUE, email VARCHAR(100) UNIQUE, cpf VARCHAR(15) UNIQUE);

DROP TABLE IF EXISTS contas_bancarias;
CREATE TABLE contas_bancarias (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, id_pessoal INTEGER NOT NULL, codigo_banco INTEGER, agencia VARCHAR(50), conta VARCHAR(50));

DROP TABLE IF EXISTS assessor;
CREATE TABLE assessor (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, id_pessoal INTEGER UNIQUE, codigo VARCHAR(10), comissao NUMBER);
 */
 
 
/*DROP TABLE IF EXISTS capitao;
CREATE TABLE capitao (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nome VARCHAR(50) UNIQUE, email VARCHAR(100) UNIQUE);

DROP TABLE IF EXISTS xerife;
CREATE TABLE xerife (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nome VARCHAR(50) UNIQUE, email VARCHAR(100) UNIQUE);
*/

DROP TABLE IF EXISTS produto;
CREATE TABLE produto (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nome VARCHAR(100) UNIQUE, tag_col_produto VARCHAR(100), cond_tn1 TEXT CHECK( cond_tn1 IN ('OU','E') ) NOT NULL DEFAULT 'OU', tag_col_nivel01 VARCHAR(100), cond_tn2 TEXT CHECK( cond_tn2 IN ('OU','E') ) NOT NULL DEFAULT 'OU', tag_col_nivel02 VARCHAR(100), cond_tn3 TEXT CHECK( cond_tn3 IN ('OU','E') ) NOT NULL DEFAULT 'OU', tag_col_nivel03 VARCHAR(100));
-- produto, tag_col_nivel01, tag_col_nivel02, tag_col_nivel03 e tag_col_produto são agora unificadas e com colunas adicionais de condições para a interação das tags com o produto
/* DROP TABLE IF EXISTS tag_col_produto;
CREATE TABLE tag_col_produto (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, tag VARCHAR(50));

DROP TABLE IF EXISTS tag_col_nivel01;
CREATE TABLE tag_col_nivel01 (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, tag VARCHAR(50));

DROP TABLE IF EXISTS tag_col_nivel02;
CREATE TABLE tag_col_nivel02 (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, tag VARCHAR(50));

DROP TABLE IF EXISTS tag_col_nivel03;
CREATE TABLE tag_col_nivel03 (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, tag VARCHAR(50));

DROP TABLE IF EXISTS produto;
-- CREATE TABLE produto (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nome VARCHAR(50) UNIQUE);
CREATE TABLE produto (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nome VARCHAR(50) UNIQUE, id_tag_col_produto INTEGER, id_tag_col_nivel01 INTEGER, id_tag_col_nivel02 INTEGER, id_tag_col_nivel03 INTEGER);
 */
 
 
 
/*DROP TABLE IF EXISTS prod_subproduto;
CREATE TABLE prod_subproduto (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, id_produto INTEGER NOT NULL, nome VARCHAR(50) UNIQUE);
*/

DROP TABLE IF EXISTS produto_capitao;
CREATE TABLE produto_capitao (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, id_produto INTEGER UNIQUE, id_pessoal INTEGER, comissao NUMBER DESCRIPTION 'Percentual do que é recebido pela Golf. Exemplo: A Golf recebe 70% do total e quero gerar esta comissão em 21% do total, logo tenho que inserir 0,3 pois 21% é 30% de 70%.');

DROP TABLE IF EXISTS assessor_xerife;
CREATE TABLE assessor_xerife (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, id_assessor INTEGER NOT NULL UNIQUE, id_pessoal INTEGER NOT NULL, comissao NUMBER DESCRIPTION 'Percentual do que é recebido pela Golf. Exemplo: A Golf recebe 70% do total e quero gerar esta comissão em 21% do total, logo tenho que inserir 0,3 pois 21% é 30% de 70%.');


/*O xerife de produto se trata de apenas uma solucao contabil para a excessao de um capitao de produto ser remunerado 
apenas pela Golf, como ocorre com o xerife de assessor*/
/*DROP TABLE IF EXISTS produto_xerife;
CREATE TABLE produto_xerife (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, id_produto INTEGER NOT NULL UNIQUE, id_xerife INTEGER NOT NULL);
*/

/*Regras de comissionamentos por produto (para os capitaes)*/
DROP TABLE IF EXISTS produto_regra_comissionamento;
CREATE TABLE produto_regra_comissionamento (
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	id_produto INTEGER NOT NULL UNIQUE,
	regra TEXT CHECK( regra IN ('PROPORCIONAL','ASSESSOR','GOLF') )   NOT NULL DEFAULT 'PROPORCIONAL'
);

/*Regras de comissionamentos por xerife*/
DROP TABLE IF EXISTS xerife_regra_comissionamento;
CREATE TABLE xerife_regra_comissionamento (
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	id_xerife INTEGER NOT NULL UNIQUE,
	regra TEXT CHECK( regra IN ('PROPORCIONAL','ASSESSOR','GOLF') )   NOT NULL DEFAULT 'PROPORCIONAL'
);