package dao;

import model.Resposta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RespostaDAO extends DAO {

    public RespostaDAO() {
        super();
    }

    public boolean save(Resposta r) {
        String sqlUpdate = "UPDATE resposta SET opcao1=?, opcao2=?, opcao3=?, opcao4=?, opcao5=?, correta=? WHERE id_questao=?";
        try (PreparedStatement ps = conexao.prepareStatement(sqlUpdate)) {
            ps.setString(1, r.getOpcao1());
            ps.setString(2, r.getOpcao2());
            ps.setString(3, r.getOpcao3());
            ps.setString(4, r.getOpcao4());
            ps.setString(5, r.getOpcao5());
            ps.setInt(6, r.getCorreta());
            ps.setInt(7, r.getId_questao());
            int rows = ps.executeUpdate();
            if (rows > 0) return true; 
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar resposta: " + e.getMessage());
        }

        String sqlInsert = "INSERT INTO resposta (id_questao, opcao1, opcao2, opcao3, opcao4, opcao5, correta) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexao.prepareStatement(sqlInsert)) {
            ps.setInt(1, r.getId_questao());
            ps.setString(2, r.getOpcao1());
            ps.setString(3, r.getOpcao2());
            ps.setString(4, r.getOpcao3());
            ps.setString(5, r.getOpcao4());
            ps.setString(6, r.getOpcao5());
            ps.setInt(7, r.getCorreta());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir resposta: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Resposta r) {
        String sql = "UPDATE resposta SET opcao1=?, opcao2=?, opcao3=?, opcao4=?, opcao5=?, correta=? WHERE id_resposta=?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, r.getOpcao1());
            ps.setString(2, r.getOpcao2());
            ps.setString(3, r.getOpcao3());
            ps.setString(4, r.getOpcao4());
            ps.setString(5, r.getOpcao5());
            ps.setInt(6, r.getCorreta());
            ps.setInt(7, r.getId_resposta());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar resposta: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM resposta WHERE id_resposta=?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar resposta: " + e.getMessage());
            return false;
        }
    }

    public List<Resposta> listarPorQuestao(int id_questao) {
        List<Resposta> respostas = new ArrayList<>();
        String sql = "SELECT * FROM resposta WHERE id_questao=?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id_questao);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Resposta r = new Resposta(
                    rs.getInt("id_resposta"),
                    rs.getInt("id_questao"),
                    rs.getString("opcao1"),
                    rs.getString("opcao2"),
                    rs.getString("opcao3"),
                    rs.getString("opcao4"),
                    rs.getString("opcao5"),
                    rs.getInt("correta")
                );
                respostas.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar respostas: " + e.getMessage());
        }
        return respostas;
    }
}
