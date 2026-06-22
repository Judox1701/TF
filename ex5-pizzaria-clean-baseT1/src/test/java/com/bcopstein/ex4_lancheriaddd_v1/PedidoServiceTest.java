package com.bcopstein.ex4_lancheriaddd_v1;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.ItemPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClientesRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.EstoqueRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ServicoDesconto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ServicoImposto;

@DisplayName("Testes do Sistema de Pedidos")
class PedidoServiceTest {

    @Mock
    private ClientesRepository clientesRepository;
    @Mock
    private ProdutosRepository produtosRepository;
    @Mock
    private EstoqueRepository estoqueRepository;
    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private ServicoDesconto servicoDesconto;
    @Mock
    private ServicoImposto servicoImposto;

    private PedidoService pedidoService;
    private Cliente cliente;
    private Produto produto;
    private Receita receita;
    private Ingrediente ingrediente;

    @BeforeEach
    void prepararCenario() {
        MockitoAnnotations.openMocks(this);
        
        pedidoService = new PedidoService(
            clientesRepository,
            produtosRepository,
            estoqueRepository,
            pedidoRepository,
            servicoDesconto,
            servicoImposto
        );

        // Preparar cliente padrão
        cliente = new Cliente("12345678900", "João Silva", "11999999999", "Rua das Flores, 123", "joao@email.com");

        // Preparar ingrediente
        ingrediente = new Ingrediente(1L, "Queijo");

        // Preparar receita com ingrediente
        receita = new Receita(1L, "Pizza Calabresa", List.of(ingrediente));

        // Preparar produto padrão
        produto = new Produto(1L, "Pizza Calabresa", receita, 5500);

        // Configurar comportamentos padrão dos mocks
        when(clientesRepository.recuperaClientePorCpf("12345678900")).thenReturn(cliente);
        when(produtosRepository.recuperaProdutoPorid(1L)).thenReturn(produto);
        when(servicoDesconto.calcular(any(Cliente.class),anyDouble())).thenReturn(0.0);
        when(servicoImposto.calcular(anyDouble())).thenReturn(0.0);
    }

    @Test
    @DisplayName("Deve submeter pedido com estoque suficiente - Status APROVADO")
    void testeSubmeterPedidoComEstoqueSuficiente() {
        // Cenário: Estoque tem quantidade suficiente
        ItemEstoque itemEstoque = new ItemEstoque(ingrediente, 10);
        when(estoqueRepository.recuperaItemEstoquePorIngrediente(1L)).thenReturn(itemEstoque);

        // Criar pedido request
        ItemPedidoRequest itemRequest = new ItemPedidoRequest();
        itemRequest.setProdutoId(1L);
        itemRequest.setQuantidade(1);
        List<ItemPedidoRequest> itens = List.of(itemRequest);

        // Mock para retorno do pedido salvo
        Pedido pedidoEsperado = new Pedido(
            1L, 
            cliente, 
            LocalDateTime.now(), 
            new ArrayList<>(), 
            Pedido.Status.APROVADO,
            5500.0, 
            0.0, 
            0.0, 
            5500.0
        );
        when(pedidoRepository.salva(any(Pedido.class))).thenReturn(pedidoEsperado);

        // Ação: Submeter pedido
        Pedido pedidoRetornado = pedidoService.submeterPedidoParaAprovacao("12345678900", itens);

        // Verificação
        assertEquals(Pedido.Status.APROVADO, pedidoRetornado.getStatus());
        assertEquals(5500.0, pedidoRetornado.getValor());
        verify(estoqueRepository).atualizaQuantidade(1L, 9);
    }

