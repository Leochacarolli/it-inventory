# IT Inventory

API REST desenvolvida como projeto da Pós-Graduação em Engenharia de Software com Java.

O **IT Inventory** teve início em uma disciplina anterior, na qual evoluiu gradualmente desde a modelagem orientada a objetos até uma API REST com persistência utilizando Spring Data JPA.

Na disciplina atual, o projeto está sendo utilizado como base para o estudo de arquiteturas de microsserviços.

Nesta primeira etapa, a aplicação ainda permanece como uma única aplicação Spring Boot. O objetivo é revisar sua arquitetura, organizar responsabilidades por domínio e identificar possíveis candidatos para futura separação em serviços independentes.

---

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

# Arquitetura atual

A aplicação utiliza atualmente a seguinte arquitetura:

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

As responsabilidades são divididas da seguinte forma:

- **Controller**: recebe requisições HTTP, valida os dados recebidos e devolve respostas da API.
- **DTO**: controla os dados recebidos e retornados pelos endpoints.
- **Service**: concentra regras de negócio e coordena as operações da aplicação.
- **Repository**: realiza o acesso ao banco de dados utilizando Spring Data JPA.
- **Model**: representa as entidades do domínio.
- **Exception**: representa situações excepcionais relacionadas às regras da aplicação.
- **GlobalExceptionHandler**: centraliza a conversão das exceções em respostas HTTP adequadas.

Os Controllers não realizam acesso direto aos Repositories.

As regras e operações necessárias para criação e atualização de ativos são delegadas ao `AssetService`.

---

# Organização por domínio

A aplicação deixou de utilizar uma organização exclusivamente técnica como:

```text
controller
service
repository
model
```

e passou a ser organizada prioritariamente pelas responsabilidades do domínio.

A estrutura principal atual é:

```text
src/main/java/br/com/posjava/leochacarolli/it_inventory
├── asset
│   ├── controller
│   ├── dto
│   ├── exception
│   ├── model
│   ├── repository
│   └── service
│
├── catalog
│   ├── exception
│   ├── model
│   ├── repository
│   └── service
│
├── location
│   ├── exception
│   ├── model
│   ├── repository
│   └── service
│
├── shared
│   ├── exception
│   └── model
│
├── config
│   └── Loader
│
└── ItInventoryApplication
```

Essa organização permite identificar de maneira mais clara as diferentes responsabilidades da aplicação e prepara o projeto para futuras evoluções arquiteturais.

---

# Módulos da aplicação

A aplicação possui atualmente três principais módulos de negócio.

## Asset

Responsável pelo gerenciamento dos ativos de TI cadastrados no inventário.

Este módulo concentra:

- criação de ativos;
- consulta de ativos;
- atualização;
- exclusão;
- consulta por status;
- pesquisa por nome;
- ordenação por nome.

Entre suas principais classes estão:

```text
Asset
AssetController
AssetService
AssetRepository
AssetRequestDTO
AssetResponseDTO
```

---

## Catalog

Responsável pelas informações utilizadas para classificar e identificar os diferentes tipos de ativos.

O módulo reúne:

```text
AssetModel
Category
Manufacturer
```

Por exemplo:

```text
Category
    ↓
Notebook

Manufacturer
    ↓
Lenovo

AssetModel
    ↓
ThinkPad E14
```

O catálogo representa as características do equipamento, enquanto o módulo `Asset` representa uma unidade física específica cadastrada no inventário.

---

## Location

Responsável pelo gerenciamento das localizações disponíveis para os ativos.

Uma localização representa o local em que determinado equipamento está alocado.

Exemplos:

```text
Human Resources
NOC
Comercial
```

Entre suas principais classes estão:

```text
Location
LocationService
LocationRepository
```

---

# Dependências entre módulos

Embora a aplicação esteja organizada por domínio, existem dependências entre seus módulos.

Atualmente o principal relacionamento é:

```text
Asset
 ├──→ Catalog
 └──→ Location
```

## Asset → Catalog

