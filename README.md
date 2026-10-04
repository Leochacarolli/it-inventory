# IT Inventory

API REST desenvolvida como projeto da Pós-Graduação em Engenharia de Software com Java.

O **IT Inventory** teve início em uma disciplina anterior, na qual evoluiu gradualmente desde a modelagem orientada a objetos até uma API REST com persistência utilizando Spring Data JPA.

Na disciplina atual, o projeto está sendo utilizado como base para o estudo de **arquiteturas de microsserviços**.

Na **Etapa 1**, a aplicação foi reorganizada por domínio e teve suas responsabilidades revisadas.

Na **Etapa 2**, a responsabilidade de **localizações** foi extraída da aplicação principal e transformada em uma aplicação Spring Boot independente, denominada **Location Service**. A comunicação entre as duas aplicações passou a ocorrer via HTTP utilizando **OpenFeign**.

---

## Objetivo

O **IT Inventory** é uma solução para gerenciamento de ativos de TI.

O domínio contempla:

- ativos;
- modelos de ativos;
- categorias;
- fabricantes;
- localizações.

A aplicação principal permite cadastrar, consultar, atualizar, remover, filtrar, ordenar e pesquisar ativos.

As localizações passaram a ser gerenciadas por um serviço independente.

---

# Arquitetura atual

Ao final da Etapa 2, a solução possui duas aplicações Spring Boot independentes:

```text
Cliente HTTP
    ↓
IT Inventory :8080
├── Controller
├── Service
├── Repository
├── Banco H2
└── LocationClient
        ↓
     OpenFeign
        ↓ HTTP
Location Service :8081
├── Controller
├── Service
├── Repository
└── Banco H2
```

A aplicação principal continua concentrando a maior parte das funcionalidades do sistema.

Apenas a responsabilidade de localização foi separada.

## Responsabilidades da aplicação principal

- **Controller**: recebe requisições HTTP e devolve respostas da API.
- **DTO**: representa os dados recebidos e retornados pelos endpoints.
- **Service**: concentra regras e coordena as operações da aplicação.
- **Repository**: realiza acesso aos dados utilizando Spring Data JPA.
- **Model**: representa as entidades pertencentes à aplicação principal.
- **Feign Client**: realiza a comunicação HTTP com o Location Service.
- **GlobalExceptionHandler**: centraliza a conversão de exceções em respostas HTTP adequadas.

Os Controllers não acessam diretamente os Repositories e também não realizam comunicação direta com o serviço externo.

O fluxo utilizado para consultas que dependem de localização é:

```text
Controller
    ↓
AssetService
    ↓
LocationClient
    ↓ HTTP
Location Service
```

---

# Organização do projeto

A aplicação principal continua organizada por domínio.

```text
it-inventory/
├── location-service/
│   ├── src/main/java/br/com/posjava/leochacarolli/location_service
│   │   ├── config
│   │   ├── controller
│   │   ├── dto
│   │   ├── exception
│   │   ├── model
│   │   ├── repository
│   │   └── service
│   ├── src/main/resources
│   └── pom.xml
│
├── postman/
│   ├── it-inventory.postman_collection.json
│   ├── it-inventory-service-unavailable.postman_collection.json
│   └── README-testes-postman.md
│
├── src/main/java/br/com/posjava/leochacarolli/it_inventory
│   ├── asset
│   │   ├── controller
│   │   ├── dto
│   │   ├── exception
│   │   ├── model
│   │   ├── repository
│   │   └── service
│   │
│   ├── catalog
│   │   ├── exception
│   │   ├── model
│   │   ├── repository
│   │   └── service
│   │
│   ├── location
│   │   └── client
│   │       └── exception
│   │
│   ├── shared
│   │   ├── exception
│   │   └── model
│   │
│   ├── config
│   └── ItInventoryApplication
│
├── src/main/resources
├── pom.xml
└── README.md
```

A aplicação principal não possui mais `LocationRepository`, `LocationService` ou entidade JPA `Location`.

