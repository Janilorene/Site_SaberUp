package service;

import dao.RespostaDAO;
import model.Resposta;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class RespostaService {

    private RespostaDAO respostaDAO = new RespostaDAO();
    private Gson gson = new Gson();

    public Object add(Request req, Response res) {
        res.type("application/json");
        Resposta r = gson.fromJson(req.body(), Resposta.class);
        boolean status = respostaDAO.save(r); 
        res.status(status ? 201 : 400);
        return gson.toJson(status ? "Resposta salva com sucesso!" : "Erro ao salvar resposta!");
    }

    public Object update(Request req, Response res) {
        res.type("application/json");
        int id = Integer.parseInt(req.params(":id"));
        Resposta r = gson.fromJson(req.body(), Resposta.class);
        r.setId_resposta(id);
        boolean status = respostaDAO.update(r);
        res.status(status ? 200 : 400);
        return gson.toJson(status ? "Resposta atualizada com sucesso!" : "Erro ao atualizar resposta!");
    }

    public Object delete(Request req, Response res) {
        res.type("application/json");
        int id = Integer.parseInt(req.params(":id"));
        boolean status = respostaDAO.delete(id);
        res.status(status ? 200 : 400);
        return gson.toJson(status ? "Resposta removida com sucesso!" : "Erro ao remover resposta!");
    }
}
