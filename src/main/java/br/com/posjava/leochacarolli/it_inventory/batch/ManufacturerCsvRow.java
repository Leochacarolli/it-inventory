package br.com.posjava.leochacarolli.it_inventory.batch;

public record ManufacturerCsvRow(
        String name,
        String country,
        boolean active
) {
}