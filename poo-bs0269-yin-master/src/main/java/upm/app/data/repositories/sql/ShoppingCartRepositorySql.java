package upm.app.data.repositories.sql;

import upm.app.data.models.Article;
import upm.app.data.models.ShoppingCart;
import upm.app.data.models.user.User;
import upm.app.data.repositories.ArticleRepository;
import upm.app.data.repositories.ShoppingCartRepository;
import upm.app.data.repositories.UserRepository;
import upm.app.data.repositories.exception.EntityNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShoppingCartRepositorySql implements ShoppingCartRepository {

    private final Connection connection;

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public ShoppingCartRepositorySql(UserRepository userRepository, ArticleRepository articleRepository) {
        this.connection = MySqlTables.getConnection();
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public ShoppingCart create(ShoppingCart shoppingCart) {
        String sql = "INSERT INTO shopping_cart (user_id, article_id, amount, discount, creation_date) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, shoppingCart.getUser().getId());
            stmt.setInt(2, shoppingCart.getArticle().getId());
            stmt.setInt(3, shoppingCart.getAmount());
            stmt.setDouble(4, shoppingCart.getDiscount());
            stmt.setTimestamp(5, Timestamp.valueOf(shoppingCart.getCreationDate()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                shoppingCart.setId(rs.getInt(1));
            }
            return shoppingCart;
        } catch (SQLException e) {
            throw new RuntimeException("Error creando carrito SQL", e);
        }
    }

    @Override
    public Optional<ShoppingCart> read(Integer id) {
        String sql = "SELECT * FROM shopping_cart WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToShoppingCart(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error leyendo carrito", e);
        }
        return Optional.empty();
    }

    private ShoppingCart mapResultSetToShoppingCart(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        Integer userId = rs.getInt("user_id");
        Integer articleId = rs.getInt("article_id");
        Integer amount = rs.getInt("amount");
        LocalDateTime date = rs.getTimestamp("creation_date").toLocalDateTime();

        User user = userRepository.read(userId)
                .orElseThrow(() -> new RuntimeException("Integridad de datos: Usuario " + userId + " no encontrado"));
        Article article = articleRepository.read(articleId)
                .orElseThrow(() -> new RuntimeException("Integridad de datos: Artículo " + articleId + " no encontrado"));

        return new ShoppingCart(id, date, user, article, amount);
    }

    @Override
    public List<ShoppingCart> findByUserId(Integer userId) {
        List<ShoppingCart> carts = new ArrayList<>();
        String sql = "SELECT id FROM shopping_cart WHERE user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                this.read(rs.getInt("id")).ifPresent(carts::add);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando carritos por usuario", e);
        }
        return carts;
    }

    @Override
    public List<ShoppingCart> findAll() {
        List<ShoppingCart> carts = new ArrayList<>();
        String sql = "SELECT id, user_id, article_id, amount, discount, creation_date FROM shopping_cart";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                carts.add(mapResultSetToShoppingCart(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando carritos", e);
        }
        return carts;
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM shopping_cart WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ShoppingCart update(Integer id, ShoppingCart entity) {
        String sql = "UPDATE shopping_cart SET user_id=?, article_id=?, amount=?, discount=?, creation_date=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, entity.getUser().getId());
            stmt.setInt(2, entity.getArticle().getId());
            stmt.setInt(3, entity.getAmount());
            stmt.setDouble(4, entity.getDiscount());
            stmt.setTimestamp(5, Timestamp.valueOf(entity.getCreationDate()));
            stmt.setInt(6, id);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new EntityNotFoundException("Shopping Cart with ID " + id + " not found for update.");
            }
            entity.setId(id);
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando carrito", e);
        }
    }

    @Override
    public int size() {
        String sql = "SELECT COUNT(*) FROM shopping_cart";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo tamaño", e);
        }
    }

    @Override
    public void clear() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM shopping_cart");
        } catch (SQLException e) {
            throw new RuntimeException("Error limpiando tabla shopping_cart", e);
        }
    }
}

