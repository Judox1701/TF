package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClientesRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private ClientesRepository clientesRepository; 

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarUsuario(@RequestBody Cliente novoCliente) {
        clientesRepository.salvaCliente(novoCliente);

        return ResponseEntity.ok("Cliente cadastrado com sucesso! Agora ele já pode fazer login.");
    }
}