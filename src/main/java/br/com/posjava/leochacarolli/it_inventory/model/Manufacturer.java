package br.com.posjava.leochacarolli.it_inventory.model;

import java.util.List;

public class Manufacturer extends BaseEntity {

    private String name;
    private String country;
    private List<AssetModel> models;

    public Manufacturer(Long id, boolean active, String name, String country, List<AssetModel> models) {
        super(id, active);
        this.name = name;
        this.country = country;
        this.models = models;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(", Nome = %s, País = %s", name, country);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<AssetModel> getModels() {
        return models;
    }

    public void setModels(List<AssetModel> models) {
        this.models = models;
    }
}
