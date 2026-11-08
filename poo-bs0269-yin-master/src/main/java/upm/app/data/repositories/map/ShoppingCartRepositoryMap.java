package upm.app.data.repositories.map;

import upm.app.data.models.ShoppingCart;
import upm.app.data.repositories.ShoppingCartRepository;

import java.util.List;

public class ShoppingCartRepositoryMap extends GenericRepositoryMap<ShoppingCart> implements ShoppingCartRepository {
    public ShoppingCartRepositoryMap() {
        super();
    }

    public List<ShoppingCart> findByUserId(Integer userId) {
        return map.values().stream()
                .filter(cart -> cart.getUser() != null && userId.equals(cart.getUser().getId()))
                .toList();
    }
}
