package upm.app.data.repositories;

import upm.app.data.models.ShoppingCart;

import java.util.List;

public interface ShoppingCartRepository extends GenericRepository<ShoppingCart> {
    List<ShoppingCart> findByUserId(Integer userId);
}
