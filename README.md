# IT Inventory

API REST desenvolvida como projeto da disciplina da Pós-Graduação em Engenharia de Software com Java.

O projeto representa a evolução incremental de uma única aplicação ao longo de quatro etapas, passando por modelagem orientada a objetos, uso de Collections e Services, criação de API REST com Spring Boot e, por fim, persistência com Spring Data JPA e banco de dados H2.

## Objetivo

O **IT Inventory** é uma aplicação para gerenciamento de ativos de TI.

O domínio contempla:

- ativos;
- modelos de ativos;
- categorias;
- fabricantes;
- localizações.

A aplicação permite cadastrar, consultar, atualizar, remover, filtrar, ordenar e pesquisar ativos.

---

## Arquitetura atual

Na Etapa 4, a aplicação utiliza a arquitetura:

```text
Cliente HTTP
    ↓
Controller
    ↓
Service
    ↓
Repository
    ↓
Banco de Dados H2
```

A separação de responsabilidades foi mantida da seguinte forma:

- **Controller**: recebe requisições HTTP e devolve respostas da API.
- **DTO**: controla os dados de entrada e saída.
- **Service**: concentra regras e operações da aplicação.
- **Repository**: realiza o acesso ao banco com Spring Data JPA.
- **Model**: representa as entidades do domínio.
- **Exception**: contém exceções específicas da aplicação.

---

## Tecnologias utilizadas

- Java 17
- Spring Boot 4.1.0
- Spring MVC
- Spring Data JPA
- Hibernate
- H2 Database
- Bean Validation
- Maven
- Postman
- Git / GitHub

---

## Estrutura principal do projeto

```text
src/main/java/br/com/posjava/leochacarolli/it_inventory
├── controller
├── dto
├── exception
├── model
├── repository
└── service
```

---

## Modelo de domínio

As principais entidades da aplicação são:

### Asset

Representa um ativo de TI.

Principais atributos:

- id;
- active;
- name;
- serialNumber;
- purchaseValue;
- model;
- location.

### AssetModel

Representa o modelo de um ativo.

Exemplos:

- ThinkPad E14;
- Latitude 5440.

### Category

Representa a categoria de um modelo.

Exemplos:

- Notebook;
- Desktop.

### Manufacturer

Representa o fabricante.

Exemplos:

- Dell;
- Lenovo.

### Location

Representa a localização física de um ativo.

Exemplos:

- Human Resources;
- NOC;
- Comercial.

### BaseEntity

Classe base utilizada pelas entidades persistentes.

Contém:

- `id`;
- `active`.

---

## Relacionamentos JPA

Os relacionamentos principais são:

```text
Category      1 ─── N AssetModel
Manufacturer  1 ─── N AssetModel
AssetModel    1 ─── N Asset
Location      1 ─── N Asset
```

O projeto utiliza:

```java
@ManyToOne
@OneToMany
@MappedSuperclass
@Id
@GeneratedValue
```

---

## Banco de dados

A aplicação utiliza banco de dados **H2 em memória**.

Configuração principal:

```yaml
spring:
  application:
    name: it-inventory

  datasource:
    url: jdbc:h2:mem:itinventory
    driver-class-name: org.h2.Driver
    username: sa
    password:

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

  h2:
    console:
      enabled: true
```

### Console H2

Com a aplicação em execução, o console pode ser acessado em:

```text
http://localhost:8080/h2-console
```

Utilize:

```text
JDBC URL: jdbc:h2:mem:itinventory
User Name: sa
Password:
```

Como o banco é executado em memória, os dados são recriados quando a aplicação é reiniciada.

---

## Como executar a aplicação

### Pré-requisitos

- JDK 17 ou superior;
- Maven, ou Maven Wrapper incluído no projeto.

### Windows

```bash
mvnw.cmd spring-boot:run
```

### Linux / macOS

```bash
./mvnw spring-boot:run
```

Também é possível executar diretamente a classe:

```text
ItInventoryApplication
```

pela IDE.

Após a inicialização, a aplicação estará disponível em:

```text
http://localhost:8080
```

---

# Documentação da API

Base URL:

```text
http://localhost:8080
```

