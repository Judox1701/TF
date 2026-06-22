package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import java.time.LocalDate;
import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public interface PedidoRepository {
    Pedido salva(Pedido pedido);
    Pedido atualiza(Pedido pedido);
    Pedido recuperaPedido(long id);
    List<Pedido> recuperaTodos();
    List<Pedido> recuperaPedidosEntreguesEntreDatas(LocalDate inicio, LocalDate fim);
}
