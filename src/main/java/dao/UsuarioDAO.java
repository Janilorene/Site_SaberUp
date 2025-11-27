package dao;

import model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends DAO {

    public UsuarioDAO() {
        super(); 
    }

    public void add(Usuario u) {
        String sql = "INSERT INTO usuario (email, nickname, serie_usuario, senha, assinante) VALUES (?, ?, ?, ?, ?)";
        // Nota: Não colocamos 'conexao' dentro do try(), pois não queremos fechá-la ao fim do método
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, u.getEmail());
            stmt.setString(2, u.getNickname());
            stmt.setString(3, u.getSerieUsuario());
            stmt.setString(4, u.getSenha()); 
            stmt.setBoolean(5, u.isAssinante());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Usuario u) {
        String sql = "UPDATE usuario SET email=?, nickname=?, serie_usuario=?, senha=?, assinante=? WHERE id_usuario=?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, u.getEmail());
            stmt.setString(2, u.getNickname());
            stmt.setString(3, u.getSerieUsuario());
            stmt.setString(4, u.getSenha());
            stmt.setBoolean(5, u.isAssinante());
            stmt.setInt(6, u.getIdUsuario());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remove(int id) {
        String sql = "DELETE FROM usuario WHERE id_usuario=?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Usuario> listar() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario ORDER BY id_usuario";
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("email"),
                        rs.getString("nickname"),
                        rs.getString("serie_usuario"),
                        rs.getString("senha"),
                        rs.getBoolean("assinante")
                );
                usuarios.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email=?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("email"),
                        rs.getString("nickname"),
                        rs.getString("serie_usuario"),
                        rs.getString("senha"),
                        rs.getBoolean("assinante")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}