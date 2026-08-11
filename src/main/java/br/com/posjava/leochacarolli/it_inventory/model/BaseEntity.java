package br.com.posjava.leochacarolli.it_inventory.model;

public abstract class BaseEntity {

    private Long id;
    private boolean active;

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
