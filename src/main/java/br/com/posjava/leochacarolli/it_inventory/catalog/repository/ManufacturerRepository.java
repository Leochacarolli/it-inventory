package br.com.posjava.leochacarolli.it_inventory.catalog.repository;

import br.com.posjava.leochacarolli.it_inventory.catalog.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerRepository
        extends JpaRepository<Manufacturer, Long> {

    boolean existsByNameIgnoreCase(String name);
}