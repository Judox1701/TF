package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.ItemPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClientesRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.EstoqueRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

@Service
public class PedidoService {
    private final ClientesRepository clientesRepository;
    private final ProdutosRepository produtosRepository;
    private final EstoqueRepository estoqueRepository;
    private final PedidoRepository pedidoRepository;
    private final ServicoDesconto servicoDesconto;
    private final ServicoImposto servicoImposto;

    @Autowired
    public PedidoService(ClientesRepository clientesRepository,
                         ProdutosRepository produtosRepository,
                         EstoqueRepository estoqueRepository,
                         PedidoRepository pedidoRepository,
                         ServicoDesconto servicoDesconto,
                         ServicoImposto servicoImposto) {
        this.clientesRepository = clientesRepository;
        this.produtosRepository = produtosRepository;
        this.estoqueRepository = estoqueRepository;
        this.pedidoRepository = pedidoRepository;
        this.servicoDesconto = servicoDesconto;
        this.servicoImposto = servicoImposto;
    }

    public Pedido submeterPedidoParaAprovacao(String clienteCpf, List<ItemPedidoRequest> itensPedido) {
        Cliente cliente = clientesRepository.recuperaClientePorCpf(clienteCpf);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado: " + clienteCpf);
        }

        List<ItemPedido> itens = new ArrayList<>();
        for (ItemPedidoRequest itemRequest : itensPedido) {
            Produto produto = produtosRepository.recuperaProdutoPorid(itemRequest.getProdutoId());
            if (produto == null) {
                throw new IllegalArgumentException("Produto não encontrado: " + itemRequest.getProdutoId());
            }
            if (itemRequest.getQuantidade() <= 0) {
                throw new IllegalArgumentException("Quantidade inválida para produto " + produto.getId());
            }
            itens.add(new ItemPedido(produto, itemRequest.getQuantidade()));
        }

        Map<Long, Integer> quantidadePorIngrediente = new HashMap<>();
        for (ItemPedido item : itens) {
            for (var ingrediente : item.getItem().getReceita().getIngredientes()) {
                quantidadePorIngrediente.merge(ingrediente.getId(), item.getQuantidade(), Integer::sum);
            }
        }

        boolean estoqueOK = true;
        for (var entry : quantidadePorIngrediente.entrySet()) {
            long ingredienteId = entry.getKey();
            int quantidadeNecessaria = entry.getValue();
            ItemEstoque itemEstoque = estoqueRepository.recuperaItemEstoquePorIngrediente(ingredienteId);
            if (itemEstoque == null || itemEstoque.getQuantidade() < quantidadeNecessaria) {
                estoqueOK = false;
                break;
            }
        }

        double valor = itens.stream()
                .mapToDouble(item -> item.getItem().getPreco() * item.getQuantidade())
                .sum();
        double impostos = servicoImposto.calcular(valor);
        double desconto = servicoDesconto.calcular(valor);
        double valorCobrado = valor + impostos - desconto;

        Pedido.Status status = estoqueOK ? Pedido.Status.APROVADO : Pedido.Status.NEGADO;

        if (estoqueOK) {
            for (var entry : quantidadePorIngrediente.entrySet()) {
                long ingredienteId = entry.getKey();
                int quantidadeNecessaria = entry.getValue();
                ItemEstoque itemEstoque = estoqueRepository.recuperaItemEstoquePorIngrediente(ingredienteId);
                estoqueRepository.atualizaQuantidade(ingredienteId, itemEstoque.getQuantidade() - quantidadeNecessaria);
            }
        }

        Pedido pedido = new Pedido(0L, cliente, LocalDateTime.now(), itens, status, valor, impostos, desconto, valorCobrado);
        return pedidoRepository.salva(pedido);
    }

    public Pedido recuperaPedido(long id) {
        return pedidoRepository.recuperaPedido(id);
    }

    public boolean cancelarPedido(long id) {
        Pedido pedido = pedidoRepository.recuperaPedido(id);
        if (pedido == null) {
            return false;
        }

        if (pedido.getStatus() == Pedido.Status.ENTREGUE ||
                pedido.getStatus() == Pedido.Status.TRANSPORTE ||
                pedido.getStatus() == Pedido.Status.CANCELADO) {
            return false;
        }

        if (pedido.getStatus() == Pedido.Status.APROVADO ||
                pedido.getStatus() == Pedido.Status.PAGO ||
                pedido.getStatus() == Pedido.Status.AGUARDANDO ||
                pedido.getStatus() == Pedido.Status.PREPARACAO ||
                pedido.getStatus() == Pedido.Status.PRONTO) {
            for (ItemPedido item : pedido.getItens()) {
                for (var ingrediente : item.getItem().getReceita().getIngredientes()) {
                    ItemEstoque itemEstoque = estoqueRepository.recuperaItemEstoquePorIngrediente(ingrediente.getId());
                    if (itemEstoque != null) {
                        estoqueRepository.atualizaQuantidade(ingrediente.getId(),
                                itemEstoque.getQuantidade() + item.getQuantidade());
                    }
                }
            }
        }

        pedido.setStatus(Pedido.Status.CANCELADO);
        pedidoRepository.atualiza(pedido);
        return true;
    }

    public List<Pedido> recuperaTodosPedidos() {
        return pedidoRepository.recuperaTodos();
    }

    public List<Pedido> listarPedidosEntreguesEntreDatas(LocalDate inicio, LocalDate fim) {
        return pedidoRepository.recuperaPedidosEntreguesEntreDatas(inicio, fim);
    }
}
