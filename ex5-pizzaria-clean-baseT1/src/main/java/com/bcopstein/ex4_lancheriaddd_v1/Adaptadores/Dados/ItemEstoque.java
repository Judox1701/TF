package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import jakarta.persistence.*;

@Entity
@Table(name = "itensEstoque")
public class ItemEstoqueEntity {
    
    @Id
    @Column(name = "ingrediente_id")
    private Long ingredienteId;
    
    private int quantidade;

    protected ItemEstoqueEntity() {}

    public ItemEstoqueEntity(Long ingredienteId, int quantidade) {
        this.ingredienteId = ingredienteId;
        this.quantidade = quantidade;
    }

    public Long getIngredienteId() { return ingredienteId; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
}