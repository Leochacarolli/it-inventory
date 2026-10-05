# IT Inventory

API REST desenvolvida como projeto da Pós-Graduação em Engenharia de Software com Java.

O **IT Inventory** é uma aplicação para gerenciamento de ativos de TI que vem sendo evoluída de forma incremental ao longo das disciplinas, passando por organização arquitetural, comunicação entre microsserviços e, atualmente, práticas de configuração externa e execução Cloud Native.

Na arquitetura atual, a solução possui duas aplicações de domínio independentes:

- **IT Inventory** — aplicação principal responsável pelo gerenciamento dos ativos;
- **Location Service** — serviço independente responsável pelo gerenciamento das localizações.

Além delas, a solução utiliza um **Spring Cloud Config Server** para centralização das configurações e bancos PostgreSQL independentes para cada serviço.

---

# Objetivo

O **IT Inventory** é uma solução para gerenciamento de ativos de TI.

O domínio contempla:

- ativos;
- modelos de ativos;
- categorias;
- fabricantes;
- localizações.

A aplicação principal permite:

- cadastrar ativos;
- consultar ativos;
- atualizar ativos;
- remover ativos;
- filtrar por status;
- ordenar por nome;
- pesquisar por nome.

As informações de localização são mantidas por um serviço independente e acessadas pela aplicação principal através de comunicação HTTP utilizando OpenFeign.

---

# Arquitetura atual

Ao final da Etapa 3, a solução possui a seguinte arquitetura:

```text
                         Config Server :8888
                          /           \
                         ↓             ↓
              IT Inventory :8080   Location Service :8081
                     ↓                     ↓
             PostgreSQL Inventory   PostgreSQL Location
```

Quando executada através do Docker Compose:

```text
Docker Compose
│
├── config-server
│      └── porta 8888
│
├── it-inventory
│      ├── porta 8080
│      └── PostgreSQL → inventory-db
│
├── location-service
│      ├── porta 8081
│      └── PostgreSQL → location-db
│
├── inventory-db
│
└── location-db
```

A comunicação entre containers ocorre através da rede criada pelo Docker Compose.

Por esse motivo, os serviços não utilizam `localhost` para comunicação interna.

Exemplos:

```text
http://config-server:8888
http://location-service:8081

jdbc:postgresql://inventory-db:5432/itinventory
jdbc:postgresql://location-db:5432/locationdb
```

---

# Organização do projeto

A estrutura principal é:

```text
it-inventory/
│
├── config-server/
│   ├── config-repo/
│   │   ├── it-inventory-dev.yml
│   │   ├── it-inventory-prod.yml
│   │   ├── location-service-dev.yml
│   │   └── location-service-prod.yml
│   │
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── location-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── postman/
│   ├── it-inventory.postman_collection.json
│   ├── it-inventory-service-unavailable.postman_collection.json
│   └── README-testes-postman.md
│
├── src/
│   └── main/
│       ├── java/
│       └── resources/
│
├── Dockerfile
├── compose.yml
├── .env.example
├── pom.xml
└── README.md
```

---

# Aplicação principal — IT Inventory

A aplicação principal continua responsável pelas funcionalidades relacionadas ao inventário.

Sua estrutura é organizada por domínio.

```text
it_inventory
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
│   └── client
│
├── shared
│   ├── exception
│   └── model
│
└── config
```

A aplicação principal não possui mais uma entidade JPA `Location`.

O ativo mantém apenas:

```text
locationId
```

e utiliza o Location Service para recuperar as informações relacionadas à localização.

---

# Location Service

O **Location Service** é uma aplicação Spring Boot independente.

Sua responsabilidade é gerenciar as localizações utilizadas pelo inventário.

Exemplos:

```text
Human Resources
NOC
Comercial
```

Principais componentes:

```text
Location
LocationController
LocationService
LocationRepository
LocationRequestDTO
LocationResponseDTO
```

O serviço possui:

- API REST própria;
- banco PostgreSQL próprio;
- configurações próprias;
- documentação Swagger;
- ciclo de execução independente.

---

# Comunicação entre aplicações

A comunicação entre o IT Inventory e o Location Service utiliza **Spring Cloud OpenFeign**.

O fluxo é:

```text
Cliente
   ↓
AssetController
   ↓
AssetService
   ↓
LocationClient
   ↓
HTTP
   ↓
Location Service
```