Um ativo precisa estar associado a um modelo existente no catálogo.

Durante a criação ou atualização de um ativo, o `AssetService` consulta o `AssetModelService` utilizando o identificador recebido pela API.

Exemplo conceitual:

```text
AssetService
    ↓
AssetModelService
    ↓
AssetModel
```

---

## Asset → Location

Um ativo também precisa estar associado a uma localização existente.

Durante a criação ou atualização, o `AssetService` consulta o `LocationService` para recuperar a localização informada.

Exemplo:

```text
AssetService
    ↓
LocationService
    ↓
Location
```

Atualmente essas dependências são resolvidas através de chamadas entre objetos Java dentro da mesma aplicação Spring Boot.

Essa análise permite identificar o acoplamento entre os módulos antes de qualquer separação em microsserviços.

---

# Candidato a serviço independente

O módulo escolhido como possível candidato para futura execução independente é o módulo **Location**.

## Responsabilidade

O módulo é responsável pelo cadastro e gerenciamento das localizações onde os ativos podem estar alocados.

## Por que poderia ser separado

A responsabilidade de localização é relativamente independente do gerenciamento do ativo propriamente dito.

O módulo possui seu próprio:

```text
Model
Service
Repository
Exceptions
```

e pode possuir ciclo de vida próprio.

Uma eventual separação permitiria, por exemplo, que informações de localização fossem utilizadas por outros sistemas além do inventário de ativos.

## Dependências atuais

Atualmente o módulo que depende diretamente de `Location` é:

```text
Asset
```

Durante a criação ou atualização de um ativo, o `AssetService` consulta o `LocationService`.

Hoje essa comunicação ocorre internamente:

```text
AssetService
    ↓
LocationService
```

Em uma futura arquitetura distribuída, essa dependência poderia passar a envolver comunicação entre serviços.

Nesta etapa, entretanto, **nenhum módulo foi separado**.

A aplicação permanece como um único projeto Spring Boot.

---

# Tecnologias utilizadas

- Java 17
- Spring Boot 4.1.0
- Spring MVC
- Spring Data JPA
- Hibernate
- H2 Database
- Bean Validation
- Springdoc OpenAPI
- Swagger UI
- Maven
- Postman
- Git
- GitHub

---

# Modelo de domínio

As principais entidades da aplicação são:

## Asset

Representa um ativo físico de TI.

Principais atributos:

- id;
- active;
- name;
- serialNumber;
- purchaseValue;
- model;
- location.

---

## AssetModel

Representa o modelo de um ativo.

Exemplos:

```text
ThinkPad E14
Latitude 5440
```

---

## Category

Representa a categoria de um modelo.

Exemplos:

```text
Notebook
Desktop
```

---

## Manufacturer

Representa o fabricante de um modelo.

Exemplos:

```text
Dell
Lenovo
```

---

## Location

Representa a localização física de um ativo.

Exemplos:

```text
Human Resources
NOC
Comercial
```

---

## BaseEntity

Classe base utilizada pelas entidades persistentes.

Contém:

```text
id
active
```

A classe está localizada no módulo compartilhado:

```text
shared
└── model
    └── BaseEntity
```

---

# Relacionamentos JPA

Os principais relacionamentos são:

```text
Category      1 ─── N AssetModel
Manufacturer  1 ─── N AssetModel
AssetModel    1 ─── N Asset
Location      1 ─── N Asset
```

O projeto utiliza recursos como:

```java
@ManyToOne
@OneToMany
@MappedSuperclass
@Id
@GeneratedValue
```

---

# Banco de dados

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

## Console H2

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

# Como executar a aplicação

## Pré-requisitos

- JDK 17 ou superior;
- Maven ou Maven Wrapper incluído no projeto.

## Windows

```bash
mvnw.cmd spring-boot:run
```

## Linux / macOS

```bash
./mvnw spring-boot:run
```

Também é possível executar diretamente pela IDE utilizando:

```text
ItInventoryApplication
```

Após a inicialização, a aplicação estará disponível em:

