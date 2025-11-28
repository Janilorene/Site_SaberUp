package app;

import static spark.Spark.*;
import controller.*;
import service.QuestaoService;
import service.RespostaService;

public class Aplicacao {
    public static void main(String[] args) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            port(Integer.parseInt(processBuilder.environment().get("PORT")));
        } else {
            port(4568);
        }

        staticFiles.location("/public");

        
        UsuarioController usuarioController = new UsuarioController();
        JogoController jogoController = new JogoController();
        PartidaController partidaController = new PartidaController();
        AnaliseController analiseController = new AnaliseController();
  
        QuestaoService questaoService = new QuestaoService();
        RespostaService respostaService = new RespostaService();

        // ================== ROTAS ==================

        get("/", (req, res) -> { res.redirect("/materias.html"); return null; });

        // Usuário & Login
        post("/usuario", usuarioController::cadastrar);
        post("/login", usuarioController::login);

        // Jogos (CRUD)
        get("/jogos", jogoController::listar);
        get("/jogos/topico/:topico", jogoController::listarPorTopico);
        post("/jogo", jogoController::adicionar);
        put("/jogo/:id", jogoController::atualizar);
        delete("/jogo/:id", jogoController::deletar);

        // Questões 
        post("/questao", questaoService::add);
        put("/questao/:id", questaoService::update);
        delete("/questao/:id", questaoService::delete);
        get("/questoes/:idJogo", questaoService::listarPorJogo);

        // Respostas 
        post("/resposta", respostaService::add);
        put("/resposta/:id", respostaService::update);
        delete("/resposta/:id", respostaService::delete);

        // Partidas
        post("/partida", partidaController::registrar);
        get("/partida/usuario/:id", partidaController::listarUltimas);

        // Inteligência Artificial (OCR + Gemini)
        post("/api/analise", analiseController::analisar);
    }
}