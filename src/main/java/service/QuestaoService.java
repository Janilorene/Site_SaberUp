package service;

import dao.QuestaoDAO;
import dao.RespostaDAO;
import model.Questao;
import model.Resposta;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.*;

public class QuestaoService {

    private QuestaoDAO questaoDAO = new QuestaoDAO();
    private RespostaDAO respostaDAO = new RespostaDAO();
    private Gson gson = new Gson();

    // POST /questao
    public String add(Request req, Response res) {
        res.type("application/json");
        Questao q = gson.fromJson(req.body(), Questao.class);
        boolean status = questaoDAO.insert(q);
        res.status(status ? 201 : 400);
        return gson.toJson(status ? "Questão inserida com sucesso!" : "Erro ao inserir questão!");
    }

    // PUT /questao/:id
    public String update(Request req, Response res) {
        res.type("application/json");
        int id = Integer.parseInt(req.params(":id"));
        Questao q = gson.fromJson(req.body(), Questao.class);
        boolean status = questaoDAO.update(id, q);
        res.status(status ? 200 : 400);
        return gson.toJson(status ? "Questão atualizada com sucesso!" : "Erro ao atualizar questão!");
    }

    // DELETE /questao/:id
    public String delete(Request req, Response res) {
        res.type("application/json");
        int id = Integer.parseInt(req.params(":id"));
        boolean status = questaoDAO.delete(id);
        res.status(status ? 200 : 400);
        return gson.toJson(status ? "Questão removida com sucesso!" : "Erro ao remover questão!");
    }

    // GET /questoes/:idJogo
    public String listarPorJogo(Request req, Response res) {
        res.type("application/json");
        int idJogo = Integer.parseInt(req.params(":idJogo"));

        List<Questao> questoes = questaoDAO.listarPorJogo(idJogo);
        List<Map<String,Object>> questoesComRespostas = new ArrayList<>();

        for (Questao q : questoes) {
            List<Resposta> respostas = respostaDAO.listarPorQuestao(q.getId_questao());
            Map<String,Object> questaoJson = new HashMap<>();
            questaoJson.put("id_questao", q.getId_questao());
            questaoJson.put("enunciado", q.getEnunciado());
            questaoJson.put("id_jogo", q.getId_jogo());
            
            List<Map<String,Object>> respFormatadas = new ArrayList<>();
            if (respostas != null && !respostas.isEmpty()) {
                Resposta r = respostas.get(0); // assumindo 1 conjunto por questão
                Map<String,Object> rJson = new HashMap<>();
                rJson.put("id_resposta", r.getId_resposta());
                rJson.put("opcao1", r.getOpcao1());
                rJson.put("opcao2", r.getOpcao2());
                rJson.put("opcao3", r.getOpcao3());
                rJson.put("opcao4", r.getOpcao4());
                rJson.put("opcao5", r.getOpcao5());
                rJson.put("correta", r.getCorreta());
                respFormatadas.add(rJson);
            }
            questaoJson.put("respostas", respFormatadas);
            questoesComRespostas.add(questaoJson);
        }

        return gson.toJson(questoesComRespostas);
    }
}