    @Test
    @DisplayName("Deve submeter pedido com estoque insuficiente - Status NEGADO")
    void testeSubmeterPedidoComEstoqueInsuficiente() {
        // Cenário: Estoque insuficiente (tem 1, precisa 2)
        ItemEstoque itemEstoque = new ItemEstoque(ingrediente, 1);
        when(estoqueRepository.recuperaItemEstoquePorIngrediente(1L)).thenReturn(itemEstoque);

        // Criar pedido request com quantidade 2
        ItemPedidoRequest itemRequest = new ItemPedidoRequest();
        itemRequest.setProdutoId(1L);
        itemRequest.setQuantidade(2);
        List<ItemPedidoRequest> itens = List.of(itemRequest);

        // Mock para retorno do pedido salvo
        Pedido pedidoEsperado = new Pedido(
            1L,
            cliente,
            LocalDateTime.now(),
            new ArrayList<>(),
            Pedido.Status.NEGADO,
            11000.0,
            0.0,
            0.0,
            11000.0
        );
        when(pedidoRepository.salva(any(Pedido.class))).thenReturn(pedidoEsperado);

        // Ação: Submeter pedido
        Pedido pedidoRetornado = pedidoService.submeterPedidoParaAprovacao("12345678900", itens);

        // Verificação
        assertEquals(Pedido.Status.NEGADO, pedidoRetornado.getStatus());
        verify(estoqueRepository, never()).atualizaQuantidade(anyLong(), anyInt());
    }

    @Test
    @DisplayName("Deve lançar erro ao submeter com cliente não encontrado")
    void testeSubmeterComClienteNaoEncontrado() {
        // Cenário: Cliente não existe
        when(clientesRepository.recuperaClientePorCpf("99999999999")).thenReturn(null);

        // Criar pedido request
        ItemPedidoRequest itemRequest = new ItemPedidoRequest();
        itemRequest.setProdutoId(1L);
        itemRequest.setQuantidade(1);
        List<ItemPedidoRequest> itens = List.of(itemRequest);

        // Verificação: Deve lançar IllegalArgumentException
        assertThrows(
            IllegalArgumentException.class,
            () -> pedidoService.submeterPedidoParaAprovacao("99999999999", itens),
            "Cliente não encontrado: 99999999999"
        );
    }

    @Test
    @DisplayName("Deve lançar erro ao submeter com produto não encontrado")
    void testeSubmeterComProdutoNaoEncontrado() {
        // Cenário: Produto não existe
        when(produtosRepository.recuperaProdutoPorid(999L)).thenReturn(null);

        // Criar pedido request com produto inválido
        ItemPedidoRequest itemRequest = new ItemPedidoRequest();
        itemRequest.setProdutoId(999L);
        itemRequest.setQuantidade(1);
        List<ItemPedidoRequest> itens = List.of(itemRequest);

        // Verificação: Deve lançar IllegalArgumentException
        assertThrows(
            IllegalArgumentException.class,
            () -> pedidoService.submeterPedidoParaAprovacao("12345678900", itens),
            "Produto não encontrado: 999"
        );
    }

    @Test
    @DisplayName("Deve lançar erro com quantidade inválida (zero ou negativa)")
    void testeSubmeterComQuantidadeInvalida() {
        // Cenário: Quantidade inválida (zero)
        ItemPedidoRequest itemRequest = new ItemPedidoRequest();
        itemRequest.setProdutoId(1L);
        itemRequest.setQuantidade(0);
        List<ItemPedidoRequest> itens = List.of(itemRequest);

        // Verificação: Deve lançar IllegalArgumentException
        assertThrows(
            IllegalArgumentException.class,
            () -> pedidoService.submeterPedidoParaAprovacao("12345678900", itens)
        );
    }

    @Test
    @DisplayName("Deve calcular corretamente valor, impostos e desconto")
    void testeCalculoPrecosCorretamente() {
        // Cenário: Estoque suficiente e cálculos de preço
        ItemEstoque itemEstoque = new ItemEstoque(ingrediente, 10);
        when(estoqueRepository.recuperaItemEstoquePorIngrediente(1L)).thenReturn(itemEstoque);
        when(servicoDesconto.calcular(cliente, 5500.0)).thenReturn(385.0); // 7% de desconto
        when(servicoImposto.calcular(5500.0)).thenReturn(550.0);   // 10% de imposto

        // Criar pedido request
        ItemPedidoRequest itemRequest = new ItemPedidoRequest();
        itemRequest.setProdutoId(1L);
        itemRequest.setQuantidade(1);
        List<ItemPedidoRequest> itens = List.of(itemRequest);

        // Mock para retorno do pedido salvo
        Pedido pedidoEsperado = new Pedido(
            1L,
            cliente,
            LocalDateTime.now(),
            new ArrayList<>(),
            Pedido.Status.APROVADO,
            5500.0,  // valor bruto
            550.0,   // impostos
            385.0,   // desconto
            5665.0   // valor cobrado (5500 + 550 - 385)
        );
        when(pedidoRepository.salva(any(Pedido.class))).thenReturn(pedidoEsperado);

        // Ação: Submeter pedido
        Pedido pedidoRetornado = pedidoService.submeterPedidoParaAprovacao("12345678900", itens);

        // Verificação
        assertEquals(5500.0, pedidoRetornado.getValor());
        assertEquals(550.0, pedidoRetornado.getImpostos());
        assertEquals(385.0, pedidoRetornado.getDesconto());
        assertEquals(5665.0, pedidoRetornado.getValorCobrado());
    }

