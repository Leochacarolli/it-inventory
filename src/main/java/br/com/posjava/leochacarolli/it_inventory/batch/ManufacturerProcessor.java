package br.com.posjava.leochacarolli.it_inventory.batch;

import br.com.posjava.leochacarolli.it_inventory.catalog.model.Manufacturer;
import br.com.posjava.leochacarolli.it_inventory.catalog.repository.ManufacturerRepository;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ManufacturerProcessor
        implements ItemProcessor<ManufacturerCsvRow, Manufacturer> {

    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerProcessor(
            ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public Manufacturer process(ManufacturerCsvRow item) {

        String normalizedName =
                item.name().trim().toUpperCase();

        if (manufacturerRepository.existsByNameIgnoreCase(normalizedName)) {
            return null;
        }

        String normalizedCountry =
                item.country().trim();

        return new Manufacturer(
                null,
                item.active(),
                normalizedName,
                normalizedCountry,
                new ArrayList<>()
        );
    }
}