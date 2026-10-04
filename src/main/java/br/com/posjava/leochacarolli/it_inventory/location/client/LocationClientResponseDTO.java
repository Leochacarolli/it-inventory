package br.com.posjava.leochacarolli.it_inventory.location.client;

public class LocationClientResponseDTO {

    private Long id;
    private boolean active;
    private String name;
    private int floor;
    private String description;

    public LocationClientResponseDTO() {
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}