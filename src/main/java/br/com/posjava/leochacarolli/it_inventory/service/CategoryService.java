package br.com.posjava.leochacarolli.it_inventory.service;

import br.com.posjava.leochacarolli.it_inventory.exception.CategoryNotFoundException;
import br.com.posjava.leochacarolli.it_inventory.model.Category;
import br.com.posjava.leochacarolli.it_inventory.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Categoria não encontrada para o ID: " + id));
    }
}
