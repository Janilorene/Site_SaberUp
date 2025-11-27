package service;

import dao.PartidaDAO;
import model.Partida;
import java.util.List;

public class PartidaService {

    private PartidaDAO dao;

    public PartidaService() {
        this.dao = new PartidaDAO();
    }

    public boolean registrarPartida(int idUsuario, int idJogo, int pontuacao) {
        Partida p = new Partida();
        p.setIdUsuario(idUsuario);
        p.setIdJogo(idJogo);
        p.setPontuacao(pontuacao);
        return dao.inserir(p);
    }

    public boolean registrarPartidaIA(Partida partida) {
        try {

            // Salva no banco
            PartidaDAO dao = new PartidaDAO();
            dao.salvarComEmbedding(partida);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }




    // Retorna as últimas partidas do usuário com dados do jogo
    public List<Partida> ultimasPartidas(int idUsuario) {
        return dao.listarUltimasPartidasComJogo(idUsuario);
    }
}