Exemplo:

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

Os Controllers não realizam diretamente a comunicação HTTP.

A responsabilidade permanece na camada de Service e no Feign Client.

---

# Tecnologias utilizadas

- Java 17
- Spring Boot 4.1.x
- Spring MVC
- Spring Data JPA
- Hibernate
- PostgreSQL
- Bean Validation
- Spring Cloud OpenFeign
- Spring Cloud Config
- Springdoc OpenAPI
- Swagger UI
- Maven
- Docker
- Docker Compose
- Postman
- Git
- GitHub

---

# Persistência independente

A Etapa 3 substituiu os bancos H2 em memória por bancos PostgreSQL.

Cada aplicação possui sua própria persistência.

## Banco do IT Inventory

Banco:

```text
itinventory
```

Principais tabelas:

```text
asset
asset_model
category
manufacturer
```

O banco principal não possui tabela `location`.

## Banco do Location Service

Banco:

```text
locationdb
```

Tabela principal:

```text
location
```

Isso garante que um serviço não acesse diretamente as tabelas pertencentes ao outro.

A comunicação entre os contextos ocorre exclusivamente pelas interfaces disponibilizadas pelos serviços.

---

# PostgreSQL com Docker

Os dois bancos são executados em containers independentes.

## Inventory Database

Execução local:

```text
localhost:5432
```

Dentro da rede Docker:

```text
inventory-db:5432
```

## Location Database

Execução local:

```text
localhost:5433
```

Dentro da rede Docker:

```text
location-db:5432
```

Os bancos utilizam volumes Docker para preservar os dados entre reinicializações dos containers.

```text
inventory-db-data
location-db-data
```

---

# Profiles

As aplicações possuem configurações distintas para diferentes ambientes.

Os profiles utilizados são:

```text
dev
prod
```

## Desenvolvimento

No profile `dev`, são utilizados valores adequados para execução através da IDE.

Exemplos:

```text
IT Inventory DB:
jdbc:postgresql://localhost:5432/itinventory

Location Service DB:
jdbc:postgresql://localhost:5433/locationdb

Location Service:
http://localhost:8081
```

## Produção / Containers

No profile `prod`, os serviços utilizam os nomes definidos no Docker Compose.

Exemplos:

```text
jdbc:postgresql://inventory-db:5432/itinventory

jdbc:postgresql://location-db:5432/locationdb

http://location-service:8081
```

Dessa forma, a aplicação não precisa ter seu código alterado quando o ambiente muda.

---

# Variáveis de ambiente

As configurações que podem variar entre ambientes são fornecidas externamente.

Entre as variáveis utilizadas estão:

```text
SPRING_PROFILES_ACTIVE

SERVER_PORT

DB_URL
DB_USERNAME
DB_PASSWORD

LOCATION_SERVICE_URL

CONFIG_SERVER_URL
CONFIG_REPO_LOCATION
```

As aplicações utilizam essas variáveis durante a execução.

Exemplo:

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

---

# Arquivo `.env`

O Docker Compose utiliza um arquivo `.env` local para algumas configurações.

Exemplo:

```env
INVENTORY_DB_NAME=itinventory
INVENTORY_DB_USER=itinventory
INVENTORY_DB_PASSWORD=inventory123

LOCATION_DB_NAME=locationdb
LOCATION_DB_USER=location
LOCATION_DB_PASSWORD=location123
```

O arquivo `.env` não deve ser versionado.

Por isso ele está incluído no:

```text
.gitignore
```

O repositório disponibiliza:

```text
.env.example
```

como referência das variáveis necessárias.

---

# Configuração centralizada

A solução utiliza **Spring Cloud Config Server**.

O Config Server é executado na porta:

```text
8888
```

As configurações são armazenadas em:

```text
config-server/config-repo/
```

Arquivos existentes:

```text
it-inventory-dev.yml
it-inventory-prod.yml

location-service-dev.yml
location-service-prod.yml
```

---

# Funcionamento do Config Server

O IT Inventory utiliza:

```text
spring.application.name = it-inventory
```

O Location Service utiliza:

```text
spring.application.name = location-service
```

Ao iniciar com o profile:

```text
prod
```

o IT Inventory consulta:

```text
http://config-server:8888/it-inventory/prod
```

e o Location Service consulta:

```text
http://config-server:8888/location-service/prod
```

Quando executado pela IDE, o endereço padrão pode ser:

