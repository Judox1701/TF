package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClientesRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@Repository
public class ClientesRepositoryJDBC implements ClientesRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ClientesRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Cliente recuperaClientePorCpf(String cpf) {
        String sql = "SELECT cpf, nome, celular, endereco, email FROM clientes WHERE cpf = ?";
        List<Cliente> clientes = jdbcTemplate.query(
                sql,
                ps -> ps.setString(1, cpf),
                (rs, rowNum) -> new Cliente(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("celular"),
                        rs.getString("endereco"),
                        rs.getString("email")));
        return clientes.isEmpty() ? null : clientes.get(0);
    }
}
