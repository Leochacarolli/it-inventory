package br.com.posjava.leochacarolli.it_inventory.catalog.service;

import br.com.posjava.leochacarolli.it_inventory.catalog.model.Manufacturer;
import br.com.posjava.leochacarolli.it_inventory.catalog.repository.ManufacturerRepository;
import org.springframework.stereotype.Service;

@Service
public class ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerService(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    public void addManufacturer(Manufacturer manufacturer) {
        manufacturerRepository.save(manufacturer);
    }
}
