package dao;

import model.Partida;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


public class PartidaDAO extends DAO {

    public PartidaDAO() {
        super(); 
    }

    public boolean inserir(Partida partida) {
        String sql = "INSERT INTO partida (id_usuario, id_jogo, pontuacao) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conexao.prepareStatement(sql)) {  
            ps.setInt(1, partida.getIdUsuario());
            ps.setInt(2, partida.getIdJogo());
            ps.setInt(3, partida.getPontuacao());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void salvar(Partida partida) throws SQLException {
        String sql = "INSERT INTO partida (id_usuario, id_jogo, pontuacao, resolucao_texto, resolucao_feedback) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, partida.getIdUsuario());
            stmt.setInt(2, partida.getIdJogo());
            stmt.setInt(3, partida.getPontuacao());
            stmt.setString(4, partida.getResolucaoTexto());
            stmt.setString(5, partida.getResolucaoFeedback());

            stmt.executeUpdate();
        }
    }

    public List<Partida> listarUltimasPartidasComJogo(int idUsuario) {
        String sql = """
            SELECT p.id_partida, p.pontuacao, p.data_hora,
                   j.id_jogo, j.titulo, j.materia, j.topico, j.serie_jogo
            FROM partida p
            INNER JOIN jogo j ON p.id_jogo = j.id_jogo
            WHERE p.id_usuario = ?
            ORDER BY p.id_partida DESC
            LIMIT 10
        """;

        List<Partida> lista = new ArrayList<>();
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Partida p = new Partida();
                p.setIdPartida(rs.getInt("id_partida"));
                p.setIdUsuario(idUsuario);
                p.setIdJogo(rs.getInt("id_jogo"));
                p.setPontuacao(rs.getInt("pontuacao"));
                p.setTitulo(rs.getString("titulo"));
                p.setMateria(rs.getString("materia"));
                p.setTopico(rs.getString("topico"));
                p.setSerieJogo(rs.getString("serie_jogo"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public void salvarComEmbedding(Partida partida) throws SQLException {
        String sql = "INSERT INTO partida (id_usuario, id_jogo, pontuacao, resolucao_texto, resolucao_feedback, embedding) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, partida.getIdUsuario());
            stmt.setInt(2, partida.getIdJogo());
            stmt.setInt(3, partida.getPontuacao());
            stmt.setString(4, partida.getResolucaoTexto());
            stmt.setString(5, partida.getResolucaoFeedback());

            // Convertendo double[] para array do PostgreSQL
            if (partida.getEmbedding() != null) {
                Array sqlArray = conexao.createArrayOf("float8", Arrays.stream(partida.getEmbedding())
                                                                       .boxed()
                                                                       .toArray(Double[]::new));
                stmt.setArray(6, sqlArray);
            } else {
                stmt.setArray(6, null);
            }

            stmt.executeUpdate();
        }
    }



}
