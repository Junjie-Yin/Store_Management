package upm.app.data.repositories;

import upm.app.data.models.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends GenericRepository<Article> {
    Optional<Article> findByBarcode(String barcode);

    List<Article> findByProvider(String provider);
}
