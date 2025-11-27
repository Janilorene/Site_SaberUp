package service;

import model.Jogo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JogoService {

    private Connection getConnection() throws SQLException {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");

        if (url == null || url.isEmpty()) {
            url = "jdbc:postgresql://localhost:5432/saberup";
            user = "ti2cc";
            pass = "ti@cc";
        }
        return DriverManager.getConnection(url, user, pass);
    }

    public int addAndGetId(String serieJogo, String titulo, String materia, String topico) {
        int idJogo = 0;
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO Jogo (serie_jogo, titulo, materia, topico) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, serieJogo);
            stmt.setString(2, titulo);
            stmt.setString(3, materia);
            stmt.setString(4, topico);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                idJogo = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idJogo;
    }


    public void update(int id, String serieJogo, String titulo, String materia, String topico) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE Jogo SET serie_jogo=?, titulo=?, materia=?, topico=? WHERE id_jogo=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, serieJogo);
            stmt.setString(2, titulo);
            stmt.setString(3, materia);
            stmt.setString(4, topico);
            stmt.setInt(5, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remove(int id) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM Jogo WHERE id_jogo=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Jogo> listar(String topico) {
        if (topico != null && !topico.isEmpty()) {
            return listarPorTopico(topico);
        } else {
            return listarTodos();
        }
    }

    private List<Jogo> listarTodos() {
        List<Jogo> jogos = new ArrayList<>();
        try (Connection conn = getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Jogo ORDER BY id_jogo");
            while (rs.next()) {
                Jogo j = new Jogo();
                j.setIdJogo(rs.getInt("id_jogo"));
                j.setSerieJogo(rs.getString("serie_jogo"));
                j.setTitulo(rs.getString("titulo"));
                j.setMateria(rs.getString("materia"));
                j.setTopico(rs.getString("topico"));
                jogos.add(j);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jogos;
    }

    public List<Jogo> listarPorTopico(String topico) {
        List<Jogo> jogos = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM Jogo WHERE topico = ? ORDER BY id_jogo";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, topico);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Jogo j = new Jogo();
                j.setIdJogo(rs.getInt("id_jogo"));
                j.setSerieJogo(rs.getString("serie_jogo"));
                j.setTitulo(rs.getString("titulo"));
                j.setMateria(rs.getString("materia"));
                j.setTopico(rs.getString("topico"));
                jogos.add(j);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jogos;
    }
}