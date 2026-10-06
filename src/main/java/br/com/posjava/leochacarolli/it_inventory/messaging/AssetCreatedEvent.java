package br.com.posjava.leochacarolli.it_inventory.messaging;

public record AssetCreatedEvent(
        String eventType,
        Long assetId,
        String assetName,
        Long locationId
) {
}