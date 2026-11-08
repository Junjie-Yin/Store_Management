package upm.app.data.repositories.map;

import upm.app.data.models.Article;
import upm.app.data.repositories.ArticleRepository;

import java.util.List;
import java.util.Optional;

public class ArticleRepositoryMap extends GenericRepositoryMap<Article> implements ArticleRepository {
    public ArticleRepositoryMap() {
        super();
    }

    public Optional<Article> findByBarcode(String barcode) {
        return map.values().stream()
                .filter(article -> article.getBarcode().equalsIgnoreCase(barcode))
                .findFirst();
    }

    public List<Article> findByProvider(String provider) {
        return map.values().stream()
                .filter(article -> article.getProvider().equalsIgnoreCase(provider))
                .toList();
    }
}
