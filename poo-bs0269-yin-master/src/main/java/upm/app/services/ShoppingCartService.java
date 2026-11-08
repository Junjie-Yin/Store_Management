package upm.app.services;

import upm.app.data.models.ShoppingCart;
import upm.app.data.repositories.ShoppingCartRepository;
import upm.app.services.exceptions.NotFoundException;

import java.util.List;

public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public List<ShoppingCart> findAll() {
        return shoppingCartRepository.findAll();
    }

    public List<ShoppingCart> findByUserId(Integer userId) {
        return shoppingCartRepository.findByUserId(userId);
    }

    public ShoppingCart read(Integer id) {
        return shoppingCartRepository.read(id)
                .orElseThrow(() -> new NotFoundException("No existe un carrito con id: " + id));
    }
}
