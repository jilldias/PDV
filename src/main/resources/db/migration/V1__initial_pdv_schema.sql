CREATE TABLE categorias (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(255)
);

CREATE TABLE funcionarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    cargo VARCHAR(100),
    login VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    email VARCHAR(150),
    telefone VARCHAR(50)
);

CREATE TABLE clientes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    cpf VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(150),
    telefone VARCHAR(50),
    endereco VARCHAR(255)
);

CREATE TABLE produtos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    codigo_barras VARCHAR(64) NOT NULL UNIQUE,
    preco DECIMAL(15, 2) NOT NULL,
    estoque INT NOT NULL,
    ativo BOOLEAN NOT NULL,
    data_cadastro DATETIME NOT NULL,
    categoria_id BIGINT NOT NULL,
    CONSTRAINT fk_produto_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

CREATE TABLE caixas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    data_abertura DATETIME NOT NULL,
    data_fechamento DATETIME,
    valor_inicial DECIMAL(15, 2) NOT NULL,
    valor_final DECIMAL(15, 2),
    status VARCHAR(20) NOT NULL,
    funcionario_id BIGINT NOT NULL,
    CONSTRAINT fk_caixa_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id)
);

CREATE TABLE vendas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    data_venda DATETIME NOT NULL,
    valor_total DECIMAL(15, 2) NOT NULL,
    valor_pago DECIMAL(15, 2),
    troco DECIMAL(15, 2),
    status VARCHAR(20) NOT NULL,
    funcionario_id BIGINT NOT NULL,
    caixa_id BIGINT,
    cliente_id BIGINT,
    forma_pagamento VARCHAR(50) NOT NULL,
    CONSTRAINT fk_venda_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id),
    CONSTRAINT fk_venda_caixa FOREIGN KEY (caixa_id) REFERENCES caixas(id),
    CONSTRAINT fk_venda_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE TABLE itens_venda (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    produto_id BIGINT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(15, 2) NOT NULL,
    subtotal DECIMAL(15, 2) NOT NULL,
    venda_id BIGINT NOT NULL,
    CONSTRAINT fk_itemvenda_produto FOREIGN KEY (produto_id) REFERENCES produtos(id),
    CONSTRAINT fk_itemvenda_venda FOREIGN KEY (venda_id) REFERENCES vendas(id)
);

CREATE TABLE movimentacoes_estoque (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    produto_id BIGINT NOT NULL,
    quantidade INT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    data_movimentacao DATETIME NOT NULL,
    observacao VARCHAR(250),
    CONSTRAINT fk_movimentacao_produto FOREIGN KEY (produto_id) REFERENCES produtos(id)
);
