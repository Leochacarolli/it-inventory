# IT Inventory API

API REST desenvolvida em Java com Spring Boot para gerenciamento de ativos de TI.

O projeto foi desenvolvido de forma evolutiva durante a disciplina, passando por diferentes etapas de arquitetura, persistência, comunicação entre serviços, configuração externa, containerização, mensageria e processamento em lote.

## Repositório

GitHub:

https://github.com/Leochacarolli/it-inventory

---

# Visão geral

O sistema possui como principal responsabilidade o gerenciamento de ativos de TI, permitindo cadastrar, consultar, atualizar e remover ativos.

Ao longo das etapas, a aplicação evoluiu de uma implementação orientada a objetos com armazenamento em memória para uma arquitetura distribuída composta por:

- API principal de inventário;
- serviço independente de localizações;
- Config Server;
- bancos PostgreSQL independentes;
- RabbitMQ;
- consumidor assíncrono de eventos;
- processamento em lote com Spring Batch;
- execução integrada através de Docker Compose.

---

# Tecnologias utilizadas

- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Validation
- Spring Cloud OpenFeign
- Spring Cloud Config
- Spring AMQP
- RabbitMQ
- Spring Batch
- PostgreSQL
- Docker
- Docker Compose
- Maven
- Postman
- Git / GitHub

---

# Arquitetura final

Ao final da Etapa 4, a aplicação possui a seguinte organização:

```text
                        Config Server
                             │
                 ┌───────────┴───────────┐
                 │                       │
                 ▼                       ▼
          IT Inventory            Location Service
                 │                       │
                 ▼                       ▼
          inventory-db              location-db
          PostgreSQL                PostgreSQL

                 │
                 │ ASSET_CREATED
                 ▼
              RabbitMQ
                 │
                 ▼
          Activity Service


          manufacturers.csv
                 │
                 ▼
           Spring Batch
                 │
       ┌─────────┼─────────┐
       ▼         ▼         ▼
     Reader   Processor   Writer
                           │
                           ▼
                  ManufacturerRepository
                           │
                           ▼
                     inventory-db
```

---

# Componentes da solução

## IT Inventory

Aplicação principal responsável pelo gerenciamento dos ativos.

Principais responsabilidades:

- CRUD de ativos;
- consulta de modelos e dados de catálogo;
- comunicação com o Location Service;
- persistência dos ativos;
- publicação de eventos no RabbitMQ;
- execução do processamento Spring Batch.

Porta padrão:

```text
8080
```

---

## Location Service

Microsserviço responsável pelas informações de localização.

Exemplos:

- Human Resources;
- NOC;
- Comercial.

A aplicação principal não acessa diretamente o banco deste serviço.

A comunicação ocorre através de HTTP utilizando OpenFeign.

Porta padrão:

```text
8081
```

---

## Config Server

Responsável por centralizar configurações que podem variar entre os ambientes.

Entre as configurações centralizadas estão:

- portas;
- URLs dos bancos;
- credenciais externas;
- URL do Location Service;
- configurações do RabbitMQ;
- configurações específicas dos profiles.

Porta padrão:

```text
8888
```

---

## Activity Service

Serviço responsável pelo consumo assíncrono dos eventos relacionados aos ativos.

Atualmente consome o evento:

```text
ASSET_CREATED
```

Esse serviço não possui uma API HTTP própria.

Sua responsabilidade é receber mensagens publicadas pela aplicação principal através do RabbitMQ.

---

## RabbitMQ

Broker utilizado para comunicação assíncrona.

Portas:

```text
5672  -> comunicação AMQP
15672 -> interface de administração
```

Estrutura utilizada:

```text
Exchange:
asset.exchange

Routing Key:
asset.created

Queue:
asset.activity.queue
```

---

# Bancos de dados

A solução utiliza dois bancos PostgreSQL independentes.

## Inventory Database

Responsável pelos dados pertencentes à aplicação principal.

Porta local:

```text
5432
```

Banco:

```text
itinventory
```

---

## Location Database