    @Test
    @DisplayName("Deve recuperar pedido por ID")
    void testeRecuperarPedidoPorId() {
        // Cenário: Pedido existe no banco
        Pedido pedidoEsperado = new Pedido(
            1L,
            cliente,
            LocalDateTime.now(),
            new ArrayList<>(),
            Pedido.Status.APROVADO,
            5500.0,
            550.0,
            385.0,
            5665.0
        );
        when(pedidoRepository.recuperaPedido(1L)).thenReturn(pedidoEsperado);

        // Ação: Recuperar pedido
        Pedido pedidoRetornado = pedidoService.recuperaPedido(1L);

        // Verificação
        assertNotNull(pedidoRetornado);
        assertEquals(1L, pedidoRetornado.getId());
        assertEquals(Pedido.Status.APROVADO, pedidoRetornado.getStatus());
    }

    @Test
    @DisplayName("Deve cancelar pedido aprovado e restaurar o estoque")
    void testeCancelarPedidoAprovado() {
        Pedido pedidoExistente = new Pedido(
            1L,
            cliente,
            LocalDateTime.now(),
            List.of(new ItemPedido(produto, 1)),
            Pedido.Status.APROVADO,
            5500.0,
            0.0,
            0.0,
            5500.0
        );

        when(pedidoRepository.recuperaPedido(1L)).thenReturn(pedidoExistente);
        when(estoqueRepository.recuperaItemEstoquePorIngrediente(1L)).thenReturn(new ItemEstoque(ingrediente, 10));
        when(pedidoRepository.atualiza(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        boolean sucesso = pedidoService.cancelarPedido(1L);

        assertTrue(sucesso);
        verify(estoqueRepository).atualizaQuantidade(1L, 11);
        verify(pedidoRepository).atualiza(any(Pedido.class));
    }

    @Test
    @DisplayName("Não deve cancelar pedido entregue")
    void testeNaoCancelarPedidoEntregue() {
        Pedido pedidoExistente = new Pedido(
            1L,
            cliente,
            LocalDateTime.now(),
            new ArrayList<>(),
            Pedido.Status.ENTREGUE,
            5500.0,
            0.0,
            0.0,
            5500.0
        );

        when(pedidoRepository.recuperaPedido(1L)).thenReturn(pedidoExistente);

        boolean sucesso = pedidoService.cancelarPedido(1L);

        assertFalse(sucesso);
        verify(pedidoRepository, never()).atualiza(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve recuperar lista de todos os pedidos")
    void testeRecuperarTodosPedidos() {
        // Cenário: Existem múltiplos pedidos
        List<Pedido> pedidosEsperados = new ArrayList<>();
        pedidosEsperados.add(new Pedido(
            1L, cliente, LocalDateTime.now(), new ArrayList<>(), 
            Pedido.Status.APROVADO, 5500.0, 550.0, 385.0, 5665.0
        ));
        pedidosEsperados.add(new Pedido(
            2L, cliente, LocalDateTime.now(), new ArrayList<>(),
            Pedido.Status.NEGADO, 6000.0, 600.0, 420.0, 6180.0
        ));

        when(pedidoRepository.recuperaTodos()).thenReturn(pedidosEsperados);

        // Ação: Recuperar todos
        List<Pedido> pedidosRetornados = pedidoService.recuperaTodosPedidos();

        // Verificação
        assertEquals(2, pedidosRetornados.size());
        assertEquals(Pedido.Status.APROVADO, pedidosRetornados.get(0).getStatus());
        assertEquals(Pedido.Status.NEGADO, pedidosRetornados.get(1).getStatus());
    }
}