O pacote `location` da aplicação principal contém apenas os componentes necessários para a comunicação com o serviço remoto.

---

# Módulos e responsabilidades

## Asset

Responsável pelo gerenciamento dos ativos físicos de TI.

Entre suas funcionalidades estão:

- criação;
- consulta;
- atualização;
- exclusão;
- filtro por status;
- ordenação por nome;
- pesquisa por nome.

Principais componentes:

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

Responsável pelas informações utilizadas para classificar os ativos.

O módulo reúne:

```text
AssetModel
Category
Manufacturer
```

Exemplo:

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

O catálogo continua pertencendo à aplicação principal.

---

## Location Service

Serviço independente responsável pelo gerenciamento das localizações onde os ativos podem estar alocados.

Exemplos:

```text
Human Resources
NOC
Comercial
```

O serviço possui aplicação, API REST, banco de dados e ciclo de execução próprios.

Principais componentes:

```text
Location
LocationController
LocationService
LocationRepository
LocationRequestDTO
LocationResponseDTO
```

---

# Serviço independente escolhido

## Nome

**Location Service**

## Responsabilidade principal

Cadastrar e consultar as localizações utilizadas pelos ativos do inventário.

## Funcionalidade separada

Na Etapa 1, `Location` fazia parte da mesma aplicação Spring Boot que `Asset`.

A comunicação era interna:

```text
AssetService
    ↓
LocationService
    ↓
LocationRepository
```

Na Etapa 2, essa responsabilidade foi removida da aplicação principal.

A comunicação passou a ser:

```text
AssetService
    ↓
LocationClient
    ↓ OpenFeign / HTTP
Location Service
    ↓
LocationService
    ↓
LocationRepository
```

## Motivo da separação

A responsabilidade de localização possui limites claros e pode existir independentemente do gerenciamento de ativos.

Ela possui seus próprios:

- dados;
- regras;
- API;
- DTOs;
- Repository;
- tratamento de exceções.

Além disso, as informações de localização poderiam futuramente ser reutilizadas por outras aplicações.

---

# Dependência entre os serviços

Um `Asset` não armazena mais uma entidade `Location`.

Na aplicação principal, o ativo possui apenas:

```text
locationId
```

Exemplo:

```text
Asset
├── id = 1
├── name = HRNT01
└── locationId = 1
```

O nome da localização é obtido através do Location Service:

```text
Asset.locationId = 1
        ↓
LocationClient
        ↓
GET /locations/1
        ↓
Location Service
        ↓
Human Resources
```

O banco de dados da aplicação principal não possui uma foreign key para a tabela de localizações, pois essa tabela pertence a outro serviço.

---

# Tecnologias utilizadas

- Java 17
- Spring Boot 4.1.x
- Spring MVC
- Spring Data JPA
- Hibernate
- H2 Database
- Bean Validation
- Spring Cloud OpenFeign
- Springdoc OpenAPI
- Swagger UI
- Maven
- Postman
- Git
- GitHub

---

# Bancos de dados

As aplicações utilizam bancos H2 em memória independentes.

## IT Inventory

```text
jdbc:h2:mem:itinventory
```

Tabelas principais:

```text
asset
asset_model
category
manufacturer
```

A tabela `location` não existe mais no banco principal.

## Location Service

```text
jdbc:h2:mem:locationdb
```

Tabela principal:

```text
location
```

Como os bancos são executados em memória, os dados são recriados quando as aplicações são reiniciadas.

---

# Configuração do Location Service

A aplicação principal não possui o endereço do Location Service inserido diretamente no código Java.

A URL é configurada externamente no `application.yml`:

```yaml
services:
  location:
    url: http://localhost:8081
```

O Feign Client utiliza essa configuração para localizar o serviço remoto.

Essa separação permite alterar o endereço do serviço sem modificar a implementação Java.

---

# Como executar a solução

## Pré-requisitos

- JDK 17 ou superior;
- Maven ou Maven Wrapper;
- portas `8080` e `8081` disponíveis.

## Ordem recomendada

Iniciar primeiro:

