package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClientesRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

@Repository
public class PedidoRepositoryJDBC implements PedidoRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ClientesRepository clientesRepository;
    private final ProdutosRepository produtosRepository;

    @Autowired
    public PedidoRepositoryJDBC(JdbcTemplate jdbcTemplate,
                                ClientesRepository clientesRepository,
                                ProdutosRepository produtosRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.clientesRepository = clientesRepository;
        this.produtosRepository = produtosRepository;
    }

    @Override
    public Pedido salva(Pedido pedido) {
        String insertPedido = "INSERT INTO pedidos " +
                "(cliente_cpf, status, valor, impostos, desconto, valor_cobrado, data_hora) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertPedido, new String[] { "id" });
            ps.setString(1, pedido.getCliente().getCpf());
            ps.setString(2, pedido.getStatus().name());
            ps.setDouble(3, pedido.getValor());
            ps.setDouble(4, pedido.getImpostos());
            ps.setDouble(5, pedido.getDesconto());
            ps.setDouble(6, pedido.getValorCobrado());
            ps.setTimestamp(7, Timestamp.valueOf(pedido.getDataHoraPagamento()));
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        long pedidoId = key != null ? key.longValue() : 0L;

        for (ItemPedido item : pedido.getItens()) {
            jdbcTemplate.update("INSERT INTO pedido_item (pedido_id, cliente_cpf, produto_id, quantidade) VALUES (?, ?, ?, ?)",
                    pedidoId,
                    pedido.getCliente().getCpf(),
                    item.getItem().getId(),
                    item.getQuantidade());
        }

        return new Pedido(pedidoId, pedido.getCliente(), pedido.getDataHoraPagamento(), pedido.getItens(), pedido.getStatus(),
                pedido.getValor(), pedido.getImpostos(), pedido.getDesconto(), pedido.getValorCobrado());
    }

    @Override
    public Pedido recuperaPedido(long id) {
        String sql = "SELECT cliente_cpf, status, valor, impostos, desconto, valor_cobrado, data_hora " +
                "FROM pedidos WHERE id = ?";

        List<Pedido> pedidos = jdbcTemplate.query(
                sql,
                ps -> ps.setLong(1, id),
                (rs, rowNum) -> {
                    String clienteCpf = rs.getString("cliente_cpf");
                    String status = rs.getString("status");
                    double valor = rs.getDouble("valor");
                    double impostos = rs.getDouble("impostos");
                    double desconto = rs.getDouble("desconto");
                    double valorCobrado = rs.getDouble("valor_cobrado");
                    java.sql.Timestamp dataHora = rs.getTimestamp("data_hora");
                    var cliente = clientesRepository.recuperaClientePorCpf(clienteCpf);
                    var itens = recuperaItens(id);
                    Pedido.Status pedidoStatus = Pedido.Status.valueOf(status);
                    return new Pedido(id, cliente, dataHora.toLocalDateTime(), itens, pedidoStatus, valor, impostos, desconto, valorCobrado);
                });

        return pedidos.isEmpty() ? null : pedidos.get(0);
    }

    private List<ItemPedido> recuperaItens(long pedidoId) {
        String sql = "SELECT produto_id, quantidade FROM pedido_item WHERE pedido_id = ?";
        return jdbcTemplate.query(
                sql,
                ps -> ps.setLong(1, pedidoId),
                (rs, rowNum) -> {
                    long produtoId = rs.getLong("produto_id");
                    int quantidade = rs.getInt("quantidade");
                    Produto produto = produtosRepository.recuperaProdutoPorid(produtoId);
                    return new ItemPedido(produto, quantidade);
                });
    }

    @Override
    public List<Pedido> recuperaTodos() {
        String sql = "SELECT id, cliente_cpf, status, valor, impostos, desconto, valor_cobrado, data_hora " +
                "FROM pedidos ORDER BY data_hora DESC";

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    long id = rs.getLong("id");
                    String clienteCpf = rs.getString("cliente_cpf");
                    String status = rs.getString("status");
                    double valor = rs.getDouble("valor");
                    double impostos = rs.getDouble("impostos");
                    double desconto = rs.getDouble("desconto");
                    double valorCobrado = rs.getDouble("valor_cobrado");
                    java.sql.Timestamp dataHora = rs.getTimestamp("data_hora");
                    var cliente = clientesRepository.recuperaClientePorCpf(clienteCpf);
                    var itens = recuperaItens(id);
                    Pedido.Status pedidoStatus = Pedido.Status.valueOf(status);
                    return new Pedido(id, cliente, dataHora.toLocalDateTime(), itens, pedidoStatus, valor, impostos, desconto, valorCobrado);
                });
    }
}
