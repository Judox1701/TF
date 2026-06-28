package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    private String cpf;
    private String nome;
    private String celular;
    private String usuario;
    private String senha;
    private String endereco;
    private String email;

    protected Cliente(){}

    public Cliente(String cpf, String nome, String celular, String usuario, String senha, String endereco, String email) {
        this.cpf = cpf;
        this.nome = nome;
        this.celular = celular;
        this.usuario = usuario;
        this.senha = senha;
        this.endereco = endereco;
        this.email = email;
    }

    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public String getCelular() { return celular; }
    public String getUsuario() { return usuario; }
    public String getSenha() { return senha; }
    public String getEndereco() { return endereco; }
    public String getEmail() { return email; }
}
