# 🗣️ Fórum Hub API

**Fórum Hub** é uma API REST desenvolvida em Java com Spring Boot que permite gerenciar tópicos de discussão em um fórum. O projeto implementa autenticação JWT, autorização de usuários e operações CRUD completas para tópicos.

Este projeto foi desenvolvido como parte da Formação ONE (Oracle Next Education) em parceria com a Alura, do desafio **Fórum Hub**, focando em:
- Desenvolvimento de APIs REST
- Autenticação e Autorização com JWT
- Segurança com Spring Security
- Persistência de dados com JPA/Hibernate
- Validação de dados e tratamento de exceções

---

## Funcionalidades

- ✅ **Autenticação JWT** - Login seguro com geração de token
- ✅ **Criar tópico** - Usuários autenticados podem criar novos tópicos
- ✅ **Listar tópicos** - Visualização paginada de todos os tópicos
- ✅ **Detalhar tópico** - Consulta detalhada de um tópico específico
- ✅ **Atualizar tópico** - Apenas o autor pode editar seu tópico
- ✅ **Deletar tópico** - Apenas o autor pode excluir seu tópico
- ✅ **Validação de duplicados** - Impede criação de tópicos com mesmo título e mensagem
- ✅ **Controle de status** - Gerenciamento do ciclo de vida dos tópicos

---

## Tecnologias Utilizadas

- **Java 17** - Linguagem de programação
- **Spring Boot 3.x** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **Hibernate** - ORM (Object-Relational Mapping)
- **PostgreSQL** - Banco de dados relacional
- **JWT (JSON Web Token)** - Autenticação stateless
- **Bean Validation** - Validação de dados
- **Maven** - Gerenciamento de dependências

---

## Pré-requisitos

Antes de executar o projeto, você precisa ter instalado:

- ✅ **JDK 17** ou superior - [Download](https://www.oracle.com/java/technologies/downloads/)
- ✅ **PostgreSQL 15** ou superior - [Download](https://www.postgresql.org/download/)
- ✅ **Maven** (ou use o Maven Wrapper incluído no projeto)
- ✅ **IDE** (IntelliJ IDEA, Eclipse, VS Code, etc.)
- ✅ **Insomnia/Postman** - Para testar a API

---

## Como Executar

### 1. Clone o repositório

```bash
git clone https://github.com/Keila-Moloni-Stefani/forumhub-api.git
cd forumhub-api
```

### 2. Configure o Banco de Dados

Crie um banco de dados no PostgreSQL:

```sql
CREATE DATABASE forumhub;
```

### 3. Configure as credenciais

Copie o arquivo `application.properties.example` para `application.properties`:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Edite o arquivo `application.properties` com suas credenciais:

```properties
# PostgreSQL Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/forumhub
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA_AQUI

# JWT Configuration
api.security.token.secret=SEU_SECRET_SEGURO_AQUI
api.security.token.expiration=86400000
```

⚠️ **Importante:**
- Substitua `SUA_SENHA_AQUI` pela senha do seu PostgreSQL
- Substitua `SEU_SECRET_SEGURO_AQUI` por uma chave secreta forte (mínimo 32 caracteres)

### 4. Crie um usuário no banco

Execute este SQL no pgAdmin ou cliente PostgreSQL:

```sql
-- Criar usuário de teste (senha: 123456)
INSERT INTO usuarios (nome, email, senha)
VALUES (
    'João Silva', 
    'joao@email.com', 
    '$2a$10$2IyU6JO59ltX/FXVLXlWMOYpo8tI6Rp4NRW59d5dswfaXScW/eHV6'
);
```

### 5. Execute o projeto

**Via Maven:**
```bash
mvn spring-boot:run
```

**Via IDE:**
- Abra o projeto na IDE
- Execute a classe `ForumhubApiApplication.java`

A aplicação estará disponível em: `http://localhost:8080`

---

## Estrutura do Projeto

```
forumhub-api/
├── src/
│   ├── main/
│   │   ├── java/br/com/alura/api/
│   │   │   ├── controller/
│   │   │   │   ├── AutenticacaoController.java
│   │   │   │   └── TopicoController.java
│   │   │   ├── domain/
│   │   │   │   ├── topico/
│   │   │   │   │   ├── Topico.java
│   │   │   │   │   ├── TopicoRepository.java
│   │   │   │   │   ├── StatusTopico.java
│   │   │   │   │   ├── DadosCadastroTopico.java
│   │   │   │   │   ├── DadosAtualizacaoTopico.java
│   │   │   │   │   ├── DadosListagemTopico.java
│   │   │   │   │   └── DadosDetalhamentoTopico.java
│   │   │   │   └── usuario/
│   │   │   │       ├── Usuario.java
│   │   │   │       ├── UsuarioRepository.java
│   │   │   │       └── DadosAutenticacao.java
│   │   │   └── infra/
│   │   │       ├── exception/
│   │   │       │   ├── TratadorDeErros.java
│   │   │       │   └── ValidacaoException.java
│   │   │       └── security/
│   │   │           ├── SecurityConfigurations.java
│   │   │           ├── SecurityFilter.java
│   │   │           ├── TokenService.java
│   │   │           ├── AutenticacaoService.java
│   │   │           └── DadosTokenJWT.java
│   │   └── resources/
│   │       └── application.properties.example
│   └── test/
├── .gitignore
├── pom.xml
└── README.md
```

---

## Segurança

### Autenticação JWT

O sistema utiliza JWT (JSON Web Token) para autenticação stateless:

1. **Login** → Recebe email e senha
2. **Validação** → Verifica credenciais no banco (BCrypt)
3. **Token** → Gera token JWT válido por 24h
4. **Proteção** → Token necessário para acessar endpoints protegidos

### Autorização

- ✅ Apenas usuários autenticados podem criar tópicos
- ✅ Apenas o autor pode editar/deletar seu próprio tópico
- ✅ Senhas criptografadas com BCrypt
- ✅ CORS configurado
- ✅ CSRF desabilitado (API stateless)

---

## Validações Implementadas

| Validação | Descrição |
|-----------|-----------|
| **Duplicação** | Não permite tópicos com mesmo título E mensagem |
| **Autoria** | Apenas autor pode atualizar/deletar |
| **Campos obrigatórios** | Título, mensagem e curso não podem ser vazios |
| **Token válido** | Verifica expiração e assinatura do JWT |

---

## Aprendizados

Este projeto proporcionou experiência prática em:

- ✅ Desenvolvimento de APIs RESTful
- ✅ Implementação de autenticação JWT
- ✅ Spring Security com filtros personalizados
- ✅ Autorização baseada em usuário
- ✅ Criptografia de senhas com BCrypt
- ✅ Validação de dados com Bean Validation
- ✅ Tratamento global de exceções
- ✅ Relacionamentos JPA (ManyToOne, OneToMany)
- ✅ Queries derivadas com Spring Data JPA
- ✅ Paginação e ordenação de resultados
- ✅ CORS e configurações de segurança

---

## Licença

Este projeto foi desenvolvido para fins educacionais como parte do desafio Fórum Hub da Alura.

---

## Desenvolvedor

Desenvolvido por Keila Moloni Stefani
💼 LinkedIn: [linkedin](https://www.linkedin.com/in/keila-moloni-stefani/)


---

⭐ Se este projeto foi útil para você, considere dar uma estrela!
