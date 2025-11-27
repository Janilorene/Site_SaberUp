package model;

public class Jogo {
    private int idJogo;
    private String serieJogo;
    private String titulo;
    private String materia;
    private String topico; 

    public int getIdJogo() {
        return idJogo;
    }
    public void setIdJogo(int idJogo) {
        this.idJogo = idJogo;
    }

    public String getSerieJogo() {
        return serieJogo;
    }
    public void setSerieJogo(String serieJogo) {
        this.serieJogo = serieJogo;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMateria() {
        return materia;
    }
    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getTopico() {
        return topico;
    }
    public void setTopico(String topico) {
        this.topico = topico;
    }
}
