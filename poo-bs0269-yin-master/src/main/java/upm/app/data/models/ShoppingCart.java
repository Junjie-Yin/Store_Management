package upm.app.data.models;

import upm.app.data.models.exceptions.InvalidAttributeException;
import upm.app.data.models.user.User;

import java.time.LocalDateTime;


public class ShoppingCart extends Entity {
    private LocalDateTime creationDate;
    private User user;

    private Article article;
    private Integer amount;
    private Double discount;

    public ShoppingCart(Integer id, LocalDateTime creationDate, User user, Article article, Integer amount) {
        super(id);
        this.creationDate = LocalDateTime.now();
        setUser(user);
        setArticle(article);
        setAmount(amount);
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDateTime(LocalDateTime creationDate) {
        if (creationDate == null) {
            throw new IllegalArgumentException("La fecha y hora de creación no pueden ser nulas.");
        }
        this.creationDate = creationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (user == null) {
            throw new InvalidAttributeException("El usuario asociado al carrito no puede ser nulo.");
        }
        this.user = user;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        if (article == null) {
            throw new InvalidAttributeException("El articulo no puede ser nulo.");
        }
        this.article = article;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        if (amount == null || amount <= 0) {
            throw new InvalidAttributeException("La cantidad debe ser un número positivo.");
        }
        if (this.article != null && this.article.getStock() < amount) {
            throw new InvalidAttributeException("Stock insuficiente. Disponible: " + article.getStock());
        }
        this.amount = amount;
        this.setDiscount();
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount() {
        if (amount != null && amount >= 3) {
            this.discount = 0.1;
        } else {
            this.discount = 0.0;
        }
    }

    public double calculateTotal() {
        if (article == null || amount == null) {
            return 0.0;
        }
        double subtotal = article.getPrice() * amount;
        return subtotal * (1 - discount);
    }

    @Override
    public String toString() {
        return String.format(
                "Carrito de %s (%s)%n" +
                        "\t Fecha: %s%n" +
                        "\t Artículo: %s (x%d)%n" +
                        "\t Precio Unitario: %.2f€%n" +
                        "\t Descuento aplicado: %.0f%%%n" +
                        "\t Total: %.2f€",
                user.getName(), user.getRole(),
                creationDate,
                article.getName(), amount,
                article.getPrice(),
                discount * 100,
                calculateTotal()
        );
    }
}
