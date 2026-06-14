package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

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

    public java.util.List<Pedido> recuperaTodosPedidos() {
        return pedidoRepository.recuperaTodos();
    }
}