```text
http://localhost:8080
```

---

# Documentação da API

A aplicação utiliza **OpenAPI / Swagger** através do Springdoc.

## Swagger UI

Com a aplicação em execução:

```text
http://localhost:8080/swagger-ui/index.html
```

ou:

```text
http://localhost:8080/swagger-ui.html
```

## Especificação OpenAPI

A especificação gerada automaticamente está disponível em:

```text
http://localhost:8080/v3/api-docs
```

O Swagger apresenta:

- os recursos disponíveis;
- endpoints;
- métodos HTTP;
- parâmetros;
- estruturas de entrada;
- estruturas de saída;
- descrições das operações.

---

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
| GET | `/assets/ordered` | Lista ativos ordenados pelo nome |
| GET | `/assets/search?name=HRNT` | Pesquisa um ativo pelo nome |

---

# Exemplo de criação de ativo

## Requisição

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

## Exemplo de resposta

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

# Exemplo de atualização

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

# Status HTTP utilizados

A API utiliza respostas HTTP adequadas às principais operações.

| Situação | Status |
|---|---|
| Consulta realizada com sucesso | `200 OK` |
| Atualização realizada com sucesso | `200 OK` |
| Criação realizada com sucesso | `201 Created` |
| Exclusão realizada com sucesso | `204 No Content` |
| Dados inválidos | `400 Bad Request` |
| Recurso não encontrado | `404 Not Found` |
| Conflito ou duplicidade | `409 Conflict` |

---

# Validação dos dados

A aplicação utiliza Bean Validation para validar os dados recebidos pela API antes da execução das operações de negócio.

Entre as anotações utilizadas estão:

```java
@NotBlank
@NotNull
@Positive
@PositiveOrZero
```

Os Controllers utilizam:

```java
@Valid
```

nos DTOs recebidos pela API.

Exemplo de requisição inválida:

```json
{
  "active": true,
  "name": "",
  "serialNumber": "ABC123",
  "purchaseValue": -100,
  "assetModelId": 0,
  "locationId": -1
}
```

A aplicação retorna:

```text
400 Bad Request
```

com informações sobre os campos inválidos.

Exemplo:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Existem campos inválidos na requisição",
  "errors": {
    "locationId": "O ID da localização deve ser maior que zero",
    "name": "O nome do ativo é obrigatório",
    "purchaseValue": "O valor de compra não pode ser negativo",
    "assetModelId": "O ID do modelo deve ser maior que zero"
  }
}
```

---

# Tratamento de exceções

O tratamento das situações excepcionais da API é centralizado através de:

```java
@RestControllerAdvice
```

na classe:

```text
GlobalExceptionHandler
```

A estrutura está localizada em:

```text
shared
└── exception
    └── GlobalExceptionHandler
```

Atualmente são tratados casos como:

```text
Bean Validation
        ↓
400 Bad Request

InvalidAssetDataException
InvalidLocationDataException
        ↓
400 Bad Request

AssetNotFoundException
AssetModelNotFoundException
CategoryNotFoundException
LocationNotFoundException
        ↓
404 Not Found

DuplicateAssetException
DuplicateLocationException
        ↓
409 Conflict
```

Dessa forma, detalhes internos da aplicação não são retornados diretamente ao cliente.

Exemplo de consulta de um recurso inexistente:

```http
GET /assets/999
```

Resposta:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Ativo não encontrado para o ID: 999"
}
```

O mesmo tratamento é utilizado para tentativas de exclusão de recursos inexistentes.

---

# Consultas utilizando Spring Data JPA

O projeto utiliza consultas derivadas do Spring Data JPA.

Exemplos no `AssetRepository`:

```java
List<Asset> findByActive(boolean active);

List<Asset> findAllByOrderByNameAsc();

Optional<Asset> findFirstByNameContainingIgnoreCase(String name);
```

Essas consultas representam necessidades do domínio e são utilizadas para:

