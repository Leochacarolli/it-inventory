package br.com.posjava.leochacarolli.it_inventory.location.repository;

import br.com.posjava.leochacarolli.it_inventory.location.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
