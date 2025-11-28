package controller;

import com.google.gson.Gson;
import model.Jogo;
import service.JogoService;
import spark.Request;
import spark.Response;

public class JogoController {
    private JogoService jogoService = new JogoService();
    private Gson gson = new Gson();

    public Object listar(Request req, Response res) {
        res.type("application/json");
        return gson.toJson(jogoService.listar(req.queryParams("topico")));
    }

    public Object listarPorTopico(Request req, Response res) {
        res.type("application/json");
        return gson.toJson(jogoService.listarPorTopico(req.params(":topico")));
    }

    public Object adicionar(Request req, Response res) {
        Jogo j = gson.fromJson(req.body(), Jogo.class);
        jogoService.addAndGetId(j.getSerieJogo(), j.getTitulo(), j.getMateria(), j.getTopico());
        return "Jogo cadastrado!";
    }

    public Object atualizar(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        Jogo j = gson.fromJson(req.body(), Jogo.class);
        jogoService.update(id, j.getSerieJogo(), j.getTitulo(), j.getMateria(), j.getTopico());
        return "Jogo atualizado!";
    }

    public Object deletar(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        jogoService.remove(id);
        return "Jogo removido!";
    }
}