Responsável exclusivamente pelos dados do Location Service.

Porta local:

```text
5433
```

Banco:

```text
locationdb
```

Cada serviço possui responsabilidade sobre seus próprios dados.

O IT Inventory não acessa diretamente as tabelas do Location Service.

---

# Estrutura do projeto

```text
it-inventory/
│
├── activity-service/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── config-server/
│   ├── config-repo/
│   │   ├── it-inventory-dev.yml
│   │   ├── it-inventory-prod.yml
│   │   ├── location-service-dev.yml
│   │   └── location-service-prod.yml
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── location-service/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── postman/
│
├── src/
│   └── main/
│       ├── java/
│       └── resources/
│           └── batch/
│               └── manufacturers.csv
│
├── .env.example
├── compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

---

# Profiles

A aplicação utiliza profiles diferentes para desenvolvimento e produção.

## Desenvolvimento

Profile:

```text
dev
```

Neste ambiente, as aplicações executadas pela IDE utilizam endereços locais.

Exemplos:

```text
PostgreSQL:
localhost:5432

Location Service:
localhost:8081

RabbitMQ:
localhost:5672
```

---

## Produção / Docker

Profile:

```text
prod
```

Quando as aplicações são executadas através do Docker Compose, a comunicação utiliza os nomes dos serviços da rede Docker.

Exemplos:

```text
inventory-db:5432

location-db:5432

location-service:8081

rabbitmq:5672

config-server:8888
```

Não é utilizado `localhost` para comunicação entre containers.

---

# Variáveis de ambiente

O projeto utiliza variáveis de ambiente para configurações que podem mudar entre ambientes.

O arquivo `.env` não é versionado.

O repositório contém apenas:

```text
.env.example
```

Exemplo:

```env
INVENTORY_DB_NAME=itinventory
INVENTORY_DB_USER=itinventory
INVENTORY_DB_PASSWORD=change-me

LOCATION_DB_NAME=locationdb
LOCATION_DB_USER=location
LOCATION_DB_PASSWORD=change-me

RABBITMQ_USER=inventory
RABBITMQ_PASSWORD=change-me
```

Para execução local, crie o `.env` a partir do exemplo:

```powershell
Copy-Item .env.example .env
```

Depois configure os valores desejados.

---

# Execução com Docker Compose

## Pré-requisitos

- Java 17
- Docker Desktop
- Docker Compose
- Maven ou Maven Wrapper

---

## 1. Gerar os arquivos JAR

### IT Inventory

Na raiz:

```powershell
.\mvnw.cmd package -DskipTests
```

### Location Service

```powershell
cd location-service
.\mvnw.cmd package -DskipTests
cd ..
```

### Config Server

```powershell
cd config-server
.\mvnw.cmd package -DskipTests
cd ..
```

### Activity Service

```powershell
cd activity-service
.\mvnw.cmd package -DskipTests
cd ..
```

---

## 2. Validar o Compose

```powershell
docker compose config
```

---

## 3. Construir as imagens

```powershell
docker compose build
```

---

## 4. Iniciar a solução

```powershell
docker compose up -d
```

---

## 5. Verificar os containers

```powershell
docker compose ps
```

Os componentes principais são:

```text
config-server
inventory-db
location-db
rabbitmq
location-service
activity-service
it-inventory
```

---

## 6. Encerrar a solução

```powershell
docker compose down
```

Os volumes dos bancos e do RabbitMQ preservam os dados.

Para remover também os volumes:

```powershell
docker compose down -v
```

---

# Endpoints principais

## Ativos

Base:

```text
http://localhost:8080/assets
```

### Listar todos

```http
GET /assets
```

### Buscar por ID

```http
GET /assets/{id}
```

### Criar ativo

```http
POST /assets
```

Exemplo:

```json
{
  "active": true,
  "name": "NOTEBOOK01",
  "serialNumber": "ABC123",
  "purchaseValue": 3500,
  "assetModelId": 1,
  "locationId": 1
}
```

### Atualizar ativo

```http
PUT /assets/{id}
```

### Remover ativo

```http
DELETE /assets/{id}
```

### Listar ativos ativos

```http
GET /assets/active
```

### Listar ativos inativos

```http
GET /assets/inactive
```

### Listar ordenados por nome

```http
GET /assets/ordered
```

### Buscar por nome

```http
GET /assets/search?name=NOTEBOOK
```

---

# Location Service

Base:

```text
http://localhost:8081/locations
```

### Listar localizações

```http
GET /locations
```

### Buscar localização

```http
GET /locations/{id}
```

---

# Comunicação síncrona

A consulta de localização de um ativo utiliza comunicação síncrona através de API REST.

Fluxo:

```text
IT Inventory
     │
     │ HTTP / OpenFeign
     ▼
