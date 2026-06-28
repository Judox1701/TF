package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

@Entity
@Table(name = "ingredientes")
public class Ingrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String descricao;

    protected Ingrediente(){}

    public Ingrediente(long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public long getId() { return id; }
    public String getDescricao() { return descricao; }
}