```text
LocationServiceApplication
```

O serviço ficará disponível em:

```text
http://localhost:8081
```

Depois iniciar:

```text
ItInventoryApplication
```

A aplicação principal ficará disponível em:

```text
http://localhost:8080
```

## Execução pela IDE

É possível executar diretamente:

```text
location-service
└── LocationServiceApplication
```

e:

```text
it-inventory
└── ItInventoryApplication
```

As duas aplicações devem permanecer em execução simultaneamente durante os testes normais de integração.

---

# API do IT Inventory

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
| GET | `/assets/active` | Lista ativos ativos |
| GET | `/assets/inactive` | Lista ativos inativos |
| GET | `/assets/ordered` | Lista ativos ordenados pelo nome |
| GET | `/assets/search?name=HRNT` | Pesquisa ativo pelo nome |

---

# API do Location Service

Base URL:

```text
http://localhost:8081
```

## Locations

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/locations` | Lista as localizações |
| GET | `/locations/{id}` | Busca uma localização por ID |
| POST | `/locations` | Cadastra uma nova localização |

---

# OpenAPI / Swagger

As duas aplicações possuem documentação própria.

## IT Inventory

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI:

```text
http://localhost:8080/v3/api-docs
```

## Location Service

Swagger UI:

```text
http://localhost:8081/swagger-ui/index.html
```

OpenAPI:

```text
http://localhost:8081/v3/api-docs
```

A documentação permite identificar endpoints, métodos HTTP, parâmetros e estruturas utilizadas pelas APIs.

---

# DTOs e contrato entre aplicações

As entidades JPA não são utilizadas diretamente como contrato entre os serviços.

O Location Service utiliza DTOs próprios:

```text
LocationRequestDTO
LocationResponseDTO
```

A aplicação principal possui um DTO específico para receber a resposta do serviço remoto:

```text
LocationClientResponseDTO
```

Esse modelo permite separar:

```text
modelo interno da aplicação
        ≠
contrato utilizado na comunicação HTTP
```

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

Antes de salvar o ativo, a aplicação principal consulta o Location Service para verificar se a localização existe.

## Resposta

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

Embora a entidade `Asset` armazene apenas `locationId`, a API continua retornando o nome da localização através da consulta remota.

---

# Exemplo de integração com uma localização criada no serviço remoto

Uma localização pode ser criada diretamente no Location Service:

```http
POST http://localhost:8081/locations
```

```json
{
  "active": true,
  "name": "Laboratório de Testes",
  "floor": 2,
  "description": "Localização criada para teste da Etapa 2"
}
```

Exemplo de resposta:

```json
{
  "id": 4,
  "active": true,
  "name": "Laboratório de Testes",
  "floor": 2,
  "description": "Localização criada para teste da Etapa 2"
}
```

Em seguida, a aplicação principal pode utilizar esse ID:

```json
{
  "active": true,
  "name": "TESTENT01",
  "serialNumber": "ABC123",
  "purchaseValue": 2500,
  "assetModelId": 1,
  "locationId": 4
}
```

A resposta da aplicação principal contém:

```json
{
  "location": "Laboratório de Testes"
}
```

demonstrando que o nome foi obtido através do Location Service.

---

# Bean Validation

A aplicação utiliza Bean Validation para validar os dados antes da execução das operações.

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

Exemplo de dados inválidos:

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

Resposta:

```text
400 Bad Request
```

com os erros referentes aos campos inválidos.

---

# Tratamento de exceções

As situações excepcionais são convertidas em respostas HTTP controladas.

## Recurso inexistente

Exemplo:

```text
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

## Localização remota inexistente

Quando o Location Service está disponível, mas o ID solicitado não existe:

```text
POST /assets
locationId = 999
```

a chamada remota retorna `404`, que é tratada pela aplicação principal.

