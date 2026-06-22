package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.PedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@RestController
@RequestMapping("/submeter")
public class SubmeterPedidoController {

    private final SubmeterPedidoUC submeterPedidoUC;

    @Autowired
    public SubmeterPedidoController(SubmeterPedidoUC submeterPedidoUC){
        this.submeterPedidoUC = submeterPedidoUC;
    }

    @PostMapping("/submeterPedidos")
    public ResponseEntity<?> submeter(@RequestBody PedidoRequest dto){

        Pedido resultado = submeterPedidoUC.run(dto);

        if (resultado.getStatus() == Pedido.Status.NEGADO){
            return ResponseEntity.status(422).body(resultado);
        }

        return ResponseEntity.ok(resultado);
    }
}
