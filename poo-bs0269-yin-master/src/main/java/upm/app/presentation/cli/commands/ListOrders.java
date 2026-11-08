package upm.app.presentation.cli.commands;

import upm.app.presentation.cli.Command;
import upm.app.presentation.view.View;
import upm.app.services.ShoppingCartService;

import java.util.List;

public class ListOrders implements Command {
    private final View view;
    private final ShoppingCartService shoppingCartService;

    public ListOrders(View view, ShoppingCartService shoppingCartService) {
        this.view = view;
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public String name() {
        return "list-orders";
    }

    @Override
    public List<String> params() {
        return List.of();
    }

    @Override
    public String helpMessage() {
        return "Lista todas las compras";
    }

    @Override
    public void execute(String[] params) {
        if (params.length != 0) {
            throw new IllegalArgumentException("Este comando no acepta parámetros.");
        }
        this.shoppingCartService.findAll().forEach(cart -> view.showItem(cart.toString()));
    }
}