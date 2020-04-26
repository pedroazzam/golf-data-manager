package br.com.golfinvest.data.model;

public class Produto {
    private int id;
    private String nome;
    private String tagProd;
    private String tagN1;
    private String tagN2;
    private String tagN3;

    public Produto(int id, String nome, String tagProd, String tagN1, String tagN2, String tagN3){
        this.id=id;
        this.nome=nome;
        this.tagProd=tagProd;
        this.tagN1=tagN1;
        this.tagN2=tagN2;
        this.tagN3=tagN3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTagProd() {
        return tagProd;
    }

    public void setTagProd(String tagProd) {
        this.tagProd = tagProd;
    }

    public String getTagN1() {
        return tagN1;
    }

    public void setTagN1(String tagN1) {
        this.tagN1 = tagN1;
    }

    public String getTagN2() {
        return tagN2;
    }

    public void setTagN2(String tagN2) {
        this.tagN2 = tagN2;
    }

    public String getTagN3() {
        return tagN3;
    }

    public void setTagN3(String tagN3) {
        this.tagN3 = tagN3;
    }
}
