package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Desconto;

@Service
public class ServicoDesconto {

    private String descontoCorrente;
    private Map<String, Desconto> regra;
    private final PedidoRepository pedidoRepository;
    
    @Autowired
    public ServicoDesconto(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.descontoCorrente = "1";
        this.regra = new HashMap<>();

        // desconto padrão
        regra.put("1", new Desconto("1","Desconto base 10%." , (valor, qtd) -> valor * 0.10));

        // regra de 3 pedidos nos ultimos 20 dias
        regra.put("2", new Desconto("2","Desconto fidelidade 7% caso feito mais de 3 pedidos nos ultimos 20 dias." , (valor, qtdPedidos) -> {
        if (qtdPedidos > 3) {
            return valor * 0.07; // 7% de desconto
        } else {
            return 0.0;
        }
        }));
        
        
    }

    public List<String> politicasDisponiveis() {
        return regra.values().stream().map(Desconto::getDesc).collect(Collectors.toList());
    }

    public void defineDescontoCorrente(String id) {
        if (!regra.containsKey(id)) {
            throw new IllegalArgumentException("Política de desconto não encontrada: " + id);
        }
        this.descontoCorrente = id;
    }

    public String getDescontoCorrente() {
        return descontoCorrente;
    }

    public String getDescricaoDescontoCorrente() {
        Desconto desconto = regra.get(this.descontoCorrente);
        return desconto != null ? desconto.getDesc() : "Nenhum desconto ativo";
    }

    public double calcular(Cliente cliente, double valorDaVenda) {
        Desconto desconto = regra.get(this.descontoCorrente);
        if (desconto == null) {
            throw new IllegalArgumentException("Desconto não encontrado: " + this.descontoCorrente);
        }

        // difinir 20 dias pra verificar
        LocalDateTime limiteData = LocalDateTime.now().minusDays(20);

        // verifica os pedidos que batem com o tempo
        int qtdPedidosRecentes = (int) pedidoRepository.recuperaTodos().stream()
                .filter(p -> p.getCliente().getCpf().equals(cliente.getCpf()))
                .filter(p -> p.getDataHoraPagamento() != null && p.getDataHoraPagamento().isAfter(limiteData))
                .count();

        return desconto.calculaDesconto(valorDaVenda, qtdPedidosRecentes);
    }
}
