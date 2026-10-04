package br.com.posjava.leochacarolli.location_service.dto;

import br.com.posjava.leochacarolli.location_service.model.Location;

public class LocationResponseDTO {

    private Long id;
    private boolean active;
    private String name;
    private int floor;
    private String description;

    public LocationResponseDTO(Location location) {
        this.id = location.getId();
        this.active = location.isActive();
        this.name = location.getName();
        this.floor = location.getFloor();
        this.description = location.getDescription();
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

    public int getFloor() {
        return floor;
    }

    public String getDescription() {
        return description;
    }
}