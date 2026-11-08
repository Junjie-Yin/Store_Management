package upm.app.data.repositories.sql;

import upm.app.data.models.Article;
import upm.app.data.repositories.ArticleRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArticleRepositorySql implements ArticleRepository {

    private final Connection connection;

    public ArticleRepositorySql() {
        this.connection = MySqlTables.getConnection();
    }

    @Override
    public Article create(Article article) {
        String sql = "INSERT INTO article (barcode, name, price, provider, stock, registration_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, article.getBarcode());
            stmt.setString(2, article.getName());
            stmt.setDouble(3, article.getPrice());
            stmt.setString(4, article.getProvider());
            stmt.setInt(5, article.getStock());
            stmt.setDate(6, Date.valueOf(article.getRegistrationDate()));

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                article.setId(rs.getInt(1));
            }
            return article;
        } catch (SQLException e) {
            throw new RuntimeException("Error creando artículo", e);
        }
    }

    @Override
    public Optional<Article> read(Integer id) {
        String sql = "SELECT * FROM article WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToArticle(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando artículo por id", e);
        }
        return Optional.empty();
    }

    private Article mapResultSetToArticle(ResultSet rs) throws SQLException {
        return new Article(
                rs.getInt("id"),
                rs.getString("barcode"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getDate("registration_date").toLocalDate(),
                rs.getString("provider"),
                rs.getInt("stock")
        );
    }

    @Override
    public Optional<Article> findByBarcode(String barcode) {
        String sql = "SELECT * FROM article WHERE barcode = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, barcode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapResultSetToArticle(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando artículo por barcode", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM article");
            while (rs.next()) {
                articles.add(mapResultSetToArticle(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando artículos", e);
        }
        return articles;
    }

    @Override
    public void delete(Integer id) {
        try (PreparedStatement deleteCartStmt = connection.prepareStatement("DELETE FROM shopping_cart WHERE article_id = ?");
             PreparedStatement deleteArticleStmt = connection.prepareStatement("DELETE FROM article WHERE id = ?")) {

            deleteCartStmt.setInt(1, id);
            deleteCartStmt.executeUpdate();

            deleteArticleStmt.setInt(1, id);
            deleteArticleStmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando artículo", e);
        }
    }

    @Override
    public Article update(Integer id, Article article) {
        String sql = """
                UPDATE article
                SET barcode = ?, name = ?, price = ?, provider = ?, stock = ?, registration_date = ?
                WHERE id = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, article.getBarcode());
            stmt.setString(2, article.getName());
            stmt.setDouble(3, article.getPrice());
            stmt.setString(4, article.getProvider());
            stmt.setInt(5, article.getStock());
            stmt.setDate(6, Date.valueOf(article.getRegistrationDate()));
            stmt.setInt(7, id);

            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new upm.app.data.repositories.exception.EntityNotFoundException("Article with ID " + id + " not found for update.");
            }

            article.setId(id);
            return article;

        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando artículo", e);
        }
    }

    @Override
    public List<Article> findByProvider(String provider) {
        List<Article> list = new ArrayList<>();
        String sql = "SELECT * FROM article WHERE provider = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, provider);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToArticle(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando por proveedor", e);
        }

        return list;
    }

    @Override
    public int size() {
        String sql = "SELECT COUNT(*) FROM article";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException("Error contando artículos", e);
        }

        return 0;
    }

    @Override
    public void clear() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM article");

        } catch (SQLException e) {
            throw new RuntimeException("Error limpiando tabla article", e);
        }
    }
}