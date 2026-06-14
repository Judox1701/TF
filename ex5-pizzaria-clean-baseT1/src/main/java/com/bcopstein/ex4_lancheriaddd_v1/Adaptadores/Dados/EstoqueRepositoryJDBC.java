package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.EstoqueRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;

@Repository
public class EstoqueRepositoryJDBC implements EstoqueRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EstoqueRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ItemEstoque recuperaItemEstoquePorIngrediente(long ingredienteId) {
        String sql = "SELECT ie.quantidade, i.id AS ingrediente_id, i.descricao " +
                     "FROM itensEstoque ie " +
                     "JOIN ingredientes i ON ie.ingrediente_id = i.id " +
                     "WHERE i.id = ?";
        List<ItemEstoque> itens = jdbcTemplate.query(
                sql,
                ps -> ps.setLong(1, ingredienteId),
                (rs, rowNum) -> new ItemEstoque(
                        new Ingrediente(rs.getLong("ingrediente_id"), rs.getString("descricao")),
                        rs.getInt("quantidade")));
        return itens.isEmpty() ? null : itens.get(0);
    }

    @Override
    public void atualizaQuantidade(long ingredienteId, int quantidade) {
        String sql = "UPDATE itensEstoque SET quantidade = ? WHERE ingrediente_id = ?";
        jdbcTemplate.update(sql, quantidade, ingredienteId);
    }
}
