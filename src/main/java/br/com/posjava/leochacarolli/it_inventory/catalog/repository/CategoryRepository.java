package br.com.posjava.leochacarolli.it_inventory.catalog.repository;

import br.com.posjava.leochacarolli.it_inventory.catalog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
