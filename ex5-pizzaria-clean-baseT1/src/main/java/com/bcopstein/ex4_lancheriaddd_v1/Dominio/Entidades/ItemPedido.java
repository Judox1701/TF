package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

@Entity
@Table(name = "itens_pedido")
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto item;
    private int quantidade;

    protected ItemPedido() {}

    public ItemPedido(Produto item, int quantidade) {
        this.item = item;
        this.quantidade = quantidade;
    }

    public Produto getItem() { return item; }
    public int getQuantidade() { return quantidade; }
}
