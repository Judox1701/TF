package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@RestController
@RequestMapping("/cancelar")
public class CancelarPedidoController{
    private PedidoService pedidoService;

    @Autowired
    public CancelarPedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @DeleteMapping("/pedidos/{id}/confirmar")
    public ResponseEntity<?> cancelarPedido(@PathVariable("id") Long id) {
        boolean sucesso = pedidoService.cancelarPedido(id);

        if (!sucesso) {
            return ResponseEntity.status(422)
                    .body("{\"erro\": \"Pedido não pode ser cancelado. Verifique o status.\"}");
        }

        return ResponseEntity.ok("{\"status\": \"CANCELADO\"}");
    }
}
