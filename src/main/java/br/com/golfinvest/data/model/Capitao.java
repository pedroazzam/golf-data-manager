package br.com.golfinvest.data.model;

import java.math.BigDecimal;

public class Capitao {

    private int id;
    private int idPessoal;
    private int idProduto;
    private String nomePessoal;
    private String nomeProduto;
    private BigDecimal comissao;

    public Capitao(){

    }

    public Capitao(int id, int idProduto, String nomeProduto, int idPessoal, String nomePessoal, BigDecimal comissao){
        this.id=id;
        this.idPessoal=idPessoal;
        this.idProduto=idProduto;
        this.nomePessoal=nomePessoal;
        this.nomeProduto=nomeProduto;
        this.comissao=comissao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPessoal() {
        return idPessoal;
    }

    public void setIdPessoal(int idPessoal) {
        this.idPessoal = idPessoal;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomePessoal() {
        return nomePessoal;
    }

    public void setNomePessoal(String nomePessoal) {
        this.nomePessoal = nomePessoal;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public BigDecimal getComissao() {
        return comissao;
    }

    public void setComissao(BigDecimal comissao) {
        this.comissao = comissao;
    }


}
