package br.com.posjava.leochacarolli.it_inventory.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class AssetModel extends BaseEntity{

    private String name;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Manufacturer manufacturer;

    @OneToMany(mappedBy = "model")
    private List<Asset> assets;

    public AssetModel() {

    }

    public AssetModel(Long id, boolean active, String name, Category category, Manufacturer manufacturer, List<Asset> assets) {
        super(id, active);
        this.name = name;
        this.category = category;
        this.manufacturer = manufacturer;
        this.assets = assets;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(
                ", Nome = %s, Categoria = %s, Fabricante = %s",
                name,
                category.getName(),
                manufacturer.getName()
        );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }
}
