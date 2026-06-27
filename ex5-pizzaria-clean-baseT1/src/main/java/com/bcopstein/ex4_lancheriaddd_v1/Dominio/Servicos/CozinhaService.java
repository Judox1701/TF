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
public class CozinhaService implements ICozinhaService {
    private Queue<Pedido> filaEntrada;
    private Pedido emPreparacao;
    private Queue<Pedido> filaSaida;
    private final PedidoRepository pedidoRepository; 
    private final IEntregaService entregaService; 
    private ScheduledExecutorService scheduler;

    @Autowired 
    public CozinhaService(PedidoRepository pedidoRepository, IEntregaService entregaService) {
        this.pedidoRepository = pedidoRepository;
        this.entregaService = entregaService; 
        filaEntrada = new LinkedBlockingQueue<Pedido>();
        emPreparacao = null;
        filaSaida = new LinkedBlockingQueue<Pedido>();
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    private synchronized void colocaEmPreparacao(Pedido pedido){
        pedido.setStatus(Pedido.Status.PREPARACAO);
        
        pedidoRepository.atualiza(pedido); 
        
        emPreparacao = pedido;
        System.out.println("Pedido em preparacao: "+pedido.getId());
        scheduler.schedule(() -> pedidoPronto(), 5, TimeUnit.SECONDS);
    }

    @Override
    public synchronized void chegadaDePedido(Pedido p) {
        filaEntrada.add(p);
        System.out.println("Pedido na fila de entrada da cozinha: "+p.getId());
        if (emPreparacao == null) {
            colocaEmPreparacao(filaEntrada.poll());
        }
    }

    @Override
    public synchronized void pedidoPronto() {
        emPreparacao.setStatus(Pedido.Status.PRONTO);
        pedidoRepository.atualiza(emPreparacao);
        filaSaida.add(emPreparacao);
        System.out.println("Pedido na fila de saida: "+emPreparacao.getId());
        
        entregaService.chegadaDePedidoPronto(emPreparacao);

        emPreparacao = null;
        if (!filaEntrada.isEmpty()){
            Pedido prox = filaEntrada.poll();
            scheduler.schedule(() -> colocaEmPreparacao(prox), 1, TimeUnit.SECONDS);
        }
    }
}