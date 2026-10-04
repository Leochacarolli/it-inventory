package br.com.posjava.leochacarolli.it_inventory.asset.model;

import br.com.posjava.leochacarolli.it_inventory.catalog.model.AssetModel;
import br.com.posjava.leochacarolli.it_inventory.shared.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Asset extends BaseEntity {

    private String name;
    private String serialNumber;
    private double purchaseValue;

    @ManyToOne
    private AssetModel model;

    @Column(nullable = false)
    private Long locationId;

    public Asset() {
    }

    public Asset(
            Long id,
            boolean active,
            String name,
            String serialNumber,
            double purchaseValue,
            AssetModel model,
            Long locationId) {

        super(id, active);
        this.name = name;
        this.serialNumber = serialNumber;
        this.purchaseValue = purchaseValue;
        this.model = model;
        this.locationId = locationId;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(
                ", Nome = %s, Serial Number = %s, Valor de Compra = %.2f, Modelo = %s, Location ID = %d",
                name,
                serialNumber,
                purchaseValue,
                model.getName(),
                locationId
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

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
}