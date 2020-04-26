package br.com.golfinvest.data.model;

public class Pessoa {
    private int id;
    private String nome;
    private String email;
    private String cpf;
    private String codigoAssessor;
    private int codigoBanco;
    private String agencia;
    private String conta;

    public Pessoa(int id, String nome, String email, String cpf, String codigoAssessor, int codigoBanco, String agencia, String conta){
        this.id=id;
        this.nome=nome;
        this.email=email;
        this.cpf=cpf;
        this.codigoAssessor=codigoAssessor;
        this.codigoBanco=codigoBanco;
        this.agencia=agencia;
        this.conta=conta;
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
}
