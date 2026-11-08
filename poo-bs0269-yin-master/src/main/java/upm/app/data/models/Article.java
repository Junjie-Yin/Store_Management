package upm.app.data.models;

import upm.app.data.models.exceptions.InvalidAttributeException;

import java.time.LocalDate;

public class Article extends Entity {
    private String barcode;
    private String name;
    private Double price;
    private LocalDate registrationDate;
    private String provider;
    private Integer stock;

    public Article(Integer id, String barcode, String name, Double price, LocalDate registrationDate, String provider, Integer stock) {
        super(id);
        setBarcode(barcode);
        setName(name);
        setPrice(price);
        setRegistrationDate(registrationDate);
        setProvider(provider);
        setStock(stock);
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        if (barcode == null || barcode.isBlank()) {
            throw new InvalidAttributeException("La barra de código del producto es inválido: " + barcode);
        }
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidAttributeException("El nombre del producto no puede ser vacío");
        }
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        if (price == null || price < 0) {
            throw new InvalidAttributeException("El precio no puede ser nulo o negativo");
        }
        this.price = price;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        if (registrationDate == null) {
            throw new InvalidAttributeException("La fecha de registro no puede ser nula");
        }
        this.registrationDate = registrationDate;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        if (stock < 0) {
            throw new InvalidAttributeException("El stock no puede ser negativo");
        }
        this.stock = stock;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "id=" + getId() +
                ", barcode='" + barcode + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }

}
