package upm.app.presentation.cli.commands;

import upm.app.presentation.cli.Command;
import upm.app.presentation.view.View;
import upm.app.services.ArticleService;

import java.util.List;

public class DeleteArticle implements Command {
    private final View view;
    private final ArticleService articleService;

    public DeleteArticle(View view, ArticleService articleService) {
        this.view = view;
        this.articleService = articleService;
    }

    @Override
    public String name() {
        return "delete-article";
    }

    @Override
    public List<String> params() {
        return List.of("id");
    }

    @Override
    public String helpMessage() {
        return "Elimina un artículo por su ID";
    }

    @Override
    public void execute(String[] params) {
        Integer id = Integer.parseInt(params[0].trim());
        articleService.delete(id);
        view.show("Artículo con ID " + id + " eliminado correctamente.");
    }
}