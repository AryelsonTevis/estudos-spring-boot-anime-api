# EstudosSpringBoot

API REST desenvolvida com **Spring Boot** para estudo de conceitos de backend, com CRUD de **Animes** e **Producers**, tratamento de exceções customizado, validações e persistência em **MySQL**.

## 🚀 Tecnologias

- Java 27
- Spring Boot 4.1.1
- Spring Data JPA
- Spring Validation
- MySQL
- Lombok
- Docker / Docker Compose

## 📌 Funcionalidades

### Animes (`/animes`)
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/animes` | Lista todos os animes |
| GET | `/animes/{id}` | Busca um anime por ID |
| GET | `/animes/find?name=` | Busca animes por nome |
| POST | `/animes` | Cria um novo anime |
| PUT | `/animes` | Atualiza um anime existente |
| DELETE | `/animes/{id}` | Remove um anime |

### Producers (`/producers`)
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/producers` | Lista todos os produtores |
| GET | `/producers/{id}` | Busca um produtor por ID |
| POST | `/producers` | Cria um novo produtor |
| PUT | `/producers` | Atualiza um produtor existente |
| DELETE | `/producers/{id}` | Remove um produtor |

O projeto também conta com um `RestExceptionHandler` global para retornar respostas de erro padronizadas (validação, bad request, etc.).

## ⚙️ Como rodar o projeto

### Pré-requisitos
- Java 27+
- Maven (ou use o `./mvnw` incluso no projeto)
- Docker e Docker Compose

### Passo a passo

1. Clone o repositório:
   ```bash
   git clone https://github.com/AryelsonTevis/estudos-spring-boot-anime-api.git
   cd EstudosSpringBoot
   ```

2. Crie um arquivo `.env` na raiz do projeto (baseado no `.env.example`) com suas credenciais de banco:
   ```
   DB_USERNAME=root
   DB_PASSWORD=sua_senha
   DB_URL=jdbc:mysql://localhost:3306/anime_spring
   ```

3. Suba o banco de dados MySQL com Docker:
   ```bash
   docker-compose up -d
   ```

4. Rode a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```

5. A API estará disponível em `http://localhost:8080`.

## 📄 Licença

Este projeto está sob a licença MIT.
