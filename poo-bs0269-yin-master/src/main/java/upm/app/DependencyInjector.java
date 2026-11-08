package upm.app;

import upm.app.data.repositories.ArticleRepository;
import upm.app.data.repositories.Seeder;
import upm.app.data.repositories.ShoppingCartRepository;
import upm.app.data.repositories.UserRepository;
import upm.app.data.repositories.sql.ArticleRepositorySql;
import upm.app.data.repositories.sql.ShoppingCartRepositorySql;
import upm.app.data.repositories.sql.UserRepositorySql;
import upm.app.presentation.ErrorHandler;
import upm.app.presentation.cli.CommandLineInterface;
import upm.app.presentation.cli.commands.*;
import upm.app.presentation.view.View;
import upm.app.services.ArticleService;
import upm.app.services.ShoppingCartService;
import upm.app.services.UserService;

public class DependencyInjector {
    private static DependencyInjector instance;

    private final ErrorHandler errorHandler;
    private final View view;
    private final CommandLineInterface commandLineInterface;
    private final UserService userService;
    private final ArticleService articleService;
    private final ShoppingCartService shoppingCartService;

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    private final Seeder seeder;

    DependencyInjector() {
        this.userRepository = new UserRepositorySql();
        this.articleRepository = new ArticleRepositorySql();
        this.shoppingCartRepository = new ShoppingCartRepositorySql(this.userRepository, this.articleRepository);

        this.seeder = new Seeder(this.userRepository, this.articleRepository, this.shoppingCartRepository);
        this.seeder.seed();

        this.userService = new UserService(this.userRepository);
        this.articleService = new ArticleService(this.articleRepository);
        this.shoppingCartService = new ShoppingCartService(this.shoppingCartRepository);

        this.view = new View();
        this.commandLineInterface = new CommandLineInterface(this.view);
        this.commandLineInterface.add(new CreateUser(this.view, this.userService));
        this.commandLineInterface.add(new ReadUser(this.view, this.userService));
        this.commandLineInterface.add(new DeleteUser(this.view, this.userService));
        this.commandLineInterface.add(new CreateArticle(this.view, this.articleService));
        this.commandLineInterface.add(new DeleteArticle(this.view, this.articleService));
        this.commandLineInterface.add(new ListOrders(this.view, this.shoppingCartService));
        this.commandLineInterface.add(new ListOrdersByUser(this.view, this.shoppingCartService));
        this.commandLineInterface.add(new ListArticlesByUser(this.view, this.shoppingCartService));

        this.errorHandler = new ErrorHandler();
    }

    public static DependencyInjector getInstance() {
        if (instance == null) {
            instance = new DependencyInjector();
        }
        return instance;
    }

    public void run() {
        this.errorHandler.handlesErrors(this.commandLineInterface, this.view);
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public View getView() {
        return view;
    }

    public CommandLineInterface getCommandLineInterface() {
        return commandLineInterface;
    }

    public UserService getUserService() {
        return userService;
    }

    public ArticleService getArticleService() {
        return articleService;
    }

    public ShoppingCartService getShoppingCartService() {
        return shoppingCartService;
    }

    public Seeder getSeeder() {
        return seeder;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public ArticleRepository getArticleRepository() {
        return articleRepository;
    }

    public ShoppingCartRepository getShoppingCartRepository() {
        return shoppingCartRepository;
    }
}
