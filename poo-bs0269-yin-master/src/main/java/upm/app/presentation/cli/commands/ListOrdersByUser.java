package upm.app.presentation.cli.commands;

import upm.app.presentation.cli.Command;
import upm.app.presentation.view.View;
import upm.app.services.ShoppingCartService;

import java.util.List;

public class ListOrdersByUser implements Command {
    private final View view;
    private final ShoppingCartService shoppingCartService;

    public ListOrdersByUser(View view, ShoppingCartService shoppingCartService) {
        this.view = view;
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public String name() {
        return "list-orders-by-user";
    }

    @Override
    public List<String> params() {
        return List.of("userId");
    }

    @Override
    public String helpMessage() {
        return "Lista la compra de un usuario específico por su ID";
    }

    @Override
    public void execute(String[] params) {
        Integer userId = Integer.parseInt(params[0].trim());
        this.shoppingCartService.findByUserId(userId).forEach(cart -> view.showItem(cart.toString()));
    }
}