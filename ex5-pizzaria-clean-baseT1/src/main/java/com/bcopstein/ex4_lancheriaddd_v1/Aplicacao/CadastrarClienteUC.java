package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.CadastrarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ClienteService;

@Component
public class CadastrarClienteUC {
    private final ClienteService clienteService;

    @Autowired
    public CadastrarClienteUC(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public Cliente run(CadastrarClienteRequest request) {
        return clienteService.cadastrarCliente(
                request.getCpf(),
                request.getNome(),
                request.getCelular(),
                request.getUsuario(),
                request.getSenha());
    }
}
