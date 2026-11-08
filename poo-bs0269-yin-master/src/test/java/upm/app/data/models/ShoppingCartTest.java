package upm.app.data.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upm.app.data.models.exceptions.InvalidAttributeException;
import upm.app.data.models.user.Role;
import upm.app.data.models.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    private User user;
    private Article article;
    private ShoppingCart shoppingCart;

    @BeforeEach
    void setUp() {
        user = User.builder("test@example.com", "Test", "User", "12345678Z", Role.ALUMNO)
                .id(1)
                .build();
        article = new Article(1, "BAR123", "Laptop", 1200.0, LocalDate.now(), "HP", 10);

        shoppingCart = new ShoppingCart(1, LocalDateTime.now(), user, article, 1);
    }

    @Test
    void testShoppingCartConstructorAndGetters() {
        assertEquals(1, shoppingCart.getId());
        assertNotNull(shoppingCart.getCreationDate());
        assertEquals(user, shoppingCart.getUser());
        assertEquals(article, shoppingCart.getArticle());
        assertEquals(1, shoppingCart.getAmount());
        assertEquals(0.0, shoppingCart.getDiscount());
    }

    @Test
    void testSetCreationDateTimeValid() {
        LocalDateTime newDate = LocalDateTime.of(2023, 1, 1, 10, 0);
        shoppingCart.setCreationDateTime(newDate);
        assertEquals(newDate, shoppingCart.getCreationDate());
    }

    @Test
    void testSetCreationDateTimeInvalidNull() {
        assertThrows(IllegalArgumentException.class, () -> shoppingCart.setCreationDateTime(null));
    }

    @Test
    void testSetUserValid() {
        User newUser = User.builder("new@example.com", "New", "User", "87654321A", Role.PROFESOR)
                .id(2)
                .build();
        shoppingCart.setUser(newUser);
        assertEquals(newUser, shoppingCart.getUser());
    }

    @Test
    void testSetUserInvalidNull() {
        assertThrows(InvalidAttributeException.class, () -> shoppingCart.setUser(null));
    }

    @Test
    void testSetArticleValid() {
        Article newArticle = new Article(2, "NEW123", "Mouse", 20.0, LocalDate.now(), "Logi", 50);
        shoppingCart.setArticle(newArticle);
        assertEquals(newArticle, shoppingCart.getArticle());
    }

    @Test
    void testSetArticleInvalidNull() {
        assertThrows(InvalidAttributeException.class, () -> shoppingCart.setArticle(null));
    }

    @Test
    void testSetAmountValidNoDiscount() {
        shoppingCart.setAmount(2);
        assertEquals(2, shoppingCart.getAmount());
        assertEquals(0.0, shoppingCart.getDiscount());
    }

    @Test
    void testSetAmountValidWithDiscount() {
        shoppingCart.setAmount(3);
        assertEquals(3, shoppingCart.getAmount());
        assertEquals(0.10, shoppingCart.getDiscount());
    }

    @Test
    void testSetAmountInvalidStockInsufficient() {
        assertThrows(InvalidAttributeException.class, () -> shoppingCart.setAmount(11));
    }

    @Test
    void testCalculateTotalNoDiscount() {
        assertEquals(1200.0, shoppingCart.calculateTotal());

        shoppingCart.setAmount(2);
        assertEquals(2400.0, shoppingCart.calculateTotal());
    }

    @Test
    void testCalculateTotalWithDiscount() {
        shoppingCart.setAmount(3);
        assertEquals(3240.0, shoppingCart.calculateTotal(), 0.01);
    }
}