- filtrar ativos pelo status;
- listar ativos em ordem alfabética;
- pesquisar ativos utilizando parte ou o nome completo.

Além dessas consultas, o Repository também possui todas as operações básicas disponibilizadas por:

```java
JpaRepository
```

---

# DTOs e serialização

A API utiliza DTOs para controlar os dados recebidos e retornados.

## AssetRequestDTO

Utilizado para os dados recebidos nas operações de criação e atualização.

Exemplo:

```json
{
  "active": true,
  "name": "HRNT01",
  "serialNumber": "4IJ18H",
  "purchaseValue": 3000.0,
  "assetModelId": 1,
  "locationId": 1
}
```

## AssetResponseDTO

Utilizado para controlar a estrutura retornada pela API.

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

Essa estratégia evita expor diretamente todo o grafo de entidades JPA e ajuda a impedir problemas de referência circular durante a serialização dos relacionamentos.

---

# Coleção de testes do Postman

As requisições utilizadas para testar a API estão armazenadas no diretório:

```text
postman/
└── it-inventory.postman_collection.json
```

A coleção contempla testes como:

- criação de ativo;
- tentativa de criação com dados inválidos;
- listagem de ativos;
- consulta por ID;
- consulta de ID inexistente;
- atualização;
- exclusão;
- tentativa de exclusão de recurso inexistente;
- listagem de ativos ativos;
- listagem de ativos inativos;
- ordenação por nome;
- pesquisa por nome.

---

# Evolução arquitetural

O projeto teve origem em uma disciplina anterior e evoluiu gradualmente desde uma aplicação orientada a objetos até uma API REST persistente.

Na disciplina atual, o foco passa a ser a preparação para uma futura arquitetura de microsserviços.

Nesta primeira etapa, foram realizadas principalmente as seguintes mudanças:

```text
Organização técnica
controller / service / repository / model
                ↓
Organização por domínio
asset / catalog / location
```

Também foram revisados:

- separação de responsabilidades;
- fluxo Controller → Service → Repository;
- Bean Validation;
- tratamento centralizado de exceções;
- consultas Spring Data JPA;
- documentação OpenAPI / Swagger;
- dependências entre módulos;
- identificação de candidato a serviço independente.

A aplicação continua sendo um **monólito Spring Boot**.

Nenhum microsserviço foi criado nesta etapa.

---

# Arquitetura ao final da Etapa 1

```text
Cliente HTTP
       ↓
┌──────────────────────────────────┐
│       Aplicação Spring Boot      │
│                                  │
│  ┌─────────┐                     │
│  │  Asset  │                     │
│  └────┬────┘                     │
│       │                          │
│  ┌────▼─────┐    ┌──────────┐    │
│  │ Catalog  │    │ Location │    │
│  └──────────┘    └──────────┘    │
│                                  │
│       Shared / Config            │
└──────────────────────────────────┘
       ↓
Banco de Dados H2
```

---

# Requisitos da Etapa 1 contemplados

A versão atual demonstra:

- arquitetura `Controller → Service → Repository → Banco`;
- Controllers sem acesso direto aos Repositories;
- regras e operações concentradas nos Services;
- organização por domínio;
- Bean Validation;
- tratamento centralizado de exceções;
- respostas HTTP adequadas;
- consultas utilizando Spring Data JPA;
- documentação OpenAPI / Swagger;
- identificação de módulos;
- análise das dependências entre módulos;
- identificação de candidato a serviço independente;
- preparação da aplicação para futura evolução arquitetural.

---

# Marco da Etapa 1

Ao concluir a Etapa 1 da disciplina atual, a versão deverá ser registrada no Git utilizando a tag:

```text
etapa-1
```

Essa versão representa a aplicação organizada antes da separação de qualquer funcionalidade em um serviço independente.

> Caso o mesmo repositório da disciplina anterior esteja sendo reutilizado e já exista uma tag com esse nome, a estratégia de versionamento deverá ser definida antes da criação da nova tag.

---

# Autor

Projeto desenvolvido por **Leonardo Chacarolli**.