```text
http://localhost:8888
```

---

# Exemplo de configuração centralizada

Exemplo do IT Inventory em produção:

```yaml
server:
  port: ${SERVER_PORT:8080}

spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://inventory-db:5432/itinventory}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false

services:
  location:
    url: ${LOCATION_SERVICE_URL:http://location-service:8081}
```

Location Service:

```yaml
server:
  port: ${SERVER_PORT:8081}

spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://location-db:5432/locationdb}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
```

---

# Docker

Cada aplicação Spring Boot possui um `Dockerfile`.

São geradas imagens independentes para:

```text
it-inventory
location-service
config-server
```

Os Dockerfiles executam os arquivos JAR utilizando Java 17.

Exemplo conceitual:

```dockerfile
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

# Geração dos arquivos JAR

Antes da construção das imagens, gerar os arquivos JAR.

## IT Inventory

Na raiz:

```bash
./mvnw package -DskipTests
```

Windows:

```powershell
.\mvnw.cmd package -DskipTests
```

## Location Service

```bash
cd location-service
./mvnw package -DskipTests
```

Windows:

```powershell
cd location-service
.\mvnw.cmd package -DskipTests
cd ..
```

## Config Server

```bash
cd config-server
./mvnw package -DskipTests
```

Windows:

```powershell
cd config-server
.\mvnw.cmd package -DskipTests
cd ..
```

Os JARs são gerados nas respectivas pastas:

```text
target/
location-service/target/
config-server/target/
```

---

# Docker Compose

Toda a solução pode ser iniciada utilizando:

```bash
docker compose up --build -d
```

O Docker Compose inicia:

```text
config-server
inventory-db
location-db
location-service
it-inventory
```

Para verificar o estado:

```bash
docker compose ps
```

Exemplo esperado:

```text
config-server      healthy
inventory-db       healthy
location-db        healthy
location-service   healthy
it-inventory       healthy
```

---

# Rede entre containers

O Docker Compose cria uma rede para comunicação entre os componentes.

Exemplo:

```text
it-inventory-network
```

Dentro dessa rede, os containers utilizam o nome do serviço como hostname.

Exemplo:

```text
it-inventory
       ↓
http://location-service:8081
```

Não é utilizado:

```text
http://localhost:8081
```

para comunicação entre containers.

O mesmo ocorre com os bancos:

```text
inventory-db:5432
location-db:5432
```

---

# Inicialização da solução

## Execução completa com Docker

Primeiro, gerar os JARs das três aplicações.

Depois:

```bash
docker compose up --build -d
```

Verificar:

```bash
docker compose ps
```

Para visualizar os logs:

```bash
docker compose logs
```

ou:

```bash
docker compose logs -f
```

Para um serviço específico:

```bash
docker compose logs it-inventory
```

```bash
docker compose logs location-service
```

```bash
docker compose logs config-server
```

---

# Parando os containers

Para parar toda a solução:

```bash
docker compose down
```

Os volumes dos bancos são preservados.

Para remover também os volumes seria necessário utilizar:

```bash
docker compose down -v
```

> O comando com `-v` remove os volumes e, consequentemente, os dados persistidos nos bancos.

---

# APIs

## IT Inventory

Base URL:

```text
http://localhost:8080
```

### Assets

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/assets` | Lista todos os ativos |
| GET | `/assets/{id}` | Busca um ativo por ID |
| POST | `/assets` | Cadastra um ativo |
| PUT | `/assets/{id}` | Atualiza um ativo |
| DELETE | `/assets/{id}` | Remove um ativo |
| GET | `/assets/active` | Lista ativos ativos |
| GET | `/assets/inactive` | Lista ativos inativos |
| GET | `/assets/ordered` | Lista ativos ordenados por nome |
| GET | `/assets/search?name=HRNT` | Pesquisa ativo pelo nome |

---

## Location Service

Base URL:

```text
http://localhost:8081
```

