# PDV Desktop Java

> **Status do Projeto:** 🚧 Em desenvolvimento
>
> Este projeto encontra-se em fase ativa de desenvolvimento, recebendo melhorias contínuas, correções de arquitetura, refatorações de código e adequações estruturais com o objetivo de torná-lo mais robusto, escalável e alinhado às boas práticas de desenvolvimento de software. Algumas funcionalidades ainda estão em processo de implementação e otimização.

## Sobre o Projeto

O **PDV Desktop Java** é um sistema de Ponto de Venda (PDV) desenvolvido para gerenciamento de vendas, produtos, estoque e operações comerciais em ambiente desktop.

O projeto foi construído utilizando Java moderno e tecnologias do ecossistema Spring, buscando aplicar conceitos de desenvolvimento corporativo, persistência de dados, arquitetura em camadas e interfaces gráficas modernas.

## Objetivos

* Gerenciar produtos e categorias.
* Controlar estoque.
* Realizar vendas.
* Gerar relatórios gerenciais.
* Gerenciar funcionários e usuários do sistema.
* Aplicar boas práticas de desenvolvimento Java.
* Servir como projeto de estudo e evolução profissional.

---

# Tecnologias Utilizadas

## Linguagem

* Java 21

## Interface Gráfica

* JavaFX

## Framework

* Spring Boot
* Spring Context
* Spring Data JPA

## Persistência de Dados

* Hibernate ORM
* JPA (Jakarta Persistence API)

## Banco de Dados

* MariaDB

## Gerenciamento de Dependências

* Maven

## Controle de Versão

* Git
* GitHub

---

# Arquitetura

O projeto segue uma arquitetura em camadas para promover organização, manutenção e escalabilidade.

```text
src
├── controller
├── service
├── repository
├── model
├── config
├── javafx
│   ├── controller
│   └── StageManager
├── resources
│   ├── css
│   └── fxml
└── exception
```

### Camadas

#### Controller

Responsável por receber as ações da interface e encaminhar para a camada de serviço.

#### Service

Contém as regras de negócio da aplicação.

#### Repository

Responsável pelo acesso aos dados utilizando Spring Data JPA.

#### Model

Entidades do sistema mapeadas para o banco de dados.

#### JavaFX

Responsável pela interface gráfica e navegação entre telas.

---

# Funcionalidades Implementadas

## Dashboard

* Tela inicial do sistema.
* Navegação entre módulos.

## Produtos

* Cadastro de produtos.
* Consulta de produtos.
* Controle de estoque.

## Categorias

* Cadastro de categorias.
* Associação de categorias aos produtos.

## Vendas

* Registro de vendas.
* Atualização de estoque.

## Relatórios

* Estrutura inicial para geração de relatórios.

---

# Funcionalidades Planejadas

* Edição de produtos.
* Exclusão lógica de registros.
* Controle de usuários e permissões.
* Auditoria de operações.
* Relatórios avançados.
* Exportação para PDF.
* Dashboard com métricas de vendas.
* Controle financeiro.
* Melhorias visuais na interface.
* Tratamento global de exceções.
* Testes automatizados.

---

# Requisitos

* Java 21 ou superior
* Maven 3.9+
* MariaDB
* Git

---

# Configuração do Banco de Dados

Criar um banco MariaDB:

```sql
CREATE DATABASE pdv;
```

Configurar o arquivo:

```properties
application.properties
```

Exemplo:

```properties
spring.datasource.url=jdbc:mariadb://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:pdv}
spring.datasource.username=${DB_USER:}
spring.datasource.password=${DB_PASSWORD:}

spring.jpa.hibernate.ddl-auto=${JPA_DDL_AUTO:update}
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
```

---

# Executando o Projeto

Clonar o repositório:

```bash
git clone https://github.com/jilldias/pdv.git
```

Entrar na pasta:

```bash
cd PDVJILL
```

Compilar:

```bash
mvn clean install
```

Executar:

```bash
mvn javafx:run
```

---

# Boas Práticas Aplicadas

* Programação Orientada a Objetos (POO)
* Arquitetura em Camadas
* Injeção de Dependência
* Separação de Responsabilidades
* Persistência com JPA/Hibernate
* Uso de Maven para gerenciamento de dependências
* Versionamento com Git

---

# Roadmap

* [ ] Refatoração geral da arquitetura
* [ ] Melhorias visuais das telas JavaFX
* [ ] Implementação completa do módulo de vendas
* [ ] Implementação completa do módulo de estoque
* [ ] Sistema de autenticação
* [ ] Relatórios avançados
* [ ] Testes automatizados
* [ ] Documentação técnica completa

---

# Autor

Desenvolvido por **Jillian Dias** como projeto de aprendizado, prática de desenvolvimento Java e evolução para aplicações corporativas desktop.

---

# Licença

Este projeto possui finalidade educacional e de estudo.
