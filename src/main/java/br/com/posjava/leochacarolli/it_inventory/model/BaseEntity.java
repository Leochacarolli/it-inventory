package br.com.posjava.leochacarolli.it_inventory.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean active;

    protected BaseEntity() {
    }

    public BaseEntity(Long id, boolean active) {
        this.id = id;
        this.active = active;
    }

    @Override
    public String toString() {
        return String.format("ID = %d, Ativo? %B", id, active);
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
}