Location Service
```

Ao consultar um ativo, a aplicação principal obtém a localização correspondente através do serviço independente.

Também existe tratamento para indisponibilidade do Location Service.

Quando o serviço não está disponível, a aplicação principal retorna uma resposta tratada informando a indisponibilidade temporária.

---

# Comunicação assíncrona com RabbitMQ

Na Etapa 4 foi implementada comunicação assíncrona para registrar a criação de um ativo.

Quando um novo ativo é criado:

```text
POST /assets
     │
     ▼
AssetService
     │
     ├── salva no PostgreSQL
     │
     └── publica ASSET_CREATED
                │
                ▼
            RabbitMQ
                │
                ▼
         Activity Service
```

Exemplo de mensagem:

```json
{
  "eventType": "ASSET_CREATED",
  "assetId": 6,
  "assetName": "DOCKERNT01",
  "locationId": 1
}
```

O produtor não precisa esperar o consumidor concluir o processamento.

---

# Teste de indisponibilidade do consumidor

Foi testado o seguinte cenário:

```text
Activity Service desligado
        │
        ▼
Ativo criado no IT Inventory
        │
        ▼
Mensagem publicada no RabbitMQ
        │
        ▼
Mensagem permanece em:
asset.activity.queue
        │
        ▼
Ready = 1
```

Depois o Activity Service foi iniciado.

Resultado:

```text
Activity Service iniciado
        │
        ▼
consumidor conecta ao RabbitMQ
        │
        ▼
mensagem é processada
        │
        ▼
Ready = 0
```

Isso demonstra uma diferença importante em relação à comunicação REST.

Na mensageria, a mensagem pode permanecer aguardando até que o consumidor esteja disponível novamente.

---

# Spring Batch

Também foi implementado processamento em lote utilizando Spring Batch.

A funcionalidade escolhida foi:

```text
Importação de fabricantes através de arquivo CSV
```

Arquivo:

```text
src/main/resources/batch/manufacturers.csv
```

Exemplo:

```csv
name,country,active
HP,USA,true
Acer,Taiwan,true
Asus,Taiwan,true
Samsung,South Korea,true
Positivo,Brazil,true
```

---

# Fluxo do Batch

```text
manufacturers.csv
       │
       ▼
FlatFileItemReader
       │
       ▼
ManufacturerCsvRow
       │
       ▼
ManufacturerProcessor
       │
       ▼
Manufacturer
       │
       ▼
RepositoryItemWriter
       │
       ▼
ManufacturerRepository
       │
       ▼
PostgreSQL
```

---

## ItemReader

Responsável pela leitura do arquivo CSV.

O cabeçalho é ignorado e cada linha é convertida para:

```text
ManufacturerCsvRow
```

---

## ItemProcessor

Responsável pela aplicação das regras de processamento.

Entre as regras utilizadas:

- remoção de espaços desnecessários;
- normalização do nome para letras maiúsculas;
- verificação de fabricante já existente.

Quando o fabricante já existe no banco, o Processor retorna `null` e o registro não segue para escrita.

Isso evita duplicação quando o Job é executado novamente.

---

## ItemWriter

Utiliza:

```text
ManufacturerRepository
```

para persistir os fabricantes processados no PostgreSQL da aplicação principal.

---

## Processamento em chunks

O Step foi configurado com:

```java
chunk(2)
```

Isso significa que o processamento ocorre em grupos controlados de dois registros.

Exemplo:

```text
registros 1 e 2
→ processamento
→ escrita
→ commit

