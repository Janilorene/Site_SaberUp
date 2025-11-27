package model;

public class Usuario {
    private int idUsuario;
    private String email;
    private String nickname;
    private String serieUsuario;
    private String senha;
    private boolean assinante;

    public Usuario() {}

    public Usuario(int idUsuario, String email, String nickname, String serieUsuario, String senha, boolean assinante) {
        this.idUsuario = idUsuario;
        this.email = email;
        this.nickname = nickname;
        this.serieUsuario = serieUsuario;
        this.senha = senha;
        this.assinante = assinante;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getSerieUsuario() { return serieUsuario; }
    public void setSerieUsuario(String serieUsuario) { this.serieUsuario = serieUsuario; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public boolean isAssinante() { return assinante; }
    public void setAssinante(boolean assinante) { this.assinante = assinante; }
}
