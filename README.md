# 🚀 Portfólio Java Senior

> **Sistema de Gestão de Projetos e Equipes**  
> Aplicação completa para gerenciamento de projetos, funcionários, gerentes e alocação de membros em equipes.

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue.svg)](https://www.postgresql.org/)
[![Swagger](https://img.shields.io/badge/API-Swagger-85EA2D.svg)](http://localhost:8080/swagger-ui.html)

---

## 📋 Índice

- [🎯 Sobre o Projeto](#-sobre-o-projeto)
- [⚡ Funcionalidades](#-funcionalidades)
- [🛠️ Tecnologias](#️-tecnologias)
- [🚀 Como Executar](#-como-executar)
- [📚 Documentação da API](#-documentação-da-api)
- [🧪 Testes](#-testes)
- [📖 Regras de Negócio](#-regras-de-negócio)

---

## 🎯 Sobre o Projeto

O **Portfólio Java Senior** é uma aplicação robusta desenvolvida para demonstrar conhecimentos avançados em
desenvolvimento Java, implementando um sistema completo de gestão de projetos corporativos.

### 💡 Problema Resolvido

- Centralização do gerenciamento de projetos
- Controle de status e prazos
- Gestão de equipes e alocação de recursos
- Rastreabilidade de orçamentos e entregas

---

## ⚡ Funcionalidades

### 📊 Gestão de Projetos

- ✅ **CRUD Completo** - Criar, listar, atualizar e excluir projetos
- 📅 **Controle de Datas** - Início, previsão e conclusão
- 💰 **Gestão Orçamentária** - Controle de custos
- 🎯 **Classificação de Risco** - Baixo, médio e alto risco
- 📈 **Status Workflow** - 8 status diferentes com regras de transição

### 👥 Gestão de Pessoas

- 👨‍💼 **Cadastro de Gerentes** - Via web service
- 👩‍💻 **Cadastro de Funcionários** - Via web service
- 🔗 **Associação a Projetos** - Apenas funcionários podem ser membros

### 🛡️ Regras de Negócio

- 🚫 **Proteção contra Exclusão** - Projetos iniciados não podem ser excluídos
- ✅ **Validação de Dados** - Campos obrigatórios e regras consistentes
- 👥 **Gestão de Membros** - Somente funcionários em projetos

---

## 🛠️ Tecnologias

### Backend

- **Java 17** - Linguagem principal
- **Spring Boot 3.x** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Validation** - Validação de dados
- **Hibernate** - ORM

### Banco de Dados

- **H2** - Ambiente de desenvolvimento/testes
- **PostgreSQL** - Ambiente de produção

### Documentação e Testes

- **Swagger/OpenAPI 3** - Documentação interativa da API
- **JUnit 5** - Testes unitários
- **Mockito** - Mocks para testes
- **Spring Boot Test** - Testes de integração

### Build e Ferramentas

- **Maven** - Gerenciamento de dependências
- **Lombok** - Redução de boilerplate

---

## 🚀 Como Executar

### Pré-requisitos

- 🐳 Docker
- 🐙 Docker Compose

### Execução com Docker

```bash
# Opção 1: Script automático
./start.sh

# Opção 2: Manual
mvn clean package -DskipTests
docker-compose up --build
```

### Serviços

- **Aplicação:** http://localhost:8080
- **PostgreSQL:** localhost:5433 (database: portfolio, user: portfolio_user, senha: portfolio_pass)
- **Swagger:** http://localhost:8080/swagger-ui.html

### Comandos úteis

```bash
# Parar containers
docker-compose down

# Limpar tudo (incluindo volumes)
docker-compose down -v

# Ver logs
docker-compose logs -f app
```

### 4️⃣ Acesse a aplicação

- **Aplicação:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html

---

## 📚 Documentação da API

### 🔗 Endpoints Principais

| Método   | Endpoint         | Descrição               |
|----------|------------------|-------------------------|
| `GET`    | `/`              | Lista todos os projetos |
| `POST`   | `/projetos`      | Cria um novo projeto    |
| `GET`    | `/projetos/{id}` | Busca projeto por ID    |
| `PUT`    | `/projetos/{id}` | Atualiza um projeto     |
| `DELETE` | `/projetos/{id}` | Exclui um projeto       |
| `POST`   | `/api/pessoas`   | Cadastra nova pessoa    |

### 📖 Documentação Completa

Acesse o **Swagger UI** em http://localhost:8080/swagger-ui.html para documentação interativa completa com exemplos.


---

## 🧪 Testes

### Executar todos os testes

```bash
mvn test
```

### Executar testes específicos

```bash
# Testes unitários do service
mvn -Dtest=ProjetoServiceTest test

# Testes de integração do controller
mvn -Dtest=ProjetoControllerTest test
```

### 📊 Cobertura de Testes

- ✅ **Testes Unitários** - Services e regras de negócio
- ✅ **Testes de Integração** - Controllers e repositories
- ✅ **Testes de Validação** - DTOs e anotações
- ✅ **Cenários de Erro** - Exceções e edge cases

---

## 📖 Regras de Negócio

### 🏗️ Projetos

- **Status Permitidos:** `EM_ANALISE` → `ANALISE_REALIZADA` → `APROVADA` → `INICIADO` → `PLANEJADO` → `EM_ANDAMENTO` →
  `ENCERRADO/CANCELADO`
- **Proteção:** Projetos `INICIADO`, `EM_ANDAMENTO` ou `ENCERRADO` não podem ser excluídos
- **Classificação:** Todo projeto tem risco: `BAIXO`, `MEDIO` ou `ALTO`
- **Datas:** Data de previsão não pode ser anterior ao início

### 👥 Pessoas e Membros

- **Cadastro:** Apenas via web service
- **Atribuições:** `FUNCIONARIO` ou `GERENTE`
- **Restrição:** Somente funcionários podem ser membros de projetos
- **Gerentes:** Responsáveis pelos projetos

---