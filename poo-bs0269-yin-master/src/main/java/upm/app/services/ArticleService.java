package upm.app.services;

import upm.app.data.models.Article;
import upm.app.data.repositories.ArticleRepository;
import upm.app.services.exceptions.DuplicateException;
import upm.app.services.exceptions.NotFoundException;

public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article create(Article article) {
        this.articleRepository.findByBarcode(article.getBarcode())
                .ifPresent(a -> {
                    throw new DuplicateException("El código de barras ya existe, y debiera ser único: " + article.getBarcode());
                });
        return articleRepository.create(article);
    }

    public void delete(Integer id) {
        this.articleRepository.read(id)
                .orElseThrow(() -> new NotFoundException("No existe un artículo con id: " + id + " para eliminar."));
        articleRepository.delete(id);
    }
}
