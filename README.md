# 🛒 E-commerce API com Spring Boot & JWT

API RESTful desenvolvida em **Spring Boot** para a gestão de um sistema de e-commerce, focada em segurança com **Spring Security** e autenticação baseada em **JWT (JSON Web Token)**.

---

## 🚀 Tecnologias Utilizadas

* **Java 17+**
* **Spring Boot**
* **Spring Security & JWT** (Autenticação e Autorização)
* **Spring Data JPA / Hibernate** (Persistência de Dados)
* **H2 Database** (Base de dados em memória para testes)
* **Maven** (Gestão de dependências)

---

## 📂 Estrutura de Entidades

* **`Usuario`**: Gestão de utilizadores, credenciais e papéis (`ROLE_ADMIN`, `ROLE_CLIENTE`).
* **`Produto`**: Gestão de catalogo de produtos, preços e controlo de stock.
* **`Pedido`**: Registo de transações efetuadas pelos utilizadores autenticados.
* **`ItemPedido`**: Associação de produtos e quantidades dentro de cada pedido.

---

## 🔌 Endpoints da API

### 🔐 Autenticação (`/auth`)
* `POST /auth/registrar` - Registo de novo utilizador.
* `POST /auth/login` - Autenticação e obtenção do Token JWT.

### 📦 Produtos (`/produtos`)
* `GET /produtos` - Lista todos os produtos com suporte a paginação e ordenação (Público).
* `POST /produtos` - Registo de novo produto (Exclusivo para administradores).

### 🛍️ Pedidos (`/pedidos`)
* `POST /pedidos` - Criação de um novo pedido validando o stock e autenticação.
* `GET /pedidos/meus` - Consulta o histórico de pedidos exclusivo do utilizador autenticado.
* `GET /pedidos` - Lista todos os pedidos do sistema.

---

## ⚙️ Como Executar o Projeto Localmente

1. Clone o repositório:
   ```bash
   git clone [https://github.com/MateusStecca/spring-boot-ecommerce-api.git](https://github.com/MateusStecca/spring-boot-ecommerce-api.git)
