package model;

public class PartidaDTO {
    private int idUsuario;
    private int idJogo;
    private int pontuacao;

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public int getIdJogo() { return idJogo; }
    public void setIdJogo(int idJogo) { this.idJogo = idJogo; }

    public int getPontuacao() { return pontuacao; }
    public void setPontuacao(int pontuacao) { this.pontuacao = pontuacao; }

    @Override
    public String toString() {
        return "PartidaDTO{idUsuario=" + idUsuario + ", idJogo=" + idJogo + ", pontuacao=" + pontuacao + "}";
    }
}
