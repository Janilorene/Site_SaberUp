package model;

public class Resposta {
    private int id_resposta;
    private int id_questao;
    private String opcao1;
    private String opcao2;
    private String opcao3;
    private String opcao4;
    private String opcao5;
    private int correta; 

    public Resposta() {}

    public Resposta(int id_resposta, int id_questao, String opcao1, String opcao2,
                    String opcao3, String opcao4, String opcao5, int correta) {
        this.id_resposta = id_resposta;
        this.id_questao = id_questao;
        this.opcao1 = opcao1;
        this.opcao2 = opcao2;
        this.opcao3 = opcao3;
        this.opcao4 = opcao4;
        this.opcao5 = opcao5;
        this.correta = correta;
    }

    public int getId_resposta() { return id_resposta; }
    public void setId_resposta(int id_resposta) { this.id_resposta = id_resposta; }

    public int getId_questao() { return id_questao; }
    public void setId_questao(int id_questao) { this.id_questao = id_questao; }

    public String getOpcao1() { return opcao1; }
    public void setOpcao1(String opcao1) { this.opcao1 = opcao1; }

    public String getOpcao2() { return opcao2; }
    public void setOpcao2(String opcao2) { this.opcao2 = opcao2; }

    public String getOpcao3() { return opcao3; }
    public void setOpcao3(String opcao3) { this.opcao3 = opcao3; }

    public String getOpcao4() { return opcao4; }
    public void setOpcao4(String opcao4) { this.opcao4 = opcao4; }

    public String getOpcao5() { return opcao5; }
    public void setOpcao5(String opcao5) { this.opcao5 = opcao5; }

    public int getCorreta() { return correta; }
    public void setCorreta(int correta) { this.correta = correta; }
}
