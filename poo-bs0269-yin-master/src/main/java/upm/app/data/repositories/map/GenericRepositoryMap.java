package upm.app.data.repositories.map;

import upm.app.data.models.Entity;
import upm.app.data.repositories.GenericRepository;
import upm.app.data.repositories.exception.EntityNotFoundException;

import java.util.*;

public abstract class GenericRepositoryMap<T extends Entity> implements GenericRepository<T> {

    protected final Map<Integer, T> map;
    private Integer id;

    GenericRepositoryMap() {
        this.map = new HashMap<>();
        this.id = 1;
    }

    public T create(T entity) {
        entity.setId(this.id);
        this.map.put(this.id, entity);
        this.id++;
        return entity;
    }

    public T update(Integer id, T entity) {
        if (!this.map.containsKey(id)) {
            throw new EntityNotFoundException("Para actualizar, la id debe existir previamente: " + id);
        }
        entity.setId(id);
        this.map.put(id, entity);
        return entity;
    }

    public Optional<T> read(Integer id) {
        return Optional.ofNullable(this.map.get(id));
    }

    public void delete(Integer id) {
        this.map.remove(id);
    }

    public List<T> findAll() {
        return new ArrayList<>(map.values());
    }

    public void clear() {
        map.clear();
        id = 1;
    }

    public int size() {
        return map.size();
    }


}
