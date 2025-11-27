package app;

import static spark.Spark.*;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import java.util.Base64;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.servlet.MultipartConfigElement;


import service.UsuarioService;
import service.JogoService;
import service.QuestaoService;
import service.RespostaService;
import service.PartidaService;
import service.AzureOCRService;
import service.GeminiService;

import model.Jogo;
import model.Usuario;
import model.Partida;
import model.PartidaDTO;

import java.util.Map;
import java.util.List;

public class Aplicacao {
    public static void main(String[] args) {
    	ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            port(Integer.parseInt(processBuilder.environment().get("PORT")));
        } else {
            port(4568);
        }
        staticFiles.location("/public");

        // Serviços
        UsuarioService usuarioService = new UsuarioService();
        JogoService jogoService = new JogoService();
        QuestaoService questaoService = new QuestaoService();
        RespostaService respostaService = new RespostaService();
        PartidaService partidaService = new PartidaService();
        AzureOCRService ocrService = new AzureOCRService();
        GeminiService geminiService = new GeminiService();

        Gson gson = new Gson();

        // ================== USUÁRIO ==================
        post("/usuario", (req, res) -> {
            String email = req.queryParams("email");
            String nickname = req.queryParams("nickname");
            String serie = req.queryParams("serie_usuario");
            String senha = req.queryParams("senha");

            Usuario u = new Usuario();
            u.setEmail(email);
            u.setNickname(nickname);
            u.setSerieUsuario(serie);
            u.setSenha(senha);

            boolean ok = usuarioService.cadastrar(u);

            if (ok) {
                res.redirect("/login.html");
            } else {
                res.redirect("/cadastro.html?erro=1");
            }
            return null;
        });

        post("/login", (req, res) -> {
            res.type("application/json");
            Map<String, String> dados = gson.fromJson(req.body(), Map.class);
            String email = dados.get("email");
            String senha = dados.get("senha");

            return usuarioService.login(email, senha)
                    .map(u -> gson.toJson(Map.of(
                            "message", "Login realizado com sucesso!",
                            "user", u.getNickname(),
                            "idUsuario", u.getIdUsuario()
                    )))
                    .orElseGet(() -> gson.toJson(Map.of("message", "Email ou senha incorretos!")));
        });

        get("/", (req, res) -> {
            res.redirect("/materias.html");
            return null;
        });

        get("/CRUD_jogos.html", (req, res) -> {
            res.type("text/html");
            InputStream is = Aplicacao.class.getResourceAsStream("/public/CRUD_jogos.html");
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        });

        // ================== CRUD JOGOS ==================
        get("/jogos", (req, res) -> {
            res.type("application/json");
            return gson.toJson(jogoService.listar(req.queryParams("topico")));
        });

        post("/jogo", (req, res) -> {
            Jogo j = gson.fromJson(req.body(), Jogo.class);
            jogoService.addAndGetId(j.getSerieJogo(), j.getTitulo(), j.getMateria(), j.getTopico());
            return "Jogo cadastrado!";
        });

        put("/jogo/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Jogo j = gson.fromJson(req.body(), Jogo.class);
            jogoService.update(id, j.getSerieJogo(), j.getTitulo(), j.getMateria(), j.getTopico());
            return "Jogo atualizado!";
        });

        delete("/jogo/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            jogoService.remove(id);
            return "Jogo removido!";
        });

        get("/jogos/topico/:topico", (req, res) -> {
            res.type("application/json");
            return gson.toJson(jogoService.listarPorTopico(req.params(":topico")));
        });

        // ================== QUESTÕES ==================
        post("/questao", (req, res) -> questaoService.add(req, res));
        put("/questao/:id", (req, res) -> questaoService.update(req, res));
        delete("/questao/:id", (req, res) -> questaoService.delete(req, res));
        get("/questoes/:idJogo", (req, res) -> questaoService.listarPorJogo(req, res));

        // ================== RESPOSTAS ==================
        post("/resposta", (req, res) -> respostaService.add(req, res));
        put("/resposta/:id", (req, res) -> respostaService.update(req, res));
        delete("/resposta/:id", (req, res) -> respostaService.delete(req, res));

        // ================== PARTIDAS ==================
        post("/partida", (req, res) -> {
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
                return gson.toJson(Map.of("error", "Erro ao processar dados da partida", "details", e.getMessage()));
            }
        });

        get("/partida/usuario/:id", (req, res) -> {
            res.type("application/json");
            int idUsuario = Integer.parseInt(req.params(":id"));
            return gson.toJson(partidaService.ultimasPartidas(idUsuario));
        });
        
     // Rota para Upload e Correção
        post("/api/analise", (req, res) -> {
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));
            res.type("application/json");

            try {
                String enunciado = req.queryParams("enunciado");
                String respAlunoTexto = req.queryParams("respostaAluno");
                String respCorretaTexto = req.queryParams("respostaCorreta"); 

                javax.servlet.http.Part filePart = req.raw().getPart("imagem");
                boolean temImagem = (filePart != null && filePart.getSize() > 0);

                String feedback;
                String textoLido = "";

                if (temImagem) {
                    try (InputStream is = filePart.getInputStream()) {
                        byte[] bytes = is.readAllBytes();
                        textoLido = ocrService.extrairTexto(bytes); 
                        
                        if (textoLido != null) {
                            feedback = geminiService.corrigirQuestao(textoLido, enunciado);
                        } else {
                            feedback = "Não consegui ler a imagem. " + geminiService.explicarErro(enunciado, respAlunoTexto, respCorretaTexto);
                        }
                    }
                } else {
                    feedback = geminiService.explicarErro(enunciado, respAlunoTexto, respCorretaTexto);
                }

                return gson.toJson(Map.of(
                    "temImagem", temImagem,
                    "texto_ocr", textoLido != null ? textoLido : "",
                    "feedback", feedback
                ));

            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return gson.toJson(Map.of("erro", e.getMessage()));
            }
        });

    }
}
