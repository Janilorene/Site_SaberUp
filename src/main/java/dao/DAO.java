package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAO {
    protected Connection conexao;

    public DAO() {
        conectar(); 
    }

    protected void conectar() {
        try {
            Class.forName("org.postgresql.Driver");
            
            String url = System.getenv("DB_URL");
            String user = System.getenv("DB_USER");
            String pass = System.getenv("DB_PASS");

            if (url == null || url.isEmpty()) {
                url = "jdbc:postgresql://localhost:5432/saberup";
                user = "ti2cc";
                pass = "ti@cc";
            }

            conexao = DriverManager.getConnection(url, user, pass);
            System.out.println("Conexão OK: " + url); // Log para ajudar a debugar

        } catch (Exception e) {
            System.err.println("Erro ao conectar ao banco de dados!");
            e.printStackTrace();
        }
    }

    protected void close() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}