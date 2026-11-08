package upm.app.presentation.cli.commands;

import upm.app.data.models.ShoppingCart;
import upm.app.presentation.cli.Command;
import upm.app.presentation.view.View;
import upm.app.services.ShoppingCartService;

import java.util.List;

public class ListArticlesByUser implements Command {
    private final View view;
    private final ShoppingCartService shoppingCartService;

    public ListArticlesByUser(View view, ShoppingCartService shoppingCartService) {
        this.view = view;
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public String name() {
        return "list-articles-by-user";
    }

    @Override
    public List<String> params() {
        return List.of("userId");
    }

    @Override
    public String helpMessage() {
        return "Lista los artículos únicos comprados por un usuario específico";
    }

    @Override
    public void execute(String[] params) {
        Integer userId = Integer.parseInt(params[0].trim());

        List<ShoppingCart> carts = this.shoppingCartService.findByUserId(userId);

        if (carts.isEmpty()) {
            this.view.show("No se encontraron compras para el usuario con ID: " + userId);
            return;
        }

        this.view.show("Artículos comprados por el usuario " + userId + ":");
        carts.stream()
                .map(ShoppingCart::getArticle)
                .distinct()
                .forEach(article -> this.view.showItem(article.toString()));
    }
}
