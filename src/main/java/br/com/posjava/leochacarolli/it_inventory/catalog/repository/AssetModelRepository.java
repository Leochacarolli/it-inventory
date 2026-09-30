package br.com.posjava.leochacarolli.it_inventory.catalog.repository;

import br.com.posjava.leochacarolli.it_inventory.catalog.model.AssetModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetModelRepository extends JpaRepository<AssetModel, Long> {

}
