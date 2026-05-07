# TaskFlow API

API REST para gestão de projetos, tarefas, membros e comentários, com autenticação JWT.

## Sumário
- [Visão geral](#visão-geral)
- [Stack e arquitetura](#stack-e-arquitetura)
- [Funcionalidades](#funcionalidades)
- [Pré-requisitos](#pré-requisitos)
- [Configuração de ambiente](#configuração-de-ambiente)
- [Como executar](#como-executar)
- [Documentação da API (Swagger)](#documentação-da-api-swagger)
- [Autenticação](#autenticação)
- [Endpoints principais](#endpoints-principais)
- [Regras de negócio importantes](#regras-de-negócio-importantes)
- [Estrutura de pastas](#estrutura-de-pastas)
- [Testes](#testes)

## Visão geral
O TaskFlow organiza trabalho em projetos com membros e papéis, permitindo:
- cadastro e login de usuários;
- criação e gestão de projetos;
- gestão de membros por papel (OWNER, MEMBER, VIEWER);
- criação e acompanhamento de tarefas com prioridade, prazo, responsável e status;
- comentários em tarefas.

## Stack e arquitetura
- **Java 21**
- **Spring Boot 4**
- **Spring Web MVC**
- **Spring Security + JWT**
- **Spring Data JPA**
- **PostgreSQL**
- **Swagger/OpenAPI (springdoc)**
- **Testcontainers (testes)**

Arquitetura em camadas com separação por domínio:
- `domain`: entidades, regras e casos de uso;
- `infra/persistence`: entidades JPA, repositórios e adapters;
- `infra/web`: controllers, DTOs e mapeadores;
- `infra/security`: autenticação/autorização JWT.

## Funcionalidades
- Autenticação:
  - `POST /api/auth/register`
  - `POST /api/auth/login`
- Usuário autenticado:
  - consultar perfil
  - atualizar perfil
  - trocar senha
- Projetos:
  - criar, atualizar, listar, detalhar e arquivar
  - adicionar e remover membros
  - listar tarefas de um projeto com filtros
- Tarefas:
  - criar (via projeto), atualizar, consultar e remover
  - alterar status
  - atribuir responsável
  - comentar e remover comentário

## Pré-requisitos
- JDK 21
- Docker e Docker Compose (para banco e/ou execução em container)

## Configuração de ambiente
Crie um arquivo `.env` (execução local) com base em `.env.example`:

```env
DB_URL=jdbc:postgresql://localhost:5432/taskflow
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=sua_chave_com_no_minimo_32_caracteres
JWT_EXPIRATION=86400000
```

> `JWT_SECRET` precisa ter pelo menos 32 bytes para HS256.

Para Docker Compose, crie também `.env.docker` (referenciado no `docker-compose.yml`) com as mesmas variáveis.

## Como executar

### 1) Banco de dados com Docker
```bash
docker compose up -d db
```

### 2) Aplicação local (sem container)
```bash
./mvnw spring-boot:run
```

Se o script não tiver permissão de execução:
```bash
chmod +x mvnw
./mvnw spring-boot:run
```

### 3) Aplicação + banco com Docker Compose
```bash
docker compose up --build
```

A API sobe em `http://localhost:8080`.

## Documentação da API (Swagger)
- UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Autenticação
- Rotas públicas:
  - `/api/auth/**`
  - `/swagger-ui/**`
  - `/v3/api-docs/**`
- Demais rotas exigem token JWT.
- Envie no header:
  - `Authorization: Bearer <seu_token>`

## Endpoints principais

### Auth
- `POST /api/auth/register`
- `POST /api/auth/login`

### Users
- `GET /api/users/me`
- `PUT /api/users/me`
- `PUT /api/users/me/password`

### Projects
- `POST /api/projects`
- `GET /api/projects`
- `GET /api/projects/{id}`
- `PUT /api/projects/{id}`
- `PATCH /api/projects/{id}/archive`
- `POST /api/projects/{id}/members`
- `DELETE /api/projects/{id}/members`
- `GET /api/projects/{projectId}/tasks?page=0&size=10&status=&priority=&deadline=YYYY-MM-DD`
- `POST /api/projects/{projectId}/tasks`

### Tasks
- `GET /api/tasks/{id}`
- `PUT /api/tasks/{id}`
- `PATCH /api/tasks/{id}/status`
- `PATCH /api/tasks/{id}/assign`
- `DELETE /api/tasks/{id}`
- `POST /api/tasks/{id}/comments`
- `DELETE /api/tasks/{id}/comments/{commentId}`

## Regras de negócio importantes
- **Papéis no projeto**
  - `OWNER`: controle total (inclui membros e arquivamento)
  - `MEMBER`: pode editar/criar tarefas e editar projeto
  - `VIEWER`: somente leitura
- **Membros**
  - só `OWNER` adiciona/remove membros;
  - `OWNER` não pode remover a si mesmo.
- **Tarefas**
  - não permite prazo no passado ao criar;
  - tarefas `DONE`/`CANCELLED` não podem ser editadas nem atribuídas;
  - transições de status válidas:
    - `TODO -> IN_PROGRESS | CANCELLED`
    - `IN_PROGRESS -> IN_REVIEW | TODO | CANCELLED`
    - `IN_REVIEW -> DONE | IN_PROGRESS`
    - `DONE` e `CANCELLED` não transitam.
- **Comentários**
  - apenas autor remove o próprio comentário.

## Estrutura de pastas
```text
src/
  main/
    java/kaua/felix/taskflow/
      domain/
      infra/
        config/
        persistence/
        security/
        web/
    resources/
      application.properties
  test/
    java/kaua/felix/taskflow/
```

## Testes
Executar:
```bash
./mvnw test
```

Os testes usam **Testcontainers** para PostgreSQL.
