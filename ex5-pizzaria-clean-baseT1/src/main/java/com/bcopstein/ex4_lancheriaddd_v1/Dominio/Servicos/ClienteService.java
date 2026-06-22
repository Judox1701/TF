package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClientesRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@Service
public class ClienteService {
    private final ClientesRepository clientesRepository;

    @Autowired
    public ClienteService(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }

    public Cliente cadastrarCliente(String cpf, String nome, String celular, String usuario, String senha) {
        if (clientesRepository.recuperaClientePorCpf(cpf) != null) {
            throw new IllegalArgumentException("CPF já cadastrado: " + cpf);
        }
        Cliente cliente = new Cliente(cpf, nome, celular, usuario, senha, null, null);
        return clientesRepository.salvaCliente(cliente);
    }
}
