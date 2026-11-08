package upm.app.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upm.app.DependencyInjector;
import upm.app.data.models.ShoppingCart;
import upm.app.data.models.user.User;
import upm.app.services.exceptions.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartServiceTest {

    private final ShoppingCartService shoppingCartService = DependencyInjector.getInstance().getShoppingCartService();
    private final DependencyInjector dependencyInjector = DependencyInjector.getInstance();
    private User ana;
    private User juan;

    @BeforeEach
    void setUp() {
        dependencyInjector.getSeeder().seed();
        ana = dependencyInjector.getUserRepository().findByEmail("ana@example.com").orElseThrow();
        juan = dependencyInjector.getUserRepository().findByEmail("juan@example.com").orElseThrow();
    }

    @Test
    void testFindAll() {
        List<ShoppingCart> carts = shoppingCartService.findAll();
        assertNotNull(carts);
        assertEquals(4, carts.size());
    }

    @Test
    void testFindByUserId() {
        List<ShoppingCart> anaCarts = shoppingCartService.findByUserId(ana.getId());
        assertEquals(1, anaCarts.size());
        assertEquals(ana.getId(), anaCarts.getFirst().getUser().getId());

        List<ShoppingCart> juanCarts = shoppingCartService.findByUserId(juan.getId());
        assertEquals(1, juanCarts.size());
        assertEquals(juan.getId(), juanCarts.getFirst().getUser().getId());
    }

    @Test
    void testFindByUserIdNotFound() {
        List<ShoppingCart> nonExistentUserCarts = shoppingCartService.findByUserId(999);
        assertTrue(nonExistentUserCarts.isEmpty());
    }

    @Test
    void testReadShoppingCart() {
        ShoppingCart anaCart = shoppingCartService.findByUserId(ana.getId()).getFirst();
        ShoppingCart foundCart = shoppingCartService.read(anaCart.getId());
        assertEquals(anaCart.getId(), foundCart.getId());
        assertEquals(ana.getId(), foundCart.getUser().getId());
    }

    @Test
    void testReadShoppingCartNotFound() {
        assertThrows(NotFoundException.class, () -> shoppingCartService.read(999));
    }
}