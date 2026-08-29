package br.com.posjava.leochacarolli.it_inventory.repository;

import br.com.posjava.leochacarolli.it_inventory.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {

}