## Assets

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/assets` | Lista todos os ativos |
| GET | `/assets/{id}` | Busca um ativo por ID |
| POST | `/assets` | Cadastra um novo ativo |
| PUT | `/assets/{id}` | Atualiza um ativo existente |
| DELETE | `/assets/{id}` | Remove um ativo |
| GET | `/assets/active` | Lista ativos com status ativo |
| GET | `/assets/inactive` | Lista ativos com status inativo |
| GET | `/assets/ordered` | Lista ativos ordenados por nome |
| GET | `/assets/search?name=HRNT` | Busca ativo pelo nome |

---

## Exemplo de criação de ativo

### Requisição

```http
POST /assets
Content-Type: application/json
```

```json
{
  "active": true,
  "name": "TESTENT01",
  "serialNumber": "ABC123",
  "purchaseValue": 2500,
  "assetModelId": 1,
  "locationId": 1
}
```

### Exemplo de resposta

```json
{
  "id": 4,
  "active": true,
  "name": "TESTENT01",
  "serialNumber": "ABC123",
  "purchaseValue": 2500.0,
  "model": "Latitude 5440",
  "location": "Human Resources"
}
```

---

## Exemplo de atualização

```http
PUT /assets/4
Content-Type: application/json
```

```json
{
  "active": false,
  "name": "TESTENT01 - Atualizado",
  "serialNumber": "ABC123",
  "purchaseValue": 2800,
  "assetModelId": 1,
  "locationId": 2
}
```

---

## Status HTTP utilizados

A API utiliza respostas HTTP adequadas às principais operações:

| Operação | Status |
|---|---|
| Criação com sucesso | `201 Created` |
| Consulta com sucesso | `200 OK` |
| Atualização com sucesso | `200 OK` |
| Exclusão com sucesso | `204 No Content` |
| Dados inválidos | `400 Bad Request` |
| Recurso não encontrado | `404 Not Found` |

---

# Validação dos dados

A aplicação utiliza Bean Validation para validar os dados recebidos pela API.

Entre as validações utilizadas estão:

```java
@NotBlank
@NotNull
@PositiveOrZero
```

O Controller utiliza:

```java
@Valid
```

para executar as validações dos DTOs.

Exemplos de dados inválidos:

```json
{
  "active": true,
  "name": "",
  "serialNumber": "ABC123",
  "purchaseValue": -100,
  "assetModelId": 1,
  "locationId": 1
}
```

Nesse caso, a aplicação retorna:

```text
400 Bad Request
```

---

# Consultas personalizadas

O projeto utiliza consultas derivadas do Spring Data JPA.

Exemplos no `AssetRepository`:

```java
List<Asset> findByActive(boolean active);

List<Asset> findAllByOrderByNameAsc();

Optional<Asset> findFirstByNameContainingIgnoreCase(String name);
```

Essas consultas são utilizadas para:

- filtrar ativos por status;
- ordenar ativos pelo nome;
- pesquisar ativos por parte do nome.

---

# DTOs e serialização

A API utiliza DTOs para controlar os dados recebidos e enviados.

O `AssetResponseDTO` evita expor diretamente todo o grafo de entidades JPA e retorna informações simplificadas dos relacionamentos.

Exemplo:

```json
{
  "id": 1,
  "active": true,
  "name": "HRNT01",
  "serialNumber": "4IJ18H",
  "purchaseValue": 3000.0,
  "model": "ThinkPad E14",
  "location": "Human Resources"
}
```

Essa estratégia também evita problemas de referência circular durante a serialização dos relacionamentos.

---

# Coleção de testes do Postman

As requisições utilizadas para testar a API devem ser exportadas do Postman e adicionadas ao repositório.

Estrutura recomendada:

```text
postman/
└── it-inventory.postman_collection.json
```

A coleção pode conter requisições para:

- criação de ativo;
- tentativa de criação com dados inválidos;
- listagem de ativos;
- consulta por ID;
- atualização;
- exclusão;
- listagem de ativos ativos;
- listagem de ativos inativos;
- ordenação por nome;
- pesquisa por nome.

---

# Evolução do projeto

O projeto foi desenvolvido de forma incremental.

As quatro etapas representam momentos diferentes da evolução da **mesma aplicação**, conforme solicitado na disciplina.

| Tag | Competência demonstrada |
|---|---|
| `etapa-1` | Modelo Orientado a Objetos |
| `etapa-2` | Collections + Service + armazenamento em memória |
| `etapa-3` | Spring Boot + API REST |
| `etapa-4` | Spring Data JPA + Banco de Dados |

## Etapa 1

Foco em:

- modelagem orientada a objetos;
- classes e objetos;
- herança;
- encapsulamento;
- relacionamentos entre objetos;
- Loader para criação e teste dos dados.

## Etapa 2

Foco em:

- Collections;
- `List`;
- `Map`;
- Streams e Lambdas;
- criação da camada Service;
- armazenamento dos dados em memória;
- regras da aplicação.

## Etapa 3

Foco em:

- Spring Boot;
- Spring MVC;
- API REST;
- Controllers;
- DTOs;
- métodos HTTP;
- integração entre Controller e Service;
- tratamento de respostas HTTP.

## Etapa 4

Foco em:

- Spring Data JPA;
- Hibernate;
- banco H2;
- entidades persistentes;
- repositories;
- relacionamentos JPA;
- identificadores gerados automaticamente;
- substituição do armazenamento em `Map`;
- consultas personalizadas;
- filtro e ordenação;
- Bean Validation.

---

# Requisitos acadêmicos contemplados

A versão atual demonstra:

- modelagem orientada a objetos;
- encapsulamento e herança;
- Collections;
- Services;
- Streams e Lambdas;
- API REST;
- arquitetura em camadas;
- Inversão de Controle;
- Injeção de Dependência;
- DTOs;
- Spring Data JPA;
- Hibernate;
- persistência em banco de dados;
- relacionamentos entre entidades;
- CRUD;
- consultas personalizadas;
- filtro;
- ordenação;
- Bean Validation;
- tratamento de status HTTP.

---

# Tags da entrega

O repositório deve conter as seguintes tags:

```text
etapa-1
etapa-2
etapa-3
etapa-4
```

Cada tag representa o estado do projeto ao final da respectiva etapa da disciplina.

---

# Autor

Projeto desenvolvido por **Leonardo Chacarolli**
