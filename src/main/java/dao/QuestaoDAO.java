package dao;

import model.Questao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestaoDAO extends DAO {

    public QuestaoDAO() {
        super(); 
    }

    @Override
    public void finalize() {
        close();
    }

    public boolean insert(Questao q) {
        boolean status = false;
        try {
            String sql = "INSERT INTO questao (enunciado, id_jogo) VALUES (?, ?)";
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setString(1, q.getEnunciado());
            ps.setInt(2, q.getId_jogo());
            ps.executeUpdate();
            ps.close();
            status = true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir questão: " + e.getMessage());
        }
        return status;
    }

    public List<Questao> listarPorJogo(int id_jogo) {
        List<Questao> questoes = new ArrayList<>();
        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("SELECT * FROM questao WHERE id_jogo = " + id_jogo);
            while (rs.next()) {
                Questao q = new Questao(
                    rs.getInt("id_questao"),
                    rs.getString("enunciado"),
                    rs.getInt("id_jogo")
                );
                questoes.add(q);
            }
            st.close();
        } catch (Exception e) {
            System.err.println("Erro ao listar questões: " + e.getMessage());
        }
        return questoes;
    }
    
    public boolean update(int id, Questao q) {
        String sql = "UPDATE questao SET enunciado = ? WHERE id_questao = ?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, q.getEnunciado());
            ps.setInt(2, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar questão: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM questao WHERE id_questao = ?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar questão: " + e.getMessage());
            return false;
        }
    }
}