Exemplo:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Localização não encontrada para o ID: 999"
}
```

## Serviço remoto indisponível

Quando o Location Service não está em execução, o OpenFeign não consegue concluir a chamada.

A aplicação principal trata essa situação e retorna:

```text
503 Service Unavailable
```

Exemplo:

```json
{
  "status": 503,
  "error": "Service Unavailable",
  "message": "O serviço de localizações está temporariamente indisponível"
}
```

Dessa forma, detalhes internos da falha de comunicação não são expostos ao cliente.

---

# Status HTTP utilizados

| Situação | Status |
|---|---|
| Consulta realizada com sucesso | `200 OK` |
| Atualização realizada com sucesso | `200 OK` |
| Criação realizada com sucesso | `201 Created` |
| Exclusão realizada com sucesso | `204 No Content` |
| Dados inválidos | `400 Bad Request` |
| Recurso inexistente | `404 Not Found` |
| Conflito ou duplicidade | `409 Conflict` |
| Serviço externo indisponível | `503 Service Unavailable` |

---

# Consultas Spring Data JPA

A aplicação principal continua utilizando consultas derivadas do Spring Data JPA.

Exemplos no `AssetRepository`:

```java
List<Asset> findByActive(boolean active);

List<Asset> findAllByOrderByNameAsc();

Optional<Asset> findFirstByNameContainingIgnoreCase(String name);
```

Essas consultas são utilizadas para:

- filtrar ativos por status;
- listar ativos em ordem alfabética;
- pesquisar ativos pelo nome.

---

# Testes com Postman

As coleções utilizadas nos testes estão armazenadas em:

```text
postman/
├── it-inventory.postman_collection.json
├── it-inventory-service-unavailable.postman_collection.json
└── README-testes-postman.md
```

## Collection principal

Executada com as duas aplicações ligadas.

Abrange:

- listagem das localizações;
- consulta de localização por ID;
- localização inexistente;
- criação de localização;
- listagem dos ativos;
- busca de ativo por ID;
- filtros;
- ordenação;
- pesquisa;
- criação de ativo;
- atualização;
- exclusão;
- Bean Validation;
- localização remota inexistente.

## Collection de indisponibilidade

Executada com:

```text
IT Inventory       → ligado
Location Service   → desligado
```

Resultado esperado:

```text
503 Service Unavailable
```

O Runner do Postman valida:

```text
Status 503
Mensagem amigável de indisponibilidade
```

---

# Testes realizados na Etapa 2

Foram validados os seguintes cenários:

| Cenário | Resultado esperado |
|---|---|
| Listar localizações | `200 OK` |
| Consultar localização existente | `200 OK` |
| Consultar localização inexistente | `404 Not Found` |
| Criar localização | `201 Created` |
| Listar ativos utilizando dados remotos | `200 OK` |
| Criar ativo com localização existente | `201 Created` |
| Atualizar localização de um ativo | `200 OK` |
| Excluir ativo | `204 No Content` |
| Consultar ativo excluído | `404 Not Found` |
| Dados inválidos | `400 Bad Request` |
| Localização remota inexistente | `404 Not Found` |
| Location Service indisponível | `503 Service Unavailable` |

---

# Evolução arquitetural

## Antes da separação

Na Etapa 1:

```text
Aplicação Spring Boot
├── Asset
├── Catalog
└── Location
        ↓
Banco único
```

A comunicação entre `Asset` e `Location` ocorria por chamadas internas entre objetos Java.

## Depois da separação

Na Etapa 2:

```text
IT Inventory
├── Asset
├── Catalog
└── LocationClient
        ↓ HTTP
Location Service
└── Location
```

A chamada:

```text
AssetService → LocationService
```

foi substituída por:

```text
AssetService → LocationClient → HTTP → Location Service
```

Essa mudança introduziu novos aspectos arquiteturais:

- comunicação pela rede;
- contratos entre aplicações;
- configuração de endereço externo;
- possibilidade de indisponibilidade;
- necessidade de tratar falhas remotas;
- execução independente das aplicações.

---

# Arquitetura ao final da Etapa 2

```text
Cliente HTTP
       ↓
