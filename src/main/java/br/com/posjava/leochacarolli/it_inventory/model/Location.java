package br.com.posjava.leochacarolli.it_inventory.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Location extends BaseEntity{

    private String name;
    private int floor;
    private String description;

    @OneToMany(mappedBy = "location")
    private List<Asset> assets;

    public Location() {

    }

    public Location(Long id, boolean active, String name, int floor, String description, List<Asset> assets) {
        super(id, active);
        this.name = name;
        this.floor = floor;
        this.description = description;
        this.assets = assets;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(", Nome = %s, Andar = %d, Descricao = %s", name, floor, description);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }
}
