package model;

import java.util.List;

public class Questao {
    private int id_questao;
    private String enunciado;
    private int id_jogo;
    private List<Resposta> respostas;

    public Questao() {}

    public Questao(int id_questao, String enunciado, int id_jogo) {
        this.id_questao = id_questao;
        this.enunciado = enunciado;
        this.id_jogo = id_jogo;
    }

    public int getId_questao() {
        return id_questao;
    }

    public void setId_questao(int id_questao) {
        this.id_questao = id_questao;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public int getId_jogo() {
        return id_jogo;
    }

    public void setId_jogo(int id_jogo) {
        this.id_jogo = id_jogo;
    }

    public List<Resposta> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<Resposta> respostas) {
        this.respostas = respostas;
    }
}
