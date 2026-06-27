package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class EntregaService implements IEntregaService {
    private Queue<Pedido> filaProntos;
    private Pedido emTransporte;
    private final PedidoRepository pedidoRepository;
    private ScheduledExecutorService scheduler;

    @Autowired
    public EntregaService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.filaProntos = new LinkedBlockingQueue<Pedido>();
        this.emTransporte = null;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    private synchronized void iniciaTransporte(Pedido pedido) {
        pedido.setStatus(Pedido.Status.TRANSPORTE);
        pedidoRepository.atualiza(pedido);
        
        emTransporte = pedido;
        System.out.println("Entregador saiu com o pedido: " + pedido.getId());
        
        // Simula 10 segundos de viagem de moto até a casa do cliente
        scheduler.schedule(() -> pedidoEntregue(), 10, TimeUnit.SECONDS);
    }

    @Override
    public synchronized void chegadaDePedidoPronto(Pedido p) {
        filaProntos.add(p);
        System.out.println("Pedido na prateleira aguardando entregador: " + p.getId());
        
        // Se o entregador estiver livre, ele já pega o pedido
        if (emTransporte == null) {
            iniciaTransporte(filaProntos.poll());
        }
    }

    @Override
    public synchronized void pedidoEntregue() {
        emTransporte.setStatus(Pedido.Status.ENTREGUE);
        pedidoRepository.atualiza(emTransporte);
        
        System.out.println("Pedido entregue com sucesso: " + emTransporte.getId());
        emTransporte = null;
        
        // Tem mais pedido pronto na fila?
        if (!filaProntos.isEmpty()) {
            Pedido prox = filaProntos.poll();
            scheduler.schedule(() -> iniciaTransporte(prox), 2, TimeUnit.SECONDS);
        }
    }
}