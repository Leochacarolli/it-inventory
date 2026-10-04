# Testes Postman - Etapa 2

## Coleção principal

Arquivo: `it-inventory.postman_collection.json`

1. Inicie `LocationServiceApplication` (porta 8081).
2. Inicie `ItInventoryApplication` (porta 8080).
3. Importe a coleção no Postman.
4. Execute a coleção inteira pelo Collection Runner, na ordem em que as pastas aparecem.

A coleção testa o `location-service` isoladamente, a integração via OpenFeign, CRUD de ativos, Bean Validation e localização remota inexistente (404). O ID do ativo criado é salvo automaticamente na variável `createdAssetId` e reutilizado no PUT e DELETE.

## Teste de indisponibilidade (503)

Arquivo: `it-inventory-service-unavailable.postman_collection.json`

1. Mantenha apenas `ItInventoryApplication` (8080) ligado.
2. Pare `LocationServiceApplication` (8081).
3. Execute a coleção.
4. O resultado esperado é `503 Service Unavailable`.
