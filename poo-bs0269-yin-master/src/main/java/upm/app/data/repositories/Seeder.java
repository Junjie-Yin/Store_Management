package upm.app.data.repositories;

import upm.app.data.models.Article;
import upm.app.data.models.ShoppingCart;
import upm.app.data.models.user.Role;
import upm.app.data.models.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Seeder {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    public Seeder(UserRepository userRepository, ArticleRepository articleRepository, ShoppingCartRepository shoppingCartRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public void seed() {
        this.shoppingCartRepository.clear();
        this.userRepository.clear();
        this.articleRepository.clear();

        User[] users = {
                User.builder("ana@example.com", "Ana", "García", "12345678A", Role.PROFESOR)
                        .build(),
                User.builder("juan@example.com", "Juan", "Pérez", "23456789B", Role.ALUMNO)
                        .build(),
                User.builder("maria@example.com", "María", "López", "34567890C", Role.ALUMNO)
                        .build(),
                User.builder("carlos@example.com", "Carlos", "Ruiz", "45678901D", Role.PROFESOR)
                        .secondSurname("Gómez")
                        .build()
        };
        for (User user : users) {
            userRepository.create(user);
        }

        Article[] articles = {
                new Article(null, "1234567890123", "Ratón Bluetooth", 20.00, LocalDate.now(), "LogiTech", 5),
                new Article(null, "8473289473294", "Zapatillas Nike", 125.50, LocalDate.now(), "Nike", 5),
                new Article(null, "5682583984329", "Don Quijote de la Mancha", 18.95, LocalDate.now(), "Editorial Octaedro", 5),
                new Article(null, "9482473729938", "Botella Personalizada", 9.95, LocalDate.now(), "El Corte Inglés", 5),
                new Article(null, "SH29380198392", "Boligrafo Gel", 2.5, LocalDate.now(), "Pilot", 5),
                new Article(null, "9999999999999", "Camiseta Adidas", 30.00, LocalDate.now(), "Adidas", 10),
        };
        for (Article article : articles) {
            articleRepository.create(article);
        }

        List<User> userList = userRepository.findAll();
        List<Article> articleList = articleRepository.findAll();

        User ana = userList.stream().filter(u -> u.getEmail().equals("ana@example.com")).findFirst().orElseThrow();
        Article mouse = articleList.stream()
                .filter(a -> a.getName().equals("Ratón Bluetooth"))
                .findFirst().orElseThrow();
        ShoppingCart cart1 = new ShoppingCart(null, LocalDateTime.now(), ana, mouse, 2);
        shoppingCartRepository.create(cart1);

        User juan = userList.stream().filter(u -> u.getEmail().equals("juan@example.com")).findFirst().orElseThrow();
        Article bottle = articleList.stream()
                .filter(a -> a.getName().equals("Botella Personalizada"))
                .findFirst().orElseThrow();
        ShoppingCart cart2 = new ShoppingCart(null, LocalDateTime.now(), juan, bottle, 3);
        shoppingCartRepository.create(cart2);

        User maria = userList.stream().filter(u -> u.getEmail().equals("maria@example.com")).findFirst().orElseThrow();
        Article book = articleList.stream()
                .filter(a -> a.getName().equals("Don Quijote de la Mancha"))
                .findFirst().orElseThrow();
        ShoppingCart cart3 = new ShoppingCart(null, LocalDateTime.now(), maria, book, 1);
        shoppingCartRepository.create(cart3);

        User carlos = userList.stream().filter(u -> u.getEmail().equals("carlos@example.com")).findFirst().orElseThrow();
        Article shoes = articleList.stream()
                .filter(a -> a.getName().equals("Zapatillas Nike"))
                .findFirst().orElseThrow();
        ShoppingCart cart4 = new ShoppingCart(null, LocalDateTime.now(), carlos, shoes, 5);
        shoppingCartRepository.create(cart4);
    }
}
