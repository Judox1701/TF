package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.CadastrarClienteUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.CadastrarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@RestController
@RequestMapping("/cliente")
public class CadastrarClienteController {
    private final CadastrarClienteUC cadastrarClienteUC;

    @Autowired
    public CadastrarClienteController(CadastrarClienteUC cadastrarClienteUC) {
        this.cadastrarClienteUC = cadastrarClienteUC;
    }

    @PostMapping("/cadastrar")
    @CrossOrigin("*")
    public ResponseEntity<?> cadastrar(@RequestBody CadastrarClienteRequest request) {
        Cliente cliente = cadastrarClienteUC.run(request);
        return ResponseEntity.ok(cliente);
    }
}
