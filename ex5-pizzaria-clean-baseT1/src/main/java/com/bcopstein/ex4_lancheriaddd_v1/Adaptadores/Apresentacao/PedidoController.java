package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ListarPedidosEntreguesUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.RecuperaListaPedidosUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.RecuperaStatusPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoParaAprovacaoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@RestController
@RequestMapping("/pedido")
public class PedidoController {
    private final RecuperaStatusPedidoUC recuperaStatusPedidoUC;
    private final RecuperaListaPedidosUC recuperaListaPedidosUC;
    private final ListarPedidosEntreguesUC listarPedidosEntreguesUC;

    public PedidoController(SubmeterPedidoParaAprovacaoUC submeterPedidoUC,
                            RecuperaStatusPedidoUC recuperaStatusPedidoUC,
                            RecuperaListaPedidosUC recuperaListaPedidosUC,
                            ListarPedidosEntreguesUC listarPedidosEntreguesUC) {
        this.recuperaStatusPedidoUC = recuperaStatusPedidoUC;
        this.recuperaListaPedidosUC = recuperaListaPedidosUC;
        this.listarPedidosEntreguesUC = listarPedidosEntreguesUC;
    }

    @GetMapping("/status/{id}")
    @CrossOrigin("*")
    public PedidoResponse recuperaStatusPedido(@PathVariable(value = "id") long id) {
        Pedido pedido = recuperaStatusPedidoUC.run(id);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não encontrado: " + id);
        }
        return new PedidoResponse(
                pedido.getId(),
                pedido.getStatus().name(),
                pedido.getDataHoraPagamento(),
                pedido.getValor(),
                pedido.getImpostos(),
                pedido.getDesconto(),
                pedido.getValorCobrado(),
                pedido.getItens().stream()
                        .map(item -> item.getItem().getDescricao() + " x" + item.getQuantidade())
                        .toList());
    }

    @GetMapping("/lista")
    @CrossOrigin("*")
    public List<PedidoResponse> recuperaListaPedidos() {
        List<Pedido> pedidos = recuperaListaPedidosUC.run();
        return pedidos.stream()
                .map(pedido -> new PedidoResponse(
                        pedido.getId(),
                        pedido.getStatus().name(),
                        pedido.getDataHoraPagamento(),
                        pedido.getValor(),
                        pedido.getImpostos(),
                        pedido.getDesconto(),
                        pedido.getValorCobrado(),
                        pedido.getItens().stream()
                                .map(item -> item.getItem().getDescricao() + " x" + item.getQuantidade())
                                .toList()))
                .toList();
    }

    @GetMapping("/entregues")
    @CrossOrigin("*")
    public List<PedidoResponse> listarPedidosEntregues(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<Pedido> pedidos = listarPedidosEntreguesUC.run(inicio, fim);
        return pedidos.stream()
                .map(pedido -> new PedidoResponse(
                        pedido.getId(),
                        pedido.getStatus().name(),
                        pedido.getDataHoraPagamento(),
                        pedido.getValor(),
                        pedido.getImpostos(),
                        pedido.getDesconto(),
                        pedido.getValorCobrado(),
                        pedido.getItens().stream()
                                .map(item -> item.getItem().getDescricao() + " x" + item.getQuantidade())
                                .toList()))
                .toList();
    }
}
