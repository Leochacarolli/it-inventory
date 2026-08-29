package br.com.posjava.leochacarolli.it_inventory.repository;

import br.com.posjava.leochacarolli.it_inventory.model.AssetModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetModelRepository extends JpaRepository<AssetModel, Long> {

}
