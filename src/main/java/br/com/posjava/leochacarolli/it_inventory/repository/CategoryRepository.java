package br.com.posjava.leochacarolli.it_inventory.repository;

import br.com.posjava.leochacarolli.it_inventory.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
