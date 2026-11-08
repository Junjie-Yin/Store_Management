package upm.app.data.repositories;

import upm.app.data.models.Entity;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T extends Entity> {
    T create(T entity);

    T update(Integer id, T entity);

    Optional<T> read(Integer id);

    void delete(Integer id);

    List<T> findAll();

    int size();

    void clear();
}
