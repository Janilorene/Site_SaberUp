package model;

public class Partida {
    private int idPartida;
    private int idUsuario;
    private int idJogo;
    private int pontuacao;

    // Campos do jogo
    private String titulo;
    private String materia;
    private String topico;
    private String serieJogo;

    // Campos de resolução IA
    private String resolucaoTexto;
    private String resolucaoFeedback;
    private double[] embedding; // <-- novo campo

    public Partida() {}

    public Partida(int idUsuario, int idJogo, int pontuacao) {
        this.idUsuario = idUsuario;
        this.idJogo = idJogo;
        this.pontuacao = pontuacao;
    }

    public int getIdPartida() { return idPartida; }
    public void setIdPartida(int idPartida) { this.idPartida = idPartida; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public int getIdJogo() { return idJogo; }
    public void setIdJogo(int idJogo) { this.idJogo = idJogo; }

    public int getPontuacao() { return pontuacao; }
    public void setPontuacao(int pontuacao) { this.pontuacao = pontuacao; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }

    public String getTopico() { return topico; }
    public void setTopico(String topico) { this.topico = topico; }

    public String getSerieJogo() { return serieJogo; }
    public void setSerieJogo(String serieJogo) { this.serieJogo = serieJogo; }

    public String getResolucaoTexto() { return resolucaoTexto; }
    public void setResolucaoTexto(String resolucaoTexto) { this.resolucaoTexto = resolucaoTexto; }

    public String getResolucaoFeedback() { return resolucaoFeedback; }
    public void setResolucaoFeedback(String resolucaoFeedback) { this.resolucaoFeedback = resolucaoFeedback; }

    public double[] getEmbedding() { return embedding; }
    public void setEmbedding(double[] embedding) { this.embedding = embedding; }
}
