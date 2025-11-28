package controller;

import com.google.gson.Gson;
import model.Usuario;
import service.UsuarioService;
import spark.Request;
import spark.Response;
import java.util.Map;

public class UsuarioController {
    private UsuarioService usuarioService = new UsuarioService();
    private Gson gson = new Gson();

    public Object cadastrar(Request req, Response res) {
        Usuario u = new Usuario();
        u.setEmail(req.queryParams("email"));
        u.setNickname(req.queryParams("nickname"));
        u.setSerieUsuario(req.queryParams("serie_usuario"));
        u.setSenha(req.queryParams("senha"));

        boolean ok = usuarioService.cadastrar(u);
        if (ok) res.redirect("/login.html");
        else res.redirect("/cadastro.html?erro=1");
        return null;
    }

    public Object login(Request req, Response res) {
        res.type("application/json");
        Map<String, String> dados = gson.fromJson(req.body(), Map.class);
        return usuarioService.login(dados.get("email"), dados.get("senha"))
                .map(u -> gson.toJson(Map.of(
                        "message", "Login realizado com sucesso!",
                        "user", u.getNickname(),
                        "idUsuario", u.getIdUsuario()
                )))
                .orElseGet(() -> gson.toJson(Map.of("message", "Email ou senha incorretos!")));
    }
}