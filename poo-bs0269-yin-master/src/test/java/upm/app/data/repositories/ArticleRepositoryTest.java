package upm.app.data.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upm.app.DependencyInjector;
import upm.app.data.models.Article;
import upm.app.data.repositories.exception.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ArticleRepositoryTest {

    private final ArticleRepository articleRepository = DependencyInjector.getInstance().getArticleRepository();

    @BeforeEach
    void setUp() {
        DependencyInjector.getInstance().getSeeder().seed();
    }

    @Test
    void testCreateArticle() {
        int initialSize = articleRepository.size();
        Article newArticle = new Article(null, "NEWBARCODE", "Keyboard", 50.0, LocalDate.now(), "Logitech", 20);
        Article createdArticle = articleRepository.create(newArticle);

        assertNotNull(createdArticle.getId());
        assertEquals(initialSize + 1, articleRepository.size());
        assertEquals(newArticle.getBarcode(), createdArticle.getBarcode());
        assertTrue(articleRepository.read(createdArticle.getId()).isPresent());
    }

    @Test
    void testUpdateArticle() {
        Article existingArticle = articleRepository.findByBarcode("1234567890123").orElseThrow();
        Integer articleId = existingArticle.getId();
        existingArticle.setName("Ratón Inalámbrico");
        existingArticle.setPrice(25.0);

        Article updatedArticle = articleRepository.update(articleId, existingArticle);

        assertEquals("Ratón Inalámbrico", updatedArticle.getName());
        assertEquals(25.0, updatedArticle.getPrice());
        assertEquals("Ratón Inalámbrico", articleRepository.read(articleId).orElseThrow().getName());
    }

    @Test
    void testUpdateArticleNotFound() {
        Article nonExistentArticle = new Article(999, "NONEXISTENT", "Test", 10.0, LocalDate.now(), "Test", 1);
        assertThrows(EntityNotFoundException.class, () -> articleRepository.update(999, nonExistentArticle));
    }

    @Test
    void testReadArticle() {
        Integer articleId = articleRepository.findByBarcode("1234567890123").orElseThrow().getId();
        Optional<Article> article = articleRepository.read(articleId);
        assertTrue(article.isPresent());
        assertEquals("Ratón Bluetooth", article.get().getName());
    }

    @Test
    void testReadArticleNotFound() {
        Optional<Article> article = articleRepository.read(999);
        assertFalse(article.isPresent());
    }

    @Test
    void testDeleteArticle() {
        int initialSize = articleRepository.size();
        Integer articleId = articleRepository.findByBarcode("1234567890123").orElseThrow().getId();
        articleRepository.delete(articleId);

        assertEquals(initialSize - 1, articleRepository.size());
        assertFalse(articleRepository.read(articleId).isPresent());
    }

    @Test
    void testFindAllArticles() {
        List<Article> articles = articleRepository.findAll();
        assertFalse(articles.isEmpty());
        assertEquals(6, articles.size());
    }

    @Test
    void testFindByBarcode() {
        Optional<Article> article = articleRepository.findByBarcode("8473289473294");
        assertTrue(article.isPresent());
        assertEquals("Zapatillas Nike", article.get().getName());
    }

    @Test
    void testFindByBarcodeNotFound() {
        Optional<Article> article = articleRepository.findByBarcode("INVALIDBARCODE");
        assertFalse(article.isPresent());
    }

    @Test
    void testFindByProvider() {
        List<Article> logitechArticles = articleRepository.findByProvider("LogiTech");
        assertEquals(1, logitechArticles.size());
        assertEquals("Ratón Bluetooth", logitechArticles.getFirst().getName());

        List<Article> nikeArticles = articleRepository.findByProvider("Nike");
        assertEquals(1, nikeArticles.size());
        assertEquals("Zapatillas Nike", nikeArticles.getFirst().getName());

        List<Article> nonExistentProviderArticles = articleRepository.findByProvider("NonExistent");
        assertTrue(nonExistentProviderArticles.isEmpty());
    }
}