package br.com.posjava.leochacarolli.it_inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.NotNull;

public class AssetRequestDTO {

    private boolean active;

    @NotBlank
    private String name;

    private String serialNumber;

    @PositiveOrZero
    private double purchaseValue;

    @NotNull
    private Long assetModelId;

    @NotNull
    private Long locationId;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
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

    public Long getAssetModelId() {
        return assetModelId;
    }

    public void setAssetModelId(Long assetModelId) {
        this.assetModelId = assetModelId;
    }
}
