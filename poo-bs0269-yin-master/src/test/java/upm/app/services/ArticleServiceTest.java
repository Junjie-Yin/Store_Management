package upm.app.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upm.app.DependencyInjector;
import upm.app.data.models.Article;
import upm.app.services.exceptions.DuplicateException;
import upm.app.services.exceptions.NotFoundException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ArticleServiceTest {

    private final ArticleService articleService = DependencyInjector.getInstance().getArticleService();
    private final DependencyInjector dependencyInjector = DependencyInjector.getInstance();

    @BeforeEach
    void setUp() {
        dependencyInjector.getSeeder().seed();
    }

    @Test
    void testCreateArticle() {
        int initialSize = dependencyInjector.getArticleRepository().size();
        Article newArticle = new Article(null, "NEWBARCODE", "Keyboard", 50.0, LocalDate.now(), "Logitech", 20);
        Article createdArticle = articleService.create(newArticle);

        assertNotNull(createdArticle.getId());
        assertEquals("NEWBARCODE", createdArticle.getBarcode());
        assertEquals(initialSize + 1, dependencyInjector.getArticleRepository().size());
    }

    @Test
    void testCreateArticleDuplicateBarcode() {
        Article existingArticle = dependencyInjector.getArticleRepository().findByBarcode("1234567890123").orElseThrow();
        Article newArticleWithDuplicateBarcode = new Article(null, existingArticle.getBarcode(), "Another Mouse", 30.0, LocalDate.now(), "Microsoft", 10);
        assertThrows(DuplicateException.class, () -> articleService.create(newArticleWithDuplicateBarcode));
    }

    @Test
    void testDeleteArticle() {
        int initialSize = dependencyInjector.getArticleRepository().size();
        Article mouse = dependencyInjector.getArticleRepository().findByBarcode("1234567890123").orElseThrow();

        articleService.delete(mouse.getId());

        assertEquals(initialSize - 1, dependencyInjector.getArticleRepository().size());
        assertFalse(dependencyInjector.getArticleRepository().read(mouse.getId()).isPresent());
    }

    @Test
    void testDeleteArticleNotFound() {
        assertThrows(NotFoundException.class, () -> articleService.delete(999));
    }
}