registros 3 e 4
→ processamento
→ escrita
→ commit

registro 5
→ processamento
→ escrita
→ commit
```

---

# Validação do Spring Batch

A execução foi validada através dos logs:

```text
manufacturerImportJob launched

Executing step:
manufacturerImportStep

status:
COMPLETED
```

Também foi validada a persistência no PostgreSQL.

Exemplo dos fabricantes disponíveis após a importação:

```text
Dell
Lenovo
HP
ACER
ASUS
SAMSUNG
POSITIVO
```

Uma nova execução do Job não duplica os fabricantes existentes.

---

# Postman

As collections utilizadas durante o desenvolvimento estão disponíveis em:

```text
postman/
```

Elas incluem cenários relacionados a:

- CRUD de ativos;
- consultas;
- integração com Location Service;
- tratamento de erros;
- indisponibilidade do serviço remoto.

---

# Evolução do projeto

## Etapa 1 — Orientação a Objetos

Objetivo principal:

```text
Modelo Orientado a Objetos
```

Foram desenvolvidas as entidades e relacionamentos iniciais do domínio.

Conceitos utilizados:

- classes;
- encapsulamento;
- herança;
- classes abstratas;
- composição;
- relacionamentos entre objetos.

Tag:

```text
etapa-1
```

---

## Etapa 2 — Microsserviços e comunicação

A solução evoluiu para separar a responsabilidade de localizações em um serviço independente.

Foram introduzidos:

- Spring Data JPA;
- PostgreSQL;
- Location Service;
- OpenFeign;
- comunicação HTTP;
- tratamento de indisponibilidade;
- independência entre os dados dos serviços.

Tag:

```text
etapa-2
```

---

## Etapa 3 — Cloud Native

Nesta etapa foram introduzidos:

- externalização de configurações;
- profiles `dev` e `prod`;
- variáveis de ambiente;
- PostgreSQL em containers;
- bancos independentes;
- Spring Cloud Config Server;
- Dockerfiles;
- Docker Compose;
- rede interna entre containers;
- volumes persistentes.

Tag:

```text
etapa-3
```

---

## Etapa 4 — Mensageria e Batch

A etapa final adicionou:

- RabbitMQ;
- produtor de mensagens;
- exchange;
- routing key;
- fila;
- consumidor assíncrono;
- Activity Service;
- teste com consumidor indisponível;
- Spring Batch;
- processamento CSV;
- ItemReader;
- ItemProcessor;
- ItemWriter;
- Job;
- Step;
- chunks;
- persistência dos dados processados.

Tag:

```text
etapa-4
```

---

# Reflexão arquitetural — Etapa 3

## 1. Quais configurações da aplicação podem variar entre ambientes?

Entre as principais configurações estão:

- portas;
- endereço dos bancos;
- usuário e senha dos bancos;
- URL do Location Service;
- endereço do Config Server;
- endereço do RabbitMQ;
- profile ativo.

---

## 2. Quais dessas configurações foram externalizadas?

Foram externalizadas:

- configuração dos bancos PostgreSQL;
- credenciais;
- portas;
- URL do Location Service;
- URL do Config Server;
- configurações do RabbitMQ;
- profile de execução.

Essas informações podem ser fornecidas através do Config Server e de variáveis de ambiente.

---

## 3. Por que um serviço não deve acessar diretamente o banco de outro serviço?

Cada serviço deve ser responsável pelos próprios dados.

Se o IT Inventory acessasse diretamente as tabelas do Location Service, os dois serviços ficariam fortemente acoplados à estrutura interna do banco.

Utilizando uma API, o serviço consumidor depende apenas do contrato disponibilizado pelo outro serviço.

---

## 4. Qual problema o Docker resolve no projeto?

O Docker padroniza o ambiente de execução.

Com ele, as aplicações podem ser executadas utilizando as mesmas versões e configurações independentemente da máquina utilizada.

Isso reduz diferenças entre ambientes e facilita a reprodução da aplicação.

---

## 5. Qual é a função do Docker Compose?

O Docker Compose permite coordenar a execução de vários componentes utilizando um único arquivo.

No projeto ele é responsável por iniciar e conectar:

- IT Inventory;
- Location Service;
- Activity Service;
- Config Server;
- RabbitMQ;
- Inventory Database;
- Location Database.

---

## 6. Qual problema uma configuração centralizada procura resolver?

A configuração centralizada evita que cada serviço mantenha todas as configurações diretamente dentro do próprio projeto.

Com o Config Server, propriedades que variam entre ambientes podem ser centralizadas e distribuídas para as aplicações.

Isso facilita manutenção e alterações de configuração.

---

# Reflexão arquitetural — Etapa 4

## 1. Qual operação foi escolhida para comunicação assíncrona?

Foi escolhido o registro da criação de um ativo.

Após o cadastro de um ativo, o IT Inventory publica um evento:

```text
ASSET_CREATED
```

Esse evento é enviado ao RabbitMQ e posteriormente processado pelo Activity Service.

---

## 2. Por que essa operação não precisa ser concluída durante a requisição original?

O cadastro do ativo é concluído quando o ativo é validado e persistido.

O registro da atividade não precisa bloquear a resposta HTTP.

Por isso, o processamento pode ocorrer posteriormente através de uma mensagem assíncrona.

---

## 3. O que acontece com a mensagem caso o consumidor esteja temporariamente indisponível?

A mensagem permanece armazenada na fila:

```text
asset.activity.queue
```

Quando o Activity Service volta a ficar disponível, ele se conecta ao RabbitMQ e processa a mensagem que estava aguardando.

---

## 4. Qual funcionalidade foi escolhida para processamento em lote?

Foi escolhida a importação de fabricantes através de um arquivo CSV.

O Spring Batch lê diversos fabricantes, aplica regras de normalização e persistência e grava os dados no banco da aplicação principal.

---

## 5. Por que essa funcionalidade é adequada para Batch?

A importação trabalha com um conjunto de registros que pode ser processado sequencialmente.

Ela não precisa ser executada como uma requisição individual para cada fabricante.

O Spring Batch também permite controlar o processamento através de chunks.

---

## 6. Em quais situações da aplicação seria mais adequado utilizar REST, mensageria ou Batch?

### REST

É mais adequado quando a aplicação precisa de uma resposta imediata.

Exemplo:

```text
IT Inventory → Location Service
```

A aplicação precisa saber a localização durante a operação.

### Mensageria

É adequada quando uma operação pode acontecer posteriormente e não precisa bloquear a requisição original.

Exemplo:

```text
ASSET_CREATED
```

O registro da atividade pode ocorrer de forma assíncrona.

### Batch

É adequado quando existe um conjunto de registros que precisa ser processado de maneira estruturada.

Exemplo:

```text
importação de fabricantes através de CSV
```

---

# Comandos úteis

## Ver containers

```powershell
docker compose ps
```

## Visualizar logs do IT Inventory

```powershell
docker compose logs it-inventory
```

## Visualizar logs do Activity Service

```powershell
docker compose logs activity-service
```

## Acompanhar consumidor em tempo real

```powershell
docker compose logs -f activity-service
```

## Verificar fabricantes

```powershell
docker exec -it inventory-db psql -U itinventory -d itinventory -c "select id, name, country, active from manufacturer order by id;"
```

---

# Marcos do projeto

As principais versões foram registradas utilizando tags Git:

```text
etapa-1
etapa-2
etapa-3
etapa-4
```

Cada tag representa um momento específico da evolução da mesma aplicação.

---

# Autor

**Leonardo Chacarolli**

Projeto desenvolvido como atividade acadêmica da Pós-Graduação em Engenharia de Software com Java.