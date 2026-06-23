ALTER TABLE funcionarios
    ADD COLUMN IF NOT EXISTS email VARCHAR(150),
    ADD COLUMN IF NOT EXISTS telefone VARCHAR(50);

ALTER TABLE vendas
    ADD COLUMN IF NOT EXISTS valor_pago DECIMAL(15, 2),
    ADD COLUMN IF NOT EXISTS troco DECIMAL(15, 2);

ALTER TABLE vendas
    MODIFY COLUMN forma_pagamento VARCHAR(50) NOT NULL;

CREATE TABLE IF NOT EXISTS movimentacoes_estoque (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    produto_id BIGINT NOT NULL,
    quantidade INT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    data_movimentacao DATETIME NOT NULL,
    observacao VARCHAR(250),
    CONSTRAINT fk_movimentacao_produto FOREIGN KEY (produto_id) REFERENCES produtos(id)
);
