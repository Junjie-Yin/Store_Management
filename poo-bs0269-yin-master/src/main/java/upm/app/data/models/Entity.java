package upm.app.data.models;

import upm.app.data.models.exceptions.InvalidAttributeException;

import java.util.Objects;

public class Entity {
    private Integer id;

    public Entity() {
    }

    public Entity(Integer id) {
        setId(id);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id != null && id <= 0) {
            throw new InvalidAttributeException("El id es inválido: " + id);
        }
        this.id = id;
    }

    @Override
    public boolean equals(Object entity) {
        if (this == entity) return true;
        if (entity == null || getClass() != entity.getClass()) return false;
        Entity other = (Entity) entity;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                '}';
    }
}