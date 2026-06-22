package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests;

public class CadastrarClienteRequest {
    private String cpf;
    private String nome;
    private String celular;
    private String usuario;
    private String senha;

    public CadastrarClienteRequest() {}

    public CadastrarClienteRequest(String cpf, String nome, String celular, String usuario, String senha) {
        this.cpf = cpf;
        this.nome = nome;
        this.celular = celular;
        this.usuario = usuario;
        this.senha = senha;
    }

    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public String getCelular() { return celular; }
    public String getUsuario() { return usuario; }
    public String getSenha() { return senha; }

    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCelular(String celular) { this.celular = celular; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public void setSenha(String senha) { this.senha = senha; }
}
