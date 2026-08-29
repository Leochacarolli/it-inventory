package br.com.posjava.leochacarolli.it_inventory.repository;

import br.com.posjava.leochacarolli.it_inventory.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
}
