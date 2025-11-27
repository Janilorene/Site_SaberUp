package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Jogo;

public class JogoDAO extends DAO {

    public JogoDAO() {
        super(); 
    }

    public boolean insert(Jogo j) {
        try {
            PreparedStatement st = conexao.prepareStatement(
                "INSERT INTO Jogo(serie_jogo, titulo, materia) VALUES(?,?,?)"
            );
            st.setString(1, j.getSerieJogo());
            st.setString(2, j.getTitulo());
            st.setString(3, j.getMateria());
            st.executeUpdate();
            st.close();
            return true;
        } catch(SQLException e){ 
            e.printStackTrace(); 
            return false; 
        }
    }

    public Jogo get(int id) {
        Jogo j = null;
        try {
            PreparedStatement st = conexao.prepareStatement(
                "SELECT * FROM Jogo WHERE id_jogo=?"
            );
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                j = new Jogo();
                j.setIdJogo(rs.getInt("id_jogo"));
                j.setSerieJogo(rs.getString("serie_jogo"));
                j.setTitulo(rs.getString("titulo"));
                j.setMateria(rs.getString("materia"));
            }
            rs.close();
            st.close();
        } catch(SQLException e){ 
            e.printStackTrace(); 
        }
        return j;
    }

    public List<Jogo> listar() {
        List<Jogo> lista = new ArrayList<>();
        try {
            Statement st = conexao.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Jogo");
            while(rs.next()) {
                Jogo j = new Jogo();
                j.setIdJogo(rs.getInt("id_jogo"));
                j.setSerieJogo(rs.getString("serie_jogo"));
                j.setTitulo(rs.getString("titulo"));
                j.setMateria(rs.getString("materia"));
                lista.add(j);
            }
            rs.close();
            st.close();
        } catch(SQLException e){ 
            e.printStackTrace(); 
        }
        return lista;
    }

    public boolean update(Jogo j) {
        try {
            PreparedStatement st = conexao.prepareStatement(
                "UPDATE Jogo SET serie_jogo=?, titulo=?, materia=? WHERE id_jogo=?"
            );
            st.setString(1, j.getSerieJogo());
            st.setString(2, j.getTitulo());
            st.setString(3, j.getMateria());
            st.setInt(4, j.getIdJogo());
            st.executeUpdate();
            st.close();
            return true;
        } catch(SQLException e){ 
            e.printStackTrace(); 
            return false; 
        }
    }

    public boolean remove(int id) {
        try {
            PreparedStatement st = conexao.prepareStatement(
                "DELETE FROM Jogo WHERE id_jogo=?"
            );
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
            return true;
        } catch(SQLException e){ 
            e.printStackTrace(); 
            return false; 
        }
    }
}
