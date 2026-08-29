package br.com.posjava.leochacarolli.it_inventory.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Category extends BaseEntity{

    private String name;
    private String description;

    @OneToMany(mappedBy = "category")
    private List<AssetModel> models;

    public Category() {

    }

    public Category(Long id, boolean active, String name, String description, List<AssetModel> models) {
        super(id, active);
        this.name = name;
        this.description = description;
        this.models = models;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(", Nome = %s, Descrição = %s", name, description);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AssetModel> getModels() {
        return models;
    }

    public void setModels(List<AssetModel> models) {
        this.models = models;
    }
}
