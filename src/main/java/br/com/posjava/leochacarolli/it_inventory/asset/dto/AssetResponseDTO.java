package br.com.posjava.leochacarolli.it_inventory.asset.dto;

import br.com.posjava.leochacarolli.it_inventory.asset.model.Asset;

public class AssetResponseDTO {

    private Long id;
    private boolean active;
    private String name;
    private String serialNumber;
    private double purchaseValue;
    private String model;
    private String location;

    public AssetResponseDTO(Asset asset, String locationName) {
        this.id = asset.getId();
        this.active = asset.isActive();
        this.name = asset.getName();
        this.serialNumber = asset.getSerialNumber();
        this.purchaseValue = asset.getPurchaseValue();
        this.model = asset.getModel().getName();
        this.location = locationName;
    }

    public Long getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public String getName() {
        return name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public double getPurchaseValue() {
        return purchaseValue;
    }

    public String getModel() {
        return model;
    }

    public String getLocation() {
        return location;
    }
}