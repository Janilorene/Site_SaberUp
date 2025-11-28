package controller;

import com.google.gson.Gson;
import model.PartidaDTO;
import service.PartidaService;
import spark.Request;
import spark.Response;
import java.util.Map;

public class PartidaController {
    private PartidaService partidaService = new PartidaService();
    private Gson gson = new Gson();

    public Object registrar(Request req, Response res) {
        res.type("application/json");
        try {
            PartidaDTO partida = gson.fromJson(req.body(), PartidaDTO.class);

            if (partida == null || partida.getIdUsuario() == 0 || partida.getIdJogo() == 0) {
                res.status(400);
                return gson.toJson(Map.of("error", "Campos obrigatórios ausentes"));
            }

            boolean sucesso = partidaService.registrarPartida(
                    partida.getIdUsuario(), partida.getIdJogo(), partida.getPontuacao()
            );

            if (sucesso) {
                res.status(201);
                return gson.toJson(Map.of("message", "Partida registrada com sucesso!"));
            } else {
                res.status(500);
                return gson.toJson(Map.of("error", "Erro ao registrar partida no banco"));
            }
        } catch (Exception e) {
            res.status(400);
            return gson.toJson(Map.of("error", "Erro ao processar dados", "details", e.getMessage()));
        }
    }

    public Object listarUltimas(Request req, Response res) {
        res.type("application/json");
        int idUsuario = Integer.parseInt(req.params(":id"));
        return gson.toJson(partidaService.ultimasPartidas(idUsuario));
    }
}