### Locations

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/locations` | Lista as localizações |
| GET | `/locations/{id}` | Busca localização por ID |
| POST | `/locations` | Cadastra uma localização |

---

# Swagger

## IT Inventory

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI:

```text
http://localhost:8080/v3/api-docs
```

## Location Service

```text
http://localhost:8081/swagger-ui/index.html
```

OpenAPI:

```text
http://localhost:8081/v3/api-docs
```

---

# Testando o Config Server

Com os containers em execução:

```bash
curl http://localhost:8888/it-inventory/prod
```

Resposta esperada contém:

```text
it-inventory-prod.yml
```

Também:

```bash
curl http://localhost:8888/location-service/prod
```

Resposta esperada contém:

```text
location-service-prod.yml
```

---

# Testando a execução integrada

Location Service:

```bash
curl http://localhost:8081/locations
```

Resposta esperada contém:

```text
Human Resources
NOC
Comercial
```

IT Inventory:

```bash
curl http://localhost:8080/assets
```

A resposta deverá conter os ativos e os nomes das localizações recuperados através do Location Service.

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

---

# Persistência dos dados

Os Loaders verificam se já existem registros antes da carga inicial.

Isso evita duplicação de dados quando as aplicações são reiniciadas.

Exemplo conceitual:

```text
Banco vazio
    ↓
Loader executa
    ↓
dados iniciais são criados
```

```text
Banco possui dados
    ↓
Loader identifica os registros
    ↓
carga inicial é ignorada
```

Isso se tornou necessário após a substituição do H2 pelos bancos PostgreSQL persistentes.

---

# Tratamento de falhas entre serviços

A aplicação principal trata diferentes situações de comunicação com o Location Service.

## Localização inexistente

```text
404 Not Found
```

Exemplo:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Localização não encontrada para o ID: 999"
}
```

## Location Service indisponível

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

---

# Testes com Postman

As coleções utilizadas nos testes encontram-se em:

```text
postman/
```

A collection principal testa:

- Location Service isoladamente;
- integração através do OpenFeign;
- CRUD de ativos;
- Bean Validation;
- localização inexistente;
- filtros;
- ordenação;
- pesquisa.

Existe também uma collection específica para validar o comportamento quando o Location Service está indisponível.

Resultado esperado:

```text
503 Service Unavailable
```

---

# Evolução arquitetural

## Etapa 1

A aplicação foi reorganizada por domínio.

```text
asset
catalog
location
shared
```

O objetivo foi melhorar a separação de responsabilidades e identificar candidatos para extração futura.

---

## Etapa 2

O gerenciamento de localizações foi extraído para uma aplicação Spring Boot independente.

Antes:

```text
AssetService
    ↓
LocationService
```

Depois:

```text
AssetService
    ↓
LocationClient
    ↓ HTTP
Location Service
```

Foi introduzido:

- OpenFeign;
- API REST independente;
- DTOs de comunicação;
- tratamento de falhas de rede;
- execução de aplicações independentes.

---

## Etapa 3

A solução foi preparada para execução Cloud Native.

Foram implementados:

- Profiles `dev` e `prod`;
- variáveis de ambiente;
- PostgreSQL;
- persistência independente;
- Spring Cloud Config Server;
- configuração centralizada;
- Dockerfiles;
- containerização dos bancos;
- volumes;
- rede Docker;
- Docker Compose;
- execução integrada dos componentes.

A solução deixou de depender de configurações fixas relacionadas ao ambiente de execução.

---

# Arquitetura final da Etapa 3

```text
                       ┌──────────────────────┐
                       │    Config Server     │
                       │        :8888         │
                       └──────────┬───────────┘
                                  │
                    ┌─────────────┴─────────────┐
                    ↓                           ↓
       ┌────────────────────────┐   ┌────────────────────────┐
       │      IT Inventory      │   │    Location Service    │
       │         :8080          │   │         :8081          │
       └────────────┬───────────┘   └────────────┬───────────┘
                    │                            │
                    ↓                            ↓
       ┌────────────────────────┐   ┌────────────────────────┐
       │     inventory-db       │   │      location-db       │
       │      PostgreSQL        │   │      PostgreSQL        │
       └────────────────────────┘   └────────────────────────┘

                    IT Inventory
                          │
                          │ OpenFeign / HTTP
                          ↓
                   Location Service
```

Todos os componentes são coordenados localmente através do Docker Compose.

---

# Reflexão arquitetural — Etapa 3

## 1. Quais configurações da aplicação podem variar entre ambientes?

Entre as configurações que podem variar estão:

- porta das aplicações;
- endereço dos bancos de dados;
- nome dos bancos;
- usuário dos bancos;
- senha dos bancos;
- URL do Location Service;
- URL do Config Server;
- profile ativo;
- configurações específicas de execução.

