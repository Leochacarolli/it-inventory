package br.com.posjava.leochacarolli.it_inventory.repository;

import br.com.posjava.leochacarolli.it_inventory.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByActive(boolean active);
    List<Asset> findAllByOrderByNameAsc();
    Optional<Asset> findFirstByNameContainingIgnoreCase(String name);
}
