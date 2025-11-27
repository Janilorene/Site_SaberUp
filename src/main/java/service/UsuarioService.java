package service;

import model.Usuario;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Optional;

public class UsuarioService {

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

    public boolean cadastrar(Usuario u) {
        String hashed = BCrypt.hashpw(u.getSenha(), BCrypt.gensalt());
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO usuario (email, nickname, serie_usuario, senha, assinante) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, u.getEmail());
            stmt.setString(2, u.getNickname());
            stmt.setString(3, u.getSerieUsuario());
            stmt.setString(4, hashed);
            stmt.setBoolean(5, u.isAssinante());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<Usuario> login(String email, String senha) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM usuario WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hashed = rs.getString("senha");
                if (BCrypt.checkpw(senha, hashed)) {
                    Usuario u = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("email"),
                        rs.getString("nickname"),
                        rs.getString("serie_usuario"),
                        hashed,
                        rs.getBoolean("assinante")
                    );
                    return Optional.of(u);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}