Esses valores dependem do ambiente onde a solução está sendo executada.

---

## 2. Quais dessas configurações foram externalizadas?

Foram externalizadas:

```text
SERVER_PORT
DB_URL
DB_USERNAME
DB_PASSWORD
LOCATION_SERVICE_URL
CONFIG_SERVER_URL
SPRING_PROFILES_ACTIVE
CONFIG_REPO_LOCATION
```

Além disso, as configurações dos ambientes `dev` e `prod` são fornecidas pelo Spring Cloud Config Server.

Dessa forma, mudanças relacionadas ao ambiente não exigem alterações no código Java.

---

## 3. Por que um serviço não deve acessar diretamente o banco de outro serviço?

Cada serviço deve ser responsável por seus próprios dados.

Se o IT Inventory acessasse diretamente as tabelas do Location Service, seria criado um forte acoplamento entre as aplicações.

Alterações internas no banco do Location Service poderiam quebrar a aplicação principal.

Por isso, a comunicação ocorre através da API disponibilizada pelo serviço:

```text
IT Inventory
      ↓ HTTP
Location Service
      ↓
Location Database
```

A API funciona como contrato entre as aplicações.

---

## 4. Qual problema o Docker resolve no projeto?

O Docker padroniza o ambiente necessário para executar os componentes.

Sem Docker, seria necessário instalar e configurar manualmente:

- PostgreSQL;
- versões de Java;
- portas;
- bancos;
- dependências de infraestrutura.

Com Docker, cada componente possui um ambiente previsível e reproduzível.

Isso reduz diferenças entre máquinas e facilita a execução da solução.

---

## 5. Qual é a função do Docker Compose?

O Docker Compose coordena a execução de múltiplos containers.

Neste projeto ele é responsável por iniciar e integrar:

```text
Config Server
IT Inventory
Location Service
Inventory Database
Location Database
```

Também é responsável por:

- criação da rede;
- definição das variáveis de ambiente;
- portas;
- volumes;
- dependências entre serviços;
- health checks.

Com um único comando:

```bash
docker compose up -d
```

é possível iniciar a infraestrutura completa.

---

## 6. Qual problema uma configuração centralizada procura resolver?

Em uma arquitetura distribuída, cada aplicação possui diversas configurações.

Sem centralização, seria necessário manter arquivos de configuração separados em cada projeto e alterar manualmente cada aplicação.

O Config Server cria um local centralizado para essas configurações.

No projeto:

```text
Config Server
      ├── it-inventory-dev
      ├── it-inventory-prod
      ├── location-service-dev
      └── location-service-prod
```

As aplicações recuperam suas configurações durante a inicialização.

Isso facilita:

- manutenção;
- organização;
- separação entre ambientes;
- redução de configurações duplicadas;
- alteração de parâmetros sem modificar código Java.

---

# Requisitos da Etapa 3 contemplados

A versão atual demonstra:

- revisão das configurações;
- profiles `dev` e `prod`;
- externalização das configurações;
- utilização de variáveis de ambiente;
- substituição do H2 por PostgreSQL;
- banco independente para cada serviço;
- ausência de acesso direto ao banco de outro serviço;
- Spring Cloud Config Server;
- configuração centralizada;
- Dockerfile para a aplicação principal;
- Dockerfile para o Location Service;
- Dockerfile para o Config Server;
- PostgreSQL em containers;
- volumes para persistência;
- Docker Compose;
- rede entre containers;
- comunicação sem utilização de `localhost` entre containers;
- health checks;
- execução integrada da solução.

---

# Tags do projeto

Os marcos da disciplina são identificados através de tags Git.

```text
etapa-1
etapa-2
etapa-3
```

## etapa-1

Representa a reorganização arquitetural da aplicação.

## etapa-2

Representa a extração do Location Service e a introdução da comunicação entre aplicações via OpenFeign.

## etapa-3

Representa a versão preparada para execução Cloud Native, incluindo:

- configurações externas;
- Profiles;
- Config Server;
- PostgreSQL;
- Docker;
- Docker Compose.

---

# Marco da Etapa 3

Ao concluir esta etapa, registrar:

```bash
git tag etapa-3
```

e enviar a tag:

```bash
git push origin etapa-3
```

A tag representa a versão da solução preparada para execução integrada e configurada externamente.

---

# Autor

Projeto desenvolvido por **Leonardo Chacarolli**.