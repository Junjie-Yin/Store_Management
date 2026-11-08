package upm.app.data.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upm.app.DependencyInjector;
import upm.app.data.models.Article;
import upm.app.data.models.ShoppingCart;
import upm.app.data.models.user.User;
import upm.app.data.repositories.exception.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartRepositoryTest {

    private final ShoppingCartRepository shoppingCartRepository = DependencyInjector.getInstance().getShoppingCartRepository();
    private final UserRepository userRepository = DependencyInjector.getInstance().getUserRepository();
    private final ArticleRepository articleRepository = DependencyInjector.getInstance().getArticleRepository();

    @BeforeEach
    void setUp() {
        DependencyInjector.getInstance().getSeeder().seed();
    }

    @Test
    void testCreateShoppingCart() {
        int initialSize = shoppingCartRepository.size();
        User testUser = userRepository.findByEmail("ana@example.com").orElseThrow();
        Article testArticle = articleRepository.findAll().stream().findFirst().orElseThrow();

        ShoppingCart newCart = new ShoppingCart(null, LocalDateTime.now(), testUser, testArticle, 1);
        ShoppingCart createdCart = shoppingCartRepository.create(newCart);

        assertNotNull(createdCart.getId());
        assertEquals(initialSize + 1, shoppingCartRepository.size());
        assertEquals(testUser.getId(), createdCart.getUser().getId());
        assertTrue(shoppingCartRepository.read(createdCart.getId()).isPresent());
    }

    @Test
    void testUpdateShoppingCart() {
        User ana = userRepository.findByEmail("ana@example.com").orElseThrow();
        ShoppingCart existingCart = shoppingCartRepository.findByUserId(ana.getId()).getFirst();
        Integer cartId = existingCart.getId();

        User juan = userRepository.findByEmail("juan@example.com").orElseThrow();
        existingCart.setUser(juan);

        ShoppingCart updatedCart = shoppingCartRepository.update(cartId, existingCart);

        assertEquals(juan.getId(), updatedCart.getUser().getId());
        assertEquals(juan.getId(), shoppingCartRepository.read(cartId).orElseThrow().getUser().getId());
    }

    @Test
    void testUpdateShoppingCartNotFound() {
        User testUser = userRepository.findByEmail("ana@example.com").orElseThrow();
        Article testArticle = articleRepository.findAll().stream().findFirst().orElseThrow();

        ShoppingCart nonExistentCart = new ShoppingCart(999, LocalDateTime.now(), testUser, testArticle, 1);
        assertThrows(EntityNotFoundException.class, () -> shoppingCartRepository.update(999, nonExistentCart));
    }

    @Test
    void testReadShoppingCart() {
        User ana = userRepository.findByEmail("ana@example.com").orElseThrow();
        Integer cartId = shoppingCartRepository.findByUserId(ana.getId()).getFirst().getId();

        Optional<ShoppingCart> cart = shoppingCartRepository.read(cartId);
        assertTrue(cart.isPresent());
        assertEquals(ana.getId(), cart.get().getUser().getId());
    }

    @Test
    void testReadShoppingCartNotFound() {
        Optional<ShoppingCart> cart = shoppingCartRepository.read(999);
        assertFalse(cart.isPresent());
    }

    @Test
    void testDeleteShoppingCart() {
        int initialSize = shoppingCartRepository.size();
        User ana = userRepository.findByEmail("ana@example.com").orElseThrow();
        Integer cartId = shoppingCartRepository.findByUserId(ana.getId()).getFirst().getId();

        shoppingCartRepository.delete(cartId);

        assertEquals(initialSize - 1, shoppingCartRepository.size());
        assertFalse(shoppingCartRepository.read(cartId).isPresent());
    }

    @Test
    void testFindAllShoppingCarts() {
        List<ShoppingCart> carts = shoppingCartRepository.findAll();
        assertFalse(carts.isEmpty());
        assertEquals(4, carts.size()); // 4 carts seeded
    }

    @Test
    void testFindByUserId() {
        User ana = userRepository.findByEmail("ana@example.com").orElseThrow();
        List<ShoppingCart> anaCarts = shoppingCartRepository.findByUserId(ana.getId());
        assertEquals(1, anaCarts.size());
        assertEquals(ana.getId(), anaCarts.getFirst().getUser().getId());

        User juan = userRepository.findByEmail("juan@example.com").orElseThrow();
        List<ShoppingCart> juanCarts = shoppingCartRepository.findByUserId(juan.getId());
        assertEquals(1, juanCarts.size());
        assertEquals(juan.getId(), juanCarts.getFirst().getUser().getId());
    }
}