┌─────────────────────────────────────┐
│          IT Inventory :8080         │
│                                     │
│  Controller                         │
│      ↓                              │
│  AssetService                       │
│      ├────────────→ Repository      │
│      │                 ↓            │
│      │              Banco H2        │
│      │                              │
│      └────────────→ LocationClient  │
└────────────────────────┬────────────┘
                         │
                         │ OpenFeign / HTTP
                         ↓
┌─────────────────────────────────────┐
│        Location Service :8081       │
│                                     │
│  LocationController                 │
│      ↓                              │
│  LocationService                    │
│      ↓                              │
│  LocationRepository                 │
│      ↓                              │
│  Banco H2 locationdb                │
└─────────────────────────────────────┘
```

---

# Reflexão arquitetural

## 1. Qual funcionalidade foi separada da aplicação principal?

Foi separado o gerenciamento de **localizações**.

A entidade e as operações relacionadas a `Location` deixaram de pertencer à aplicação principal e passaram a ser executadas pelo **Location Service**.

---

## 2. Por que ela foi escolhida?

A localização possui uma responsabilidade relativamente independente do gerenciamento dos ativos.

Ela possui dados e operações próprios e pode, no futuro, ser utilizada por outras aplicações além do IT Inventory.

Além disso, o módulo já apresentava limites claros na organização por domínio realizada na Etapa 1.

---

## 3. O que ficou mais complexo depois da separação?

Antes da separação, uma consulta de localização era apenas uma chamada entre métodos Java da mesma aplicação.

Depois da separação, foi necessário lidar com:

- comunicação HTTP;
- OpenFeign;
- DTOs específicos de comunicação;
- configuração do endereço remoto;
- inicialização de duas aplicações;
- bancos de dados independentes;
- erros retornados pelo serviço remoto;
- indisponibilidade de rede ou do serviço.

Assim, a separação reduziu o acoplamento de implementação, porém aumentou a complexidade operacional e de integração.

---

## 4. O que acontece com a funcionalidade principal caso o novo serviço fique indisponível?

As operações que precisam consultar informações de localização deixam de conseguir concluir normalmente.

Por exemplo, a listagem dos ativos utiliza o Location Service para transformar `locationId` no nome da localização.

Quando o serviço está indisponível, a aplicação principal retorna:

```text
503 Service Unavailable
```

com uma mensagem controlada.

Operações que não necessitam consultar o Location Service podem continuar funcionando normalmente.

---

## 5. A funcionalidade realmente precisa permanecer como serviço independente?

Não necessariamente.

Para o tamanho atual da aplicação, manter `Location` dentro do monólito também seria uma solução válida e mais simples operacionalmente.

A separação passa a fazer mais sentido caso:

- localização seja reutilizada por outros sistemas;
- possua evolução independente;
- necessite de escalabilidade própria;
- seja mantida por outra equipe;
- tenha regras de negócio próprias mais complexas.

Portanto, a criação de um microsserviço deve representar uma decisão arquitetural baseada nas necessidades do sistema, e não apenas uma escolha tecnológica.

---

# Requisitos da Etapa 2 contemplados

A versão atual demonstra:

- aplicação principal e serviço independente;
- responsabilidade clara para o Location Service;
- projetos Spring Boot executados separadamente;
- API REST própria do serviço;
- DTOs como contrato de comunicação;
- documentação OpenAPI / Swagger;
- OpenFeign na aplicação principal;
- comunicação HTTP entre aplicações;
- endereço do serviço configurado externamente;
- tratamento de `404` remoto;
- tratamento de indisponibilidade com `503`;
- bancos de dados independentes;
- testes isolados do serviço;
- testes de integração;
- testes com serviço indisponível;
- reflexão arquitetural sobre a adoção de microsserviços.

---

# Marco da Etapa 2

Ao concluir a Etapa 2 da disciplina atual, a versão deve ser registrada no Git utilizando a tag:

```text
etapa-2
```

Esse marco representa o primeiro momento em que a solução possui aplicações independentes comunicando-se através da rede.

---

# Autor

Projeto desenvolvido por **Leonardo Chacarolli**.
