package br.com.posjava.leochacarolli.location_service.repository;

import br.com.posjava.leochacarolli.location_service.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}