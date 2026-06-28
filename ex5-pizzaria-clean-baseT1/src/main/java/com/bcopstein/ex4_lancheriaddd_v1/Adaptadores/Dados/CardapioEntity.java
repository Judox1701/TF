package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import jakarta.persistence.*;

@Entity
@Table(name = "cardapios")
public class CardapioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;

    protected CardapioEntity() {}

    public CardapioEntity(Long id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
}