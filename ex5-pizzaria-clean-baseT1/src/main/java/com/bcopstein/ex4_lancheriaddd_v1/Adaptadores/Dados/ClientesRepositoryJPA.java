package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClientesRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@Repository
@Primary 
public class ClientesRepositoryJPA implements ClientesRepository {
    
    private final ClienteSpringDataRepository jpaRepository;

    @Autowired
    public ClientesRepositoryJPA(ClienteSpringDataRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Cliente recuperaClientePorCpf(String cpf) {
        return jpaRepository.findById(cpf).orElse(null);
    }

    @Override
    public Cliente salvaCliente(Cliente cliente) {
       return jpaRepository.save(cliente);
    }
}