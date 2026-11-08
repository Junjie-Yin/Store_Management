package upm.app.presentation.cli.commands;

import upm.app.data.models.Article;
import upm.app.presentation.cli.Command;
import upm.app.presentation.view.View;
import upm.app.services.ArticleService;

import java.time.LocalDate;
import java.util.List;

public class CreateArticle implements Command {
    private final View view;
    private final ArticleService articleService;

    public CreateArticle(View view, ArticleService articleService) {
        this.view = view;
        this.articleService = articleService;
    }

    @Override
    public String name() {
        return "create-article";
    }

    @Override
    public List<String> params() {
        return List.of("barcode", "name", "price", "provider", "stock");
    }

    @Override
    public String helpMessage() {
        return "Se crea un articulo";
    }

    @Override
    public void execute(String[] params) {
        String barcode = params[0].trim();
        String name = params[1].trim();
        Double price = Double.parseDouble(params[2].trim());
        String provider = params[3].trim();
        Integer stock = Integer.parseInt(params[4].trim());

        Article newArticle = new Article(null, barcode, name, price, LocalDate.now(), provider, stock);
        Article createdArticle = this.articleService.create(newArticle);
        this.view.show("Artículo creado: " + createdArticle.toString());
    }
}