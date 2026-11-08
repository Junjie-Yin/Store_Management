package upm.app.data.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upm.app.data.models.exceptions.InvalidAttributeException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArticleTest {

    private Article article;

    @BeforeEach
    void setUp() {
        article = new Article(1, "BAR123", "Laptop", 1200.0, LocalDate.now(), "HP", 10);
    }

    @Test
    void testArticleConstructorAndGetters() {
        assertEquals(1, article.getId());
        assertEquals("BAR123", article.getBarcode());
        assertEquals("Laptop", article.getName());
        assertEquals(1200.0, article.getPrice());
        assertEquals(LocalDate.now(), article.getRegistrationDate());
        assertEquals("HP", article.getProvider());
        assertEquals(10, article.getStock());
    }

    @Test
    void testSetBarcodeValid() {
        article.setBarcode("NEWBAR456");
        assertEquals("NEWBAR456", article.getBarcode());
    }

    @Test
    void testSetBarcodeInvalidNull() {
        assertThrows(InvalidAttributeException.class, () -> article.setBarcode(null));
    }

    @Test
    void testSetBarcodeInvalidEmpty() {
        assertThrows(InvalidAttributeException.class, () -> article.setBarcode(""));
    }

    @Test
    void testSetNameValid() {
        article.setName("New Name");
        assertEquals("New Name", article.getName());
    }

    @Test
    void testSetNameInvalidNull() {
        assertThrows(InvalidAttributeException.class, () -> article.setName(null));
    }

    @Test
    void testSetNameInvalidEmpty() {
        assertThrows(InvalidAttributeException.class, () -> article.setName(""));
    }

    @Test
    void testSetPriceValid() {
        article.setPrice(1500.0);
        assertEquals(1500.0, article.getPrice());
    }

    @Test
    void testSetPriceInvalidNull() {
        assertThrows(InvalidAttributeException.class, () -> article.setPrice(null));
    }

    @Test
    void testSetPriceInvalidNegative() {
        assertThrows(InvalidAttributeException.class, () -> article.setPrice(-10.0));
    }

    @Test
    void testSetRegistrationDateValid() {
        LocalDate newDate = LocalDate.of(2023, 1, 1);
        article.setRegistrationDate(newDate);
        assertEquals(newDate, article.getRegistrationDate());
    }

    @Test
    void testSetRegistrationDateInvalidNull() {
        assertThrows(InvalidAttributeException.class, () -> article.setRegistrationDate(null));
    }

    @Test
    void testSetProviderValid() {
        article.setProvider("Dell");
        assertEquals("Dell", article.getProvider());
    }

    @Test
    void testSetStockValid() {
        article.setStock(20);
        assertEquals(20, article.getStock());
    }

    @Test
    void testSetStockInvalidNegative() {
        assertThrows(InvalidAttributeException.class, () -> article.setStock(-5));
    }
}
