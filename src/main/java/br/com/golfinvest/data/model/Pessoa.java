package br.com.golfinvest.data.model;

import java.math.BigDecimal;

public class Pessoa {
    private int id;
    private String nome;
    private String email;
    private String cpf;
    private String codigoAssessor;
    private BigDecimal repassePrevAssessor;
    private BigDecimal repasseSegAssessor;
    private int codigoBanco;
    private String agencia;
    private String conta;
    private String dv;
    private String codigoXerife;
    private BigDecimal repassePrevXerife;

    public Pessoa(int id, String nome, String email, String cpf, String codigoAssessor, BigDecimal repassePrevAssessor, BigDecimal repasseSegAssessor, int codigoBanco, String agencia, String conta, String dv, String codigoXerife, BigDecimal repassePrevXerife){
        this.id=id;
        this.nome=nome;
        this.email=email;
        this.cpf=cpf;
        this.codigoAssessor=codigoAssessor;
        this.repassePrevAssessor=repassePrevAssessor;
        this.repasseSegAssessor=repasseSegAssessor;
        this.codigoBanco=codigoBanco;
        this.agencia=agencia;
        this.conta=conta;
        this.dv=dv;
        this.codigoXerife=codigoXerife;
        this.repassePrevXerife=repassePrevXerife;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCodigoAssessor() {
        return codigoAssessor;
    }

    public void setCodigoAssessor(String codigoAssessor) {
        this.codigoAssessor = codigoAssessor;
    }

    public BigDecimal getRepassePrevAssessor() {
        return repassePrevAssessor;
    }

    public void setRepassePrevAssessor(BigDecimal repassePrevAssessor) {
        this.repassePrevAssessor = repassePrevAssessor;
    }

    public BigDecimal getRepasseSegAssessor() {
        return repasseSegAssessor;
    }

    public void setRepasseSegAssessor(BigDecimal repasseSegAssessor) {
        this.repasseSegAssessor = repasseSegAssessor;
    }

    public int getCodigoBanco() {
        return codigoBanco;
    }

    public void setCodigoBanco(int codigoBanco) {
        this.codigoBanco = codigoBanco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public String getCodigoXerife() {
        return codigoXerife;
    }

    public void setCodigoXerife(String codigoXerife) {
        this.codigoXerife = codigoXerife;
    }

    public BigDecimal getRepassePrevXerife() {
        return repassePrevXerife;
    }

    public void setRepassePrevXerife(BigDecimal repassePrevXerife) {
        this.repassePrevXerife = repassePrevXerife;
    }
}
