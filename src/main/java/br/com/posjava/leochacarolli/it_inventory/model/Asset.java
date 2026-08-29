package br.com.posjava.leochacarolli.it_inventory.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Asset extends BaseEntity{

    private String name;
    private String serialNumber;
    private double purchaseValue;

    @ManyToOne
    private AssetModel model;

    @ManyToOne
    private Location location;

    public Asset() {
    }

    public Asset(Long id, boolean active, String name, String serialNumber, double purchaseValue, AssetModel model, Location location) {
        super(id, active);
        this.name = name;
        this.serialNumber = serialNumber;
        this.purchaseValue = purchaseValue;
        this.model = model;
        this.location = location;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(", Nome = %s, Serial Number = %s, Valor de Compra = %.2f, Modelo = %s, Localização = %s",
                name,
                serialNumber,
                purchaseValue,
                model.getName(),
                location.getName()
        );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public double getPurchaseValue() {
        return purchaseValue;
    }

    public void setPurchaseValue(double purchaseValue) {
        this.purchaseValue = purchaseValue;
    }

    public AssetModel getModel() {
        return model;
    }

    public void setModel(AssetModel model) {
        this.model = model;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
