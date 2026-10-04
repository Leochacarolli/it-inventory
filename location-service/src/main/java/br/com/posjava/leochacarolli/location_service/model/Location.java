package br.com.posjava.leochacarolli.location_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean active;

    private String name;

    private int floor;

    private String description;

    public Location() {
    }

    public Location(
            Long id,
            boolean active,
            String name,
            int floor,
            String description) {

        this.id = id;
        this.active = active;
        this.name = name;
        this.floor = floor;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return String.format(
                "ID = %d, Ativo = %s, Nome = %s, Andar = %d, Descricao = %s",
                id,
                active,
                name,
                floor,
                description
        );
    }
}