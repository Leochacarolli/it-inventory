package br.com.posjava.leochacarolli.location_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class LocationRequestDTO {

    private boolean active;

    @NotBlank(message = "O nome da localização é obrigatório")
    private String name;

    @PositiveOrZero(message = "O andar não pode ser negativo")
    private int floor;

    private